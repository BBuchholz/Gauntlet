package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.R;

/**
 * Created by brent on 4/29/16.
 */
public class AudioLink extends TapestryNodeLink {
    public AudioLink(String audioDisplayName) {
        super(audioDisplayName, LinkType.AudioLink);

        put("img", String.valueOf(R.mipmap.ic_nwd_singlenode));
    }

}
