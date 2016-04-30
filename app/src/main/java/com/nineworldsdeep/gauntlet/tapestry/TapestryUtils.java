package com.nineworldsdeep.gauntlet.tapestry;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;

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

}
