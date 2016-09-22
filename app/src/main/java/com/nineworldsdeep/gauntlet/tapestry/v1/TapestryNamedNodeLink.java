package com.nineworldsdeep.gauntlet.tapestry.v1;

import android.content.Context;
import android.content.Intent;

import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.R;

import java.util.HashMap;

/**
 * Created by brent on 4/29/16.
 */
public abstract class TapestryNamedNodeLink extends HashMap<String, String> {

    //Link Type (ParentLink, ChildLink,
    //           PeerLink, ImageLink,
    //           SynergyListLink, AudioLink)


    public TapestryNamedNodeLink(String nodeName, LinkType linkType){

        put("img", String.valueOf(R.mipmap.ic_nwd_peer));
        put("nodeName", nodeName);
        put("linkType", linkType.toString());
    }

    public Intent getIntent(Context c){

        Intent intent = new Intent(c,
                TapestryNamedNodeActivity.class);
        intent.putExtra(
                TapestryNamedNodeActivity.EXTRA_CURRENT_NODE_NAME,
                getNodeName());

        return intent;
    }

    public String getNodeName(){

        return get("nodeName");
    }

    public LinkType getLinkType(){

        return LinkType.valueOf(get("linkType"));
    }

    public static int getLayout() {

        return R.layout.tapestry_node_link;
    }

    public static String[] getMapKeysForView() {

        return  new String[] {"img",
                              "nodeName",
                              "linkType"};
    }

    public static int[] getIdsForViewElements() {

        return new int[] {R.id.img,
                          R.id.node_name,
                          R.id.link_type};
    }

    public String toLineItem() {

        String output = "";

        output += "nodeName={" + getNodeName() + "} ";
        output += "linkType={" + getLinkType().toString() + "} ";

        return output;
    }

    public static TapestryNamedNodeLink fromLineItem(String lineItem) {

        String nodeName = Parser.extract("nodeName", lineItem);
        LinkType linkType = LinkType.valueOf(Parser.extract("linkType", lineItem));

        TapestryNamedNodeLink nodeLink = null;

        switch (linkType){

            case AudioLink:

                nodeLink = AudioLink.fromLineItem(nodeName, lineItem);
                break;

            case ChildLink:

                nodeLink = new ChildLink(nodeName);
                break;

            case ImageLink:

                nodeLink = ImageLink.fromLineItem(nodeName, lineItem);
                break;

            case ParentLink:

                nodeLink = new ParentLink(nodeName);
                break;

            case PeerLink:

                nodeLink = new PeerLink(nodeName);
                break;

            case SynergyListLink:

                nodeLink = new SynergyListLink(nodeName);
                break;
        }

        return nodeLink;
    }

    public TapestryNamedNodeLink remapTo(TapestryNamedNode newNode){

        put("nodeName", newNode.getNodeName());
        return this;
    }
}
