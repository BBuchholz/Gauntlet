package com.nineworldsdeep.gauntlet.sqlite.model;

/**
 * Created by brent on 6/15/16.
 */
public class HashModelItem {

     private String mFileId, mHash, mHashedAt;

     public HashModelItem(String hash, String hashedAt) {

         this(null, hash, hashedAt);
     }

    public HashModelItem(String fileId, String hashValue, String hashedAt) {

        setFileId(fileId);
        setHash(hashValue);
        setHashedAt(hashedAt);
    }

    public String getFileId() {
          return this.mFileId;
     }

     public void setFileId(String mFileId) {
          this.mFileId = mFileId;
     }

     public String getHash() {
          return this.mHash;
     }

     public void setHash(String hash) {
          this.mHash = hash;
     }

     public String getHashedAt() {
          return this.mHashedAt;
     }

     public void setHashedAt(String hashedAt) {
          this.mHashedAt = hashedAt;
     }
}
