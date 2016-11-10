package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by brent on 12/1/15.
 * @deprecated use FileHashDbIndex instead
 */
@Deprecated
public class FileHashIndex {

//    private static FileHashIndex instance;
//
//    private HashMap<String, String> pathToHash;
//
//    public static FileHashIndex getInstance(){
//
//        if(instance == null){
//            instance = new FileHashIndex();
//        }
//
//        return instance;
//    }
//
//    private FileHashIndex(){
//
//        //singleton pattern, private constructor
//        pathToHash = new HashMap<>();
//        load();
//    }
//
////    public boolean hasStoredHash(String filePath){
////
////        return pathToHash.containsKey(filePath);
////    }
////
////    public String getStoredHash(String filePath){
////
////        return pathToHash.get(filePath);
////    }
//
//    public void storeHash(String path, String hash){
//
//        pathToHash.put(path, hash);
//    }
//
//    public void hashStoreAndSave(File file) throws Exception {
//
//        String path = file.getAbsolutePath();
//        storeHash(path, Utils.computeSHA1(path));
//        save();
//    }
//
//    public void save() {
//
//        FileHashIndexFile fhif = new FileHashIndexFile();
//
//        for(Map.Entry<String,String> ent : pathToHash.entrySet()){
//
//            fhif.addFileHash(ent.getKey(), ent.getValue());
//        }
//
//        fhif.save();
//    }
//
//    public void load(){
//
//        FileHashIndexFile fhif = new FileHashIndexFile();
//        fhif.load();
//
//        for(FileHashFragment fhf : fhif.getFileHashFragments()){
//
//            pathToHash.put(fhf.getPath(), fhf.getHash());
//        }
//
//    }
////    /**
////     * Computes and stores SHA1 hash for selected item if item is a file.
////     * If item is a directory, computes and stores hashes for
////     * all files within selected directory and all subfolders of the selected directory
////     * @param f
////     * @return count of files hashed and stored
////     */
////    public int countAndStoreSHA1Hashes(File f, int currentCount) throws Exception {
////
////        return countAndStoreSHA1Hashes(f, currentCount, true);
////
//////        if(f.exists()){
//////
//////            if(f.isDirectory()){
//////
//////                for (File f2 : f.listFiles()) {
//////
//////                    currentCount = countAndStoreSHA1Hashes(f2, currentCount);
//////
//////                }
//////
//////            }else if(f.isFile()){
//////
//////                //ignore if already indexed
//////                //TODO: add way to clear index
//////                //will ignore any files already in index, for efficiency
//////                //but need a way to explicity flush the index for old hashes
//////                //maybe timestamp them so an expiration date can be determined?
//////                if(!pathToHash.containsKey(f.getAbsolutePath())) {
//////
//////                    String hash = Utils.computeSHA1(f.getAbsolutePath());
//////                    storeHash(f.getAbsolutePath(), hash);
//////                    currentCount++;
//////                }
//////            }
//////
//////        }
//////
//////        return currentCount;
////
////    }
//
//    public int countAndStoreSHA1Hashes(File f,
//                                       int currentCount,
//                                       boolean ignorePreviouslyHashed)
//            throws Exception{
//
//        if(f.exists()){
//
//            if(f.isDirectory()){
//
//                for (File f2 : f.listFiles()) {
//
//                    currentCount =
//                            countAndStoreSHA1Hashes(f2,
//                                                    currentCount,
//                                                    ignorePreviouslyHashed);
//
//                }
//
//            }else if(f.isFile()){
//
//                //ignore if already indexed
//                //TODO: add way to clear index
//                //will ignore any files already in index, for efficiency
//                //but need a way to explicity flush the index for old hashes
//                //maybe timestamp them so an expiration date can be determined?
//                if(!ignorePreviouslyHashed ||
//                        !pathToHash.containsKey(f.getAbsolutePath())) {
//
//                    String hash = Utils.computeSHA1(f.getAbsolutePath());
//                    storeHash(f.getAbsolutePath(), hash);
//                    currentCount++;
//                }
//            }
//
//        }
//
//        return currentCount;
//    }
}
