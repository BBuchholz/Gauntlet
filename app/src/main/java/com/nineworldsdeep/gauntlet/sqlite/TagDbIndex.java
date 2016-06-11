package com.nineworldsdeep.gauntlet.sqlite;

import com.nineworldsdeep.gauntlet.mnemosyne.DisplayNameIndexFile;
import com.nineworldsdeep.gauntlet.mnemosyne.FileListItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brent on 11/17/15.
 */
public class TagDbIndex {

    private static TagDbIndex instance;
    //private NwdDb db;

//    public static DisplayNameDbIndex getInstance() {
//
//        if(instance == null){
//
//            instance = new DisplayNameDbIndex();
//        }
//
//        return instance;
//    }

    private TagDbIndex(){
        //singleton pattern, private constructor

        //pathToName = new HashMap<>();
        //this.db = db;
        //loadToDbFromFile();
    }

    public static HashMap<String, String> getPathToNameMap(NwdDb db){
asdf
        //idempotent
        loadToDbFromFile(db);

        HashMap<String, String> output = new HashMap<>();

        List<Map<String, String>> records =
                db.getPathDisplayNamesForCurrentDevice();

        for(Map<String, String> map : records){

            String path = map.get(NwdContract.COLUMN_PATH_VALUE);
            String name = map.get(NwdContract.COLUMN_DISPLAY_NAME_VALUE);

            output.put(path, name);
        }

        //TODO: this is a hack (will combine db and file records into file)
        saveToFile(output);

        return output;
    }

    private static void saveToFile(HashMap<String, String> pathToNameMap) {
asdf
        //save a copy with the db files included
        DisplayNameIndexFile dnif = new DisplayNameIndexFile();

        for(Map.Entry<String,String> ent : pathToNameMap.entrySet()){

            dnif.addDisplayName(ent.getValue(), ent.getKey());
        }

        dnif.save();
    }


//    public boolean hasDisplayName(String filePath) {
//
//        return pathToName.containsKey(filePath);
//    }

    public String getDisplayName(String filePath, NwdDb db) {

        return db.getDisplayNameForPath(filePath);
    }

//    public void setDisplayName(String path, String displayName, NwdDb db) {
//
//        db.linkFileToDisplayName(path, displayName);
//    }

//    public void save() {
//
//        DisplayNameIndexFile dnif = new DisplayNameIndexFile();
//
//        for(Map.Entry<String,String> ent : getPathToNameMap().entrySet()){
//
//            dnif.addDisplayName(ent.getValue(), ent.getKey());
//        }
//
//        dnif.save();
//
//    }

    public static void loadToDbFromFile(NwdDb db){
asdf
        DisplayNameIndexFile dnif = new DisplayNameIndexFile();
        dnif.loadItems();
//
//        for(FileListItem ili : dnif.getFileListItems()){
//
//            setDisplayName(ili.getFile().getAbsolutePath(),
//                    ili.getDisplayName());
//        }
//
//        for(FileListItem ili : dnif.getFileListItems()){
//
//            setDisplayName(ili.getFile().getAbsolutePath(),
//                    ili.getDisplayName());
//        }

        setDisplayNames(dnif.getFileListItems(), db);
    }

    private static void setDisplayNames(List<FileListItem> fileListItems, NwdDb db) {
asdf
        db.linkFilesToDisplayNames(fileListItems);
    }

    public static void setDisplayNameAndExportFile(String path,
                                                   String displayName,
                                                   NwdDb db) {
asdf
        db.linkFileToDisplayName(path, displayName);
    }
}
