package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.R;

/**
 * Created by brent on 4/29/16.
 */
public class ImageLink extends TapestryNodeLink {
    public ImageLink(String imageDisplayName) {
        super(imageDisplayName, LinkType.ImageLink);

        put("img", String.valueOf(R.mipmap.ic_nwd_singlenode));
    }

}
