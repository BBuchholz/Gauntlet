package com.nineworldsdeep.gauntlet.mnemosyne;

import android.text.Editable;

import com.nineworldsdeep.gauntlet.Utils;

import java.io.File;

/**
 * Created by brent on 11/17/15.
 */
public class FileListItem {

    private File file;
    private String displayName = null;
    private String tags = "";

    public FileListItem(String filePath) {

        file = new File(filePath);

        DisplayNameIndex dni = DisplayNameIndex.getInstance();

        if(dni.hasDisplayName(filePath)){

            displayName = dni.getDisplayName(filePath);
        }

        TagIndex ti = TagIndex.getInstance();

        if(ti.hasTagString(filePath)){

            tags = ti.getTagString(filePath);
        }
    }

    public FileListItem(String path, String displayName) {

        this.file = new File(path);
        this.displayName = displayName;
    }

    @Override
    public String toString(){

        if(!Utils.stringIsNullOrWhitespace(displayName)){
            return displayName;
        }

        if(!Utils.stringIsNullOrWhitespace(tags)){
            return tags;
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

    public void setTagString(String tags) {
        TagIndex ti = TagIndex.getInstance();
        ti.setTagString(file.getAbsolutePath(), tags);
        this.tags = tags;
    }

    public String getTags(){
        return tags;
    }
}
