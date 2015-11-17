package com.nineworldsdeep.gauntlet.mnemosyne;

import android.text.Editable;

import java.io.File;

/**
 * Created by brent on 11/17/15.
 */
public class ImageListItem {

    private File file;
    private String displayName = null;

    public ImageListItem(String filePath) {

        file = new File(filePath);

        DisplayNameIndex dni = DisplayNameIndex.getInstance();

        if(dni.hasDisplayName(filePath)){

            displayName = dni.getDisplayName(filePath);
        }
    }

    public ImageListItem(String path, String displayName) {

        this.file = new File(path);
        this.displayName = displayName;
    }

    @Override
    public String toString(){

        if(displayName != null){
            return displayName;
        }

        return file.getName();
    }

    public File getFile() {
        return file;
    }

    public void setDisplayName(String displayName) {

        DisplayNameIndex dni = DisplayNameIndex.getInstance();
        dni.setDisplayName(file.getAbsolutePath(), displayName);
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
