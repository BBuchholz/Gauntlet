package com.nineworldsdeep.gauntlet.tapestry.v1;

import com.nineworldsdeep.gauntlet.R;

/**
 * Created by brent on 4/29/16.
 */
public class ParentLink extends TapestryNamedNodeLink {
    public ParentLink(String nodeName) {
        super(nodeName,LinkType.ParentLink);
        put("img", String.valueOf(R.mipmap.ic_nwd_parent));
    }

}
