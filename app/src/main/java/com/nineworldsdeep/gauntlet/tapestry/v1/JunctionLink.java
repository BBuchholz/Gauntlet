package com.nineworldsdeep.gauntlet.tapestry.v1;

import com.nineworldsdeep.gauntlet.R;

/**
 * Created by brent on 4/30/16.
 */
public class JunctionLink extends TapestryNodeLink {
    public JunctionLink(String nodeName) {
        super(nodeName, LinkType.JunctionLink);
        put("img", String.valueOf(R.mipmap.ic_nwd_junction));
    }

    public String toLineItem(){
        //junction links are only ever dynamically generated
        //this prevents them from ending up in a link
        //list accidentally
        return "";
    }
}
