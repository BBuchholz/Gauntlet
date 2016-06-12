package com.nineworldsdeep.gauntlet.mnemosyne;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brent on 12/1/15.
 * @deprecated use TagDbIndex instead
 */
@Deprecated
public class TagIndex {

//    private static TagIndex instance;
//
//    private HashMap<String, String> pathToTagString;
//
//    public static TagIndex getInstance(){
//
//        if(instance == null){
//            instance = new TagIndex();
//        }
//
//        return instance;
//    }
//
//    private TagIndex(){
//
//        //singleton pattern, private constructor
//        pathToTagString = new HashMap<>();
//        load();
//    }
//
//    public boolean hasTagString(String filePath){
//
//        return pathToTagString.containsKey(filePath);
//    }
//
//    public String getTagString(String filePath){
//
//        if(pathToTagString.containsKey(filePath)){
//
//            return pathToTagString.get(filePath);
//        }
//        return "";
//    }
//
//    public void setTagString(String path, String tags){
//
//        pathToTagString.put(path, tags);
//    }
//
//    public void save() {
//
//        TagIndexFile tif = new TagIndexFile();
//
//        for(Map.Entry<String,String> ent : pathToTagString.entrySet()){
//
//            tif.addTagString(ent.getKey(), ent.getValue());
//        }
//
//        tif.save();
//    }
//
//    public void load(){
//
//        TagIndexFile tif = new TagIndexFile();
//        tif.loadItems();
//
//        for(FileTagFragment ftf : tif.getFileTagFragments()){
//
//            pathToTagString.put(ftf.getPath(), ftf.getTags());
//        }
//
//    }
}
