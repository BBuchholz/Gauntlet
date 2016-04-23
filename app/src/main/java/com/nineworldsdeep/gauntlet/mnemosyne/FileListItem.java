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

//        file = new File(filePath);
//
//        ProcessPath();

        setPath(filePath); //handles everything
    }

    private void ProcessPath(){

        if(file != null) {

            DisplayNameIndex dni = DisplayNameIndex.getInstance();

            if (dni.hasDisplayName(file.getAbsolutePath())) {

                displayName = dni.getDisplayName(file.getAbsolutePath());
            }

            TagIndex ti = TagIndex.getInstance();

            if (ti.hasTagString(file.getAbsolutePath())) {

                tags = ti.getTagString(file.getAbsolutePath());
            }
        }
    }

    public FileListItem(String path, String displayName) {

        this.file = new File(path);
        this.displayName = displayName;
    }

    @Override
    public String toString(){

        return getDefaultName();
    }

    public File getFile() {
        return file;
    }

    public void setPath(String filePath){

        file = new File(filePath);
        ProcessPath();
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

    public void setAndSaveTagString(String tags){
        setTagString(tags);
        TagIndex.getInstance().save();
    }

    public String getTags(){
        return tags;
    }

    public String getDefaultName() {

        if(!Utils.stringIsNullOrWhitespace(displayName)){
            return displayName;
        }

        if(!Utils.stringIsNullOrWhitespace(tags)){
            return tags;
        }

        return file.getName();
    }
}
