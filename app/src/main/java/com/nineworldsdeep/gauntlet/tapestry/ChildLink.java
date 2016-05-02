package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.R;

/**
 * Created by brent on 4/29/16.
 */
public class ChildLink extends TapestryNodeLink {
    public ChildLink(String nodeName) {
        super(nodeName, LinkType.ChildLink);
        put("img", String.valueOf(R.mipmap.ic_nwd_singlenode));
    }
}
