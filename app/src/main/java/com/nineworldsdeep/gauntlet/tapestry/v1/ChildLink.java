package com.nineworldsdeep.gauntlet.tapestry.v1;

import com.nineworldsdeep.gauntlet.R;

/**
 * Created by brent on 4/29/16.
 */
public class ChildLink extends TapestryNamedNodeLink {
    public ChildLink(String nodeName) {
        super(nodeName, LinkType.ChildLink);
        put("img", String.valueOf(R.mipmap.ic_nwd_singlenode));
    }
}
