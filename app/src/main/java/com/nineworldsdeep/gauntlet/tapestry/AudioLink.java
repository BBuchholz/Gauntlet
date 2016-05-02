package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.R;

import org.apache.commons.io.FilenameUtils;

/**
 * Created by brent on 4/29/16.
 */
public class AudioLink extends HashedPathLink {

    public AudioLink(String audioPath) {

        super(FilenameUtils.getName(audioPath), LinkType.AudioLink);

        put("img", String.valueOf(R.mipmap.ic_nwd_media));

        setPath(audioPath);
        refreshHash();
    }

    public static AudioLink fromLineItem(String nodeName, String lineItem){

        AudioLink lnk = new AudioLink(nodeName);

        lnk.extractHashedPathFromLineItem(lineItem);

        return lnk;
    }

}
