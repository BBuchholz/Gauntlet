package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

/**
 * Created by brent on 4/29/16.
 */
public class AudioLink extends HashedPathLink {

    public AudioLink(String audioDisplayName) {

        super(audioDisplayName, LinkType.AudioLink);

    }

    public static AudioLink fromLineItem(String nodeName, String lineItem){

        AudioLink lnk = new AudioLink(nodeName);

        lnk.extractHashedPathFromLineItem(lineItem);

        return lnk;
    }

}
