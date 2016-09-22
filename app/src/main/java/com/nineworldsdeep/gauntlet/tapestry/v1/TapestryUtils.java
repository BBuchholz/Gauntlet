package com.nineworldsdeep.gauntlet.tapestry.v1;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;

/**
 * Created by brent on 4/28/16.
 */
public class TapestryUtils {

    public static ArrayList<TapestryNamedNodeLink> getNodeLinks(String nodeName) {
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

        TapestryNamedNode nd = new TapestryNamedNode(nodeName);

        return nd.getLinks();
    }

    public static String processNodeName(String name) {

        return Utils.processName(name);
    }

    public static void linkNodeToImagePath(String nodeName, String imagePath){

        TapestryNamedNode nd = new TapestryNamedNode(nodeName);
        ImageLink lnk = new ImageLink(imagePath);

        nd.add(lnk);
        nd.save();
    }

    public static void linkNodeToAudioPath(String nodeName, String audioPath){

        TapestryNamedNode nd = new TapestryNamedNode(nodeName);
        AudioLink lnk = new AudioLink(audioPath);

        nd.add(lnk);
        nd.save();
    }

    public static void linkNodeToSynergyList(String nodeName, String listName){

        TapestryNamedNode nd = new TapestryNamedNode(nodeName);
        SynergyListLink lnk = new SynergyListLink(listName);

        nd.add(lnk);
        nd.save();
    }

    public static void linkNodes(String fromNodeName, String toNodeName, LinkType linkType) {

        TapestryNamedNode fromNode = new TapestryNamedNode(fromNodeName);
        TapestryNamedNode toNode = new TapestryNamedNode(toNodeName);

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
        TapestryNamedNode nd = null;

        String lastFoundName = "";

        do{
            gName = "Gardens-" + dateTimeStamp + "_" +
                    letters + "_" +  currentDevice;

            nd = new TapestryNamedNode(gName);

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

        // http://stackoverflow.com/questions/342052/how-to-increment-a-java-string-through-all-the-possibilities

        int length = letters.length();
        char c = letters.charAt(length - 1);

        if(c == 'z')
            return length > 1 ? incrementLetters(letters.substring(0, length - 1)) + 'a' : "aa";

        return letters.substring(0, length - 1) + ++c;
    }

    public static String getCurrentDeviceName() {

        return new ConfigFile().getDeviceName();
    }

    public static String getNewGardenName(String currentDevice) {

        //try current date, garden a, for current device
        String dateTimeStamp = Utils.getCurrentTimeStamp_yyyyMMdd();
        String letters = "a";
        String gardenName = "";
        TapestryNamedNode nd = null;

        do{
            gardenName = "Gardens-" + dateTimeStamp + "_" +
                    letters + "_" +  currentDevice;

            nd = new TapestryNamedNode(gardenName);

            letters = incrementLetters(letters);

        }while(nd.exists());

        return gardenName;
    }

    public static ArrayList<MetaEntry> getMetaEntries(String nodeName) {

        TapestryNamedNode nd = new TapestryNamedNode(nodeName);

        ArrayList<MetaEntry> lst =
                new ArrayList<>();

        for(TapestryNamedNodeLink lnk : nd.getLinks()){

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

        TapestryNamedNode fromNd = new TapestryNamedNode(fromNodeName);
        TapestryNamedNode toNd = new TapestryNamedNode(toNodeName);

        for (TapestryNamedNodeLink lnk: fromNd.getLinks()) {

            if(!lnk.getNodeName().startsWith(ignoreLinksStartingWith)) {

                toNd.add(lnk);
            }
        }

        toNd.save();

        fromNd.remapExternalLinksTo(toNd);

        fromNd.delete();
    }
}
