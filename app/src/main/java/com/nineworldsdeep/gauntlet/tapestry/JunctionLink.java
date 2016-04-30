package com.nineworldsdeep.gauntlet.tapestry;

/**
 * Created by brent on 4/30/16.
 */
public class JunctionLink extends TapestryNodeLink {
    public JunctionLink(String nodeName) {
        super(nodeName, LinkType.JunctionLink);
    }

    public String toLineItem(){
        //junction links are only ever dynamically generated
        //this prevents them from ending up in a link
        //list accidentally
        return "";
    }
}
