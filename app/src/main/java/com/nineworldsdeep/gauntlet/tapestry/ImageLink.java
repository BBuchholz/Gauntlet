package com.nineworldsdeep.gauntlet.tapestry;

import android.provider.MediaStore;

import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

/**
 * Created by brent on 4/29/16.
 */
public class ImageLink extends HashedPathLink {

    public ImageLink(String imageDisplayName) {

        super(imageDisplayName, LinkType.ImageLink);

    }

    public static ImageLink fromLineItem(String nodeName, String lineItem){

        ImageLink lnk = new ImageLink(nodeName);

        lnk.extractHashedPathFromLineItem(lineItem);

        return lnk;
    }
}
