package com.nineworldsdeep.gauntlet.sqlite;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.mnemosyne.DisplayNameIndexFile;
import com.nineworldsdeep.gauntlet.mnemosyne.FileHashFragment;
import com.nineworldsdeep.gauntlet.mnemosyne.FileHashIndexFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brent on 11/17/15.
 */
public class FileHashDbIndex {

    public static HashMap<String, String> getPathToHashMap(boolean importFile,
                                                           boolean exportFile,
                                                           NwdDb db){

//        if(importFile) {
//
//            //idempotent
//            loadToDbFromFile(db);
//        }

        HashMap<String, String> output = new HashMap<>();

        List<Map<String, String>> records =
                db.getPathHashRecordsForCurrentDevice();

        for(Map<String, String> map : records){

            String path = map.get(NwdContract.COLUMN_PATH_VALUE);
            String name = map.get(NwdContract.COLUMN_HASH_VALUE);

            output.put(path, name);
        }

        if(exportFile) {

            saveToFile(output);
        }

        return output;
    }

    private static void saveToFile(HashMap<String, String> pathToHashMap) {

        //sync a copy with the db files included
        FileHashIndexFile fhif = new FileHashIndexFile();

        for(Map.Entry<String,String> ent : pathToHashMap.entrySet()){

            fhif.addFileHash(ent.getKey(), ent.getValue());
        }

        fhif.save();
    }

    public static void loadToDbFromFile(NwdDb db){

        DisplayNameIndexFile dnif = new DisplayNameIndexFile();
        dnif.loadItems();

        FileHashIndexFile fhif = new FileHashIndexFile();
        fhif.loadItems();

        setFileHashes(fhif.getFileHashFragments(), db);
    }

    private static void setFileHashes(List<FileHashFragment> fileHashFragments,
                                      NwdDb db) {

        db.linkFilesToHashes(fileHashFragments);
    }

    public static int countAndStoreSHA1Hashes(File f,
                                              boolean ignorePreviouslyHashed,
                                              NwdDb db)
        throws Exception{

        HashMap<String, String> pathToHashMap =
                getPathToHashMap(true, false, db);

        int count = countAndStoreSHA1Hashes(f, 0, ignorePreviouslyHashed, pathToHashMap);

        HashMap<String, String> filterPathToHashMap =
                new HashMap<>();

        for(String path : pathToHashMap.keySet()){

            File pathFile = new File(path);

            if(pathFile.exists()){

                filterPathToHashMap.put(path, pathToHashMap.get(path));
            }
        }

        db.linkFilesToHashes(filterPathToHashMap);

        saveToFile(filterPathToHashMap);

        return count;
    }

    public static int countAndStoreSHA1Hashes(File f,
                                              int currentCount,
                                              boolean ignorePreviouslyHashed,
                                              HashMap<String,String> pathToHash)
            throws Exception{

        if(f.exists()){

            if(f.isDirectory()){

                for (File f2 : f.listFiles()) {

                    currentCount =
                            countAndStoreSHA1Hashes(f2,
                                                    currentCount,
                                                    ignorePreviouslyHashed,
                                                    pathToHash);

                }

            }else if(f.isFile()){

                if(!ignorePreviouslyHashed ||
                        !pathToHash.containsKey(f.getAbsolutePath())) {

                    String hash = Utils.computeSHA1(f.getAbsolutePath());
                    pathToHash.put(f.getAbsolutePath(), hash);
                    currentCount++;
                }
            }

        }

        return currentCount;
    }

}
