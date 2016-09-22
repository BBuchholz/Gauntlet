package com.nineworldsdeep.gauntlet.tapestry.v1;

import com.nineworldsdeep.gauntlet.R;

/**
 * Created by brent on 4/29/16.
 */
public class PeerLink extends TapestryNamedNodeLink {
    public PeerLink(String nodeName) {
        super(nodeName, LinkType.PeerLink);
        put("img", String.valueOf(R.mipmap.ic_nwd_peer));
    }
}
