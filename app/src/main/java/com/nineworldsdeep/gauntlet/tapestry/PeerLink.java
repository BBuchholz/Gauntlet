package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.R;

/**
 * Created by brent on 4/29/16.
 */
public class PeerLink extends TapestryNodeLink {
    public PeerLink(String nodeName) {
        super(nodeName, LinkType.PeerLink);
        put("img", String.valueOf(R.mipmap.ic_nwd_peer));
    }
}
