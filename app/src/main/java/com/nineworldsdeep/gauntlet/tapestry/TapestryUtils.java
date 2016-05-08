package com.nineworldsdeep.gauntlet.tapestry;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by brent on 4/28/16.
 */
public class TapestryUtils {

    public static ArrayList<TapestryNodeLink> getNodeLinks(String nodeName) {
//
//       ArrayList<TapestryNodeLink> lst =
//               new ArrayList<>();

        //for testing let's generate one of each

//        lst.add(new ParentLink("MotherNode"));
//        lst.add(new ChildLink("TestChild"));
//        lst.add(new PeerLink("TestPeer"));
//        lst.add(new ImageLink("Image Display Name"));
//        lst.add(new SynergyListLink("TestSynergyList"));
//        lst.add(new AudioLink("Audio Display Name"));

        TapestryNode nd = new TapestryNode(nodeName);

        return nd.getLinks();
    }

    public static String processNodeName(String name) {

        return Utils.processName(name);
    }

    public static void linkNodeToImagePath(String nodeName, String imagePath){

        TapestryNode nd = new TapestryNode(nodeName);
        ImageLink lnk = new ImageLink(imagePath);

        nd.add(lnk);
        nd.save();
    }

    public static void linkNodeToAudioPath(String nodeName, String audioPath){

        TapestryNode nd = new TapestryNode(nodeName);
        AudioLink lnk = new AudioLink(audioPath);

        nd.add(lnk);
        nd.save();
    }

    public static void linkNodeToSynergyList(String nodeName, String listName){

        TapestryNode nd = new TapestryNode(nodeName);
        SynergyListLink lnk = new SynergyListLink(listName);

        nd.add(lnk);
        nd.save();
    }

    public static void linkNodes(String fromNodeName, String toNodeName, LinkType linkType) {

        TapestryNode fromNode = new TapestryNode(fromNodeName);
        TapestryNode toNode = new TapestryNode(toNodeName);

        //switch enum?
        switch (linkType){

            case ParentLink:

                //add parent linkNodes to fromNode
                fromNode.add(new ParentLink(toNodeName));
                //add child linkNodes to toNode
                toNode.add(new ChildLink(fromNodeName));

                fromNode.save();
                toNode.save();

                return;

            case PeerLink:

                //add peer linkNodes to both nodes
                fromNode.add(new PeerLink(toNodeName));
                toNode.add(new PeerLink(fromNodeName));

                fromNode.save();
                toNode.save();

                return;

            case ChildLink:

                //add child linkNodes to fromNode
                fromNode.add(new ChildLink(toNodeName));
                //add parent linkNodes to toNode
                toNode.add(new ParentLink(fromNodeName));

                fromNode.save();
                toNode.save();

                return;
        }
    }

    public static void linkNodes(Context c, String fromNodeName, String toNodeName, LinkType linkType) {

        //this one allows toasts

        linkNodes(fromNodeName, toNodeName, linkType);

        Utils.toast(c, linkType + " from " + fromNodeName + " to " + toNodeName + " created.");
    }

    public static String getCurrentGardenName(String currentDevice) {

        //try current date, garden a, for current device
        String dateTimeStamp = Utils.getCurrentTimeStamp_yyyyMMdd();
        String letters = "a";
        String gName = "";
        TapestryNode nd = null;

        String lastFoundName = "";

        do{
            gName = "Gardens-" + dateTimeStamp + "_" +
                    letters + "_" +  currentDevice;

            nd = new TapestryNode(gName);

            if(nd.exists()){

                lastFoundName = gName;
            }

            letters = incrementLetters(letters);

        }while(nd.exists());

        if(Utils.stringIsNullOrWhitespace(lastFoundName)){

            lastFoundName = gName;
        }

        return lastFoundName;
    }

    private static String incrementLetters(String letters) {

        //what we want:
        //a - z for first
        //z then increments to aa
        //next is ab, ac, ad, etc.
        //az increments to ba
        //next is bb, bc, bd, etc.
        //zz increments to aaa
        //

        //get last letter
        //if its less than 'z', increment it
        //if its 'z', append

        //found this: http://stackoverflow.com/questions/342052/how-to-increment-a-java-string-through-all-the-possibilities

        int length = letters.length();
        char c = letters.charAt(length - 1);

        if(c == 'z')
            return length > 1 ? incrementLetters(letters.substring(0, length - 1)) + 'a' : "aa";

        return letters.substring(0, length - 1) + ++c;
    }

    public static String getCurrentDevice() {

        return new ConfigFile().getDeviceName();
    }

    public static String getNewGardenName(String currentDevice) {

        //try current date, garden a, for current device
        String dateTimeStamp = Utils.getCurrentTimeStamp_yyyyMMdd();
        String letters = "a";
        String gardenName = "";
        TapestryNode nd = null;

        do{
            gardenName = "Gardens-" + dateTimeStamp + "_" +
                    letters + "_" +  currentDevice;

            nd = new TapestryNode(gardenName);

            letters = incrementLetters(letters);

        }while(nd.exists());

        return gardenName;
    }

    public static ArrayList<MetaEntry> getMetaEntries(String nodeName) {

        TapestryNode nd = new TapestryNode(nodeName);

        ArrayList<MetaEntry> lst =
                new ArrayList<>();

        for(TapestryNodeLink lnk : nd.getLinks()){

            if(lnk instanceof HashedPathLink){

                HashedPathLink hpl = (HashedPathLink)lnk;

                lst.add(new MetaEntry(hpl));
            }
        }

        return lst;
    }

    public static void transplant(String fromNodeName,
                                  String toNodeName,
                                  String ignoreLinksStartingWith) {

        TapestryNode fromNd = new TapestryNode(fromNodeName);
        TapestryNode toNd = new TapestryNode(toNodeName);

        for (TapestryNodeLink lnk: fromNd.getLinks()) {

            if(!lnk.getNodeName().startsWith(ignoreLinksStartingWith)) {

                toNd.add(lnk);
            }
        }

        toNd.save();

        fromNd.remapExternalLinksTo(toNd);

        //fromNd.delete();
    }
}
