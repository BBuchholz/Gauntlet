package com.nineworldsdeep.gauntlet.sqlite;

import com.nineworldsdeep.gauntlet.mnemosyne.DisplayNameIndexFile;
import com.nineworldsdeep.gauntlet.mnemosyne.FileListItem;

import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brent on 11/17/15.
 */
public class DisplayNameDbIndex {

    private static DisplayNameDbIndex instance;
    private NwdDb db;

    public static DisplayNameDbIndex getInstance(NwdDb db) {

        if(db == null){

            throw new IllegalArgumentException(
                    "null database passed to DisplayNameDbIndex.getInstance()");
        }

        if(instance == null || !instance.db.is(db)){

            instance = new DisplayNameDbIndex(db);
        }

        return instance;
    }

    private DisplayNameDbIndex(NwdDb db){
        //singleton pattern, private constructor

        //pathToName = new HashMap<>();
        this.db = db;
        load();
    }

    private HashMap<String, String> getPathToNameMap(){

        HashMap<String, String> output = new HashMap<>();

        List<Map<String, String>> records =
                db.getPathDisplayNamesForCurrentDevice();

        for(Map<String, String> map : records){

            output.put(map.get(NwdContract.COLUMN_PATH_VALUE),
                       map.get(NwdContract.COLUMN_DISPLAY_NAME_VALUE));
        }

        return output;
    }


//    public boolean hasDisplayName(String filePath) {
//
//        return pathToName.containsKey(filePath);
//    }

    public String getDisplayName(String filePath) {

        return db.getDisplayNameForPath(filePath);
    }

    public void setDisplayName(String path, String displayName) {

        db.linkFileToDisplayName(path, displayName);
    }

    public void save() {

        DisplayNameIndexFile dnif = new DisplayNameIndexFile();

        for(Map.Entry<String,String> ent : getPathToNameMap().entrySet()){

            dnif.addDisplayName(ent.getValue(), ent.getKey());
        }

        dnif.save();

    }

    public void load(){

        DisplayNameIndexFile dnif = new DisplayNameIndexFile();
        dnif.loadItems();
//
//        for(FileListItem ili : dnif.getImageListItems()){
//
//            setDisplayName(ili.getFile().getAbsolutePath(),
//                    ili.getDisplayName());
//        }

        for(FileListItem ili : dnif.getImageListItems()){}
    }
}
