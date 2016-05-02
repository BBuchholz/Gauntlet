package com.nineworldsdeep.gauntlet.tapestry;

import android.provider.MediaStore;

import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import org.apache.commons.io.FilenameUtils;

/**
 * Created by brent on 4/29/16.
 */
public class ImageLink extends HashedPathLink {

    public ImageLink(String imagePath) {

        super("Image: " + FilenameUtils.getName(imagePath), LinkType.ImageLink);
        put("img", String.valueOf(R.mipmap.ic_nwd_media));
        setPath(imagePath);
        refreshHash();
    }

    public static ImageLink fromLineItem(String nodeName, String lineItem){

        ImageLink lnk = new ImageLink(nodeName);

        lnk.extractHashedPathFromLineItem(lineItem);

        return lnk;
    }
}
