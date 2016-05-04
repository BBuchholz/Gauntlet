package com.nineworldsdeep.gauntlet.tapestry;

import android.content.Context;
import android.content.Intent;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.mnemosyne.AudioDisplayActivity;

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

    @Override
    public Intent getIntent(Context c) {

        Intent intent = new Intent(c, AudioDisplayActivity.class);
        intent.putExtra(
                AudioDisplayActivity.EXTRA_AUDIOPATH,
                getPath()
        );

        return intent;
    }
}
