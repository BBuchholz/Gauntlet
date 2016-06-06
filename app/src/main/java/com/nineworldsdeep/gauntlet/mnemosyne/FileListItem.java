package com.nineworldsdeep.gauntlet.mnemosyne;

import android.text.Editable;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.DisplayNameDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.io.File;

/**
 * Created by brent on 11/17/15.
 */
public class FileListItem {

    private File file;
    private String displayName = null;
    private String tags = "";

    public FileListItem(String filePath, NwdDb db) {

//        file = new File(filePath);
//
//        ProcessPath();

        setPath(filePath, db); //handles everything
    }

    private void ProcessPath(NwdDb db){

        if(file != null) {

            DisplayNameDbIndex dni = DisplayNameDbIndex.getInstance(db);

            String indexedName = dni.getDisplayName(file.getAbsolutePath());

            if (!Utils.stringIsNullOrWhitespace(indexedName)) {

                displayName = indexedName;

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

    public void setPath(String filePath, NwdDb db){

        file = new File(filePath);
        ProcessPath(db);
    }

    //TODO: make private, use set and save
    public void setDisplayName(String displayName, NwdDb db) throws Exception {

        DisplayNameDbIndex dni = DisplayNameDbIndex.getInstance(db);
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

    public void setAndSaveDisplayName(String displayName, NwdDb db) throws Exception {
        setDisplayName(displayName, db);
        DisplayNameDbIndex.getInstance(db).save();
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
