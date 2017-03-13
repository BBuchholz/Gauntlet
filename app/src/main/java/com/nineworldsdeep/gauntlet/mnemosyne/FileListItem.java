package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.DisplayNameDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.FileHashDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.HashMap;

public class FileListItem {

    private File file;
    private String displayName = null;
    private String tags = "";

    public FileListItem(String filePath, NwdDb db) {

        setPath(filePath, db); //handles everything
    }

    /**
     * ONLY USE THIS CONSTRUCTOR FOR ITEMS ALREADY IN THE DATABASE
     * it does not push any data to the db like the other methods do,
     * intended for populating large numbers of items from a single db query
     * @param filePath
     * @param pathToTagString - a map containing paths mapped to
     *                        their tagStrings, intended to be used to prevent
     *                        having to go back to the database more than once
     *                        when initializing large numbers of items at once
     */
    public FileListItem(String filePath, HashMap<String,String> pathToTagString){

        file = new File(filePath);

        if(pathToTagString != null &&
                pathToTagString.containsKey(filePath)){

            tags = pathToTagString.get(filePath);
        }

        displayName = FilenameUtils.getName(filePath);
    }

    private void processPath(NwdDb db){

        if(file != null) {

            this.displayName = processDisplayName(
                    DisplayNameDbIndex.getNameForPath(file.getAbsolutePath(),
                            db));

            this.tags =
                    TagDbIndex.getTagStringForPath(file.getAbsolutePath(), db);
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

    public void setPath(String filePath, NwdDb db){

        file = new File(filePath);
        processPath(db);
    }

    //TODO: make private, use set and sync
    public void setDisplayName(String displayName, NwdDb db) throws Exception {

        db.linkFileToDisplayName(file.getAbsolutePath(), displayName);
        this.displayName = displayName;

        FileHashDbIndex.countAndStoreSHA1Hashes(file, false, db);
    }

    public String getDisplayName() {
        return displayName;
    }

    private void setTagString(String tags, NwdDb db) throws Exception {

        TagDbIndex.setTagString(file.getAbsolutePath(), tags, db);

        this.tags = tags;

        //FileHashDbIndex.countAndStoreSHA1Hashes(file, false, db);
    }

    public void setAndSaveTagString(String tags, NwdDb db) throws Exception {

        setTagString(tags, db);
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
