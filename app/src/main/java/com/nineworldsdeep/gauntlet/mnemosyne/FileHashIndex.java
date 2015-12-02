package com.nineworldsdeep.gauntlet.mnemosyne;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brent on 12/1/15.
 */
public class FileHashIndex {

    private static FileHashIndex instance;

    private HashMap<String, String> pathToHash;

    public static FileHashIndex getInstance(){

        if(instance == null){
            instance = new FileHashIndex();
        }

        return instance;
    }

    private FileHashIndex(){

        //singleton pattern, private constructor
        pathToHash = new HashMap<>();
        load();
    }

    public boolean hasStoredHash(String filePath){

        return pathToHash.containsKey(filePath);
    }

    public String getStoredHash(String filePath){

        return pathToHash.get(filePath);
    }

    public void storeHash(String path, String hash){

        pathToHash.put(path, hash);
    }

    public void save() {

        FileHashIndexFile fhif = new FileHashIndexFile();

        for(Map.Entry<String,String> ent : pathToHash.entrySet()){

            fhif.addFileHash(ent.getKey(), ent.getValue());
        }

        fhif.save();
    }

    public void load(){

        FileHashIndexFile fhif = new FileHashIndexFile();
        fhif.loadItems();

        for(FileHashFragment fhf : fhif.getFileHashFragments()){

            pathToHash.put(fhf.getPath(), fhf.getHash());
        }

    }

}
