package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.DisplayNameDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.io.File;
import java.util.HashMap;

/**
 * Created by brent on 11/17/15.
 */
public class FileListItem {

    private File file;
    private String displayName = null;
    private String tags = "";

    public FileListItem(String filePath, HashMap<String,String> dbPathToNameMap) {

//        file = new File(filePath);
//
//        processPath();

        setPath(filePath, dbPathToNameMap); //handles everything
    }

    private void processPath(HashMap<String,String> dbPathToNameMap){

        if(file != null) {

            //DisplayNameDbIndex dni = DisplayNameDbIndex.getInstance(db);

            String indexedName = dbPathToNameMap.get(file.getAbsolutePath()); //dni.getDisplayName(file.getAbsolutePath());

//            if (!Utils.stringIsNullOrWhitespace(indexedName)) {
//
//                displayName = indexedName;
//
//            }else{
//
//                displayName = file.getName();
//            }

            displayName = processDisplayName(indexedName);

            TagIndex ti = TagIndex.getInstance();

            if (ti.hasTagString(file.getAbsolutePath())) {

                tags = ti.getTagString(file.getAbsolutePath());

            }else{

                tags = "";
            }
        }
    }

    private String processDisplayName(String name) {

        if (!Utils.stringIsNullOrWhitespace(name)) {

            return name;

        }else{

            return file.getName();
        }
    }

    public FileListItem(String path, String displayName) {

        this.file = new File(path);
        this.displayName = processDisplayName(displayName);
    }

    @Override
    public String toString(){

        return getDefaultName();
    }

    public File getFile() {
        return file;
    }

    public void setPath(String filePath, HashMap<String,String> dbPathToNameMap){

        file = new File(filePath);
        processPath(dbPathToNameMap);
    }

    //TODO: make private, use set and save
    public void setDisplayName(String displayName, NwdDb db) throws Exception {

        //DisplayNameDbIndex dni = DisplayNameDbIndex.getInstance(db);
        //dni.setDisplayName(file.getAbsolutePath(), displayName);
        db.linkFileToDisplayName(file.getAbsolutePath(), displayName);
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
//
//    public void setAndSaveDisplayName(String displayName, NwdDb db) throws Exception {
//        setDisplayName(displayName, db);
//        DisplayNameDbIndex.getInstance().save();
//    }

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
