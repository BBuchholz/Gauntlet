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

            }else{

                displayName = file.getName();
            }

            TagIndex ti = TagIndex.getInstance();

            if (ti.hasTagString(file.getAbsolutePath())) {

                tags = ti.getTagString(file.getAbsolutePath());

            }else{

                tags = "";
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

    //TODO: make private, use set and save
    public void setDisplayName(String displayName) throws Exception {

        DisplayNameIndex dni = DisplayNameIndex.getInstance();
        dni.setDisplayName(file.getAbsolutePath(), displayName);
        this.displayName = displayName;
        FileHashIndex fhi = FileHashIndex.getInstance();
        fhi.hashStoreAndSave(file);
    }

    public String getDisplayName() {
        return displayName;
    }

    //TODO: make private, use set and save
    public void setTagString(String tags) throws Exception {
        TagIndex ti = TagIndex.getInstance();
        ti.setTagString(file.getAbsolutePath(), tags);
        this.tags = tags;
        FileHashIndex fhi = FileHashIndex.getInstance();
        fhi.hashStoreAndSave(file);
    }

    public void setAndSaveTagString(String tags) throws Exception {
        setTagString(tags);
        TagIndex.getInstance().save();
    }

    public void setAndSaveDisplayName(String displayName) throws Exception {
        setDisplayName(displayName);
        DisplayNameIndex.getInstance().save();
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
