package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.synergy.v2.LineItemListFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brent on 12/1/15.
 */
public class FileHashIndexFile extends LineItemListFile {

    public FileHashIndexFile(){
        super("FileHashIndex",
                Configuration.getConfigDirectory());
    }

    public void addFileHash(String path, String hash){

        String entry = "path={" + path + "} ";
        entry += "sha1Hash={" + hash + "} ";

        add(entry);
    }

    public List<FileHashFragment> getFileHashFragments() {

        List<FileHashFragment> lst = new ArrayList<>();

        for(String item : getItems()){
            lst.add(new FileHashFragment(item));
        }

        return lst;
    }

    public HashMap<String, String> getPathToHashMap() {

        HashMap<String, String> pathToHash = new HashMap<>();

        for(FileHashFragment fhf : getFileHashFragments()){

            pathToHash.put(fhf.getPath(), fhf.getHash());
        }

        return pathToHash;
    }
}
