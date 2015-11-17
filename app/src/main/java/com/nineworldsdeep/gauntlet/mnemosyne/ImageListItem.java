package com.nineworldsdeep.gauntlet.mnemosyne;

import java.io.File;

/**
 * Created by brent on 11/17/15.
 */
public class ImageListItem {

    private File file;

    public ImageListItem(String filePath) {

        file = new File(filePath);
    }

    @Override
    public String toString(){

        return file.getName();
    }

    public File getFile() {
        return file;
    }
}
