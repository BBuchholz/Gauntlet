package com.nineworldsdeep.gauntlet.mnemosyne;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brent on 11/17/15.
 * @deprecated use DisplayNameDbIndex instead
 */
@Deprecated
public class DisplayNameIndex {

//    private static DisplayNameIndex instance;
//
//    private HashMap<String, String> pathToName;
//
//    public static DisplayNameIndex getInstance() {
//
//        if(instance == null){
//            instance = new DisplayNameIndex();
//        }
//
//        return instance;
//    }
//
//    private DisplayNameIndex(){
//        //singleton pattern, private constructor
//
//        pathToName = new HashMap<>();
//        load();
//    }
//
////    public boolean hasDisplayName(String filePath) {
////
////        return pathToName.containsKey(filePath);
////    }
//
//    public String getDisplayName(String filePath) {
//
//        if (!pathToName.containsKey(filePath)){
//
//            return "";
//        }
//
//        return pathToName.get(filePath);
//    }
//
//    public void setDisplayName(String path, String displayName) {
//
//        pathToName.put(path, displayName);
//    }
//
//    public void save() {
//
//        DisplayNameIndexFile dnif = new DisplayNameIndexFile();
//
//        for(Map.Entry<String,String> ent : pathToName.entrySet()){
//
//            dnif.addDisplayName(ent.getValue(), ent.getKey());
//        }
//
//        dnif.save();
//
//    }
//
//    public void load(){
//
//        DisplayNameIndexFile dnif = new DisplayNameIndexFile();
//        dnif.load();
//
//        for(FileListItem ili : dnif.getFileListItems()){
//
//            pathToName.put(ili.getFile().getAbsolutePath(), ili.getDisplayName());
//        }
//    }
}
