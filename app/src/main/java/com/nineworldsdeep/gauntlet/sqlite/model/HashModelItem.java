package com.nineworldsdeep.gauntlet.sqlite.model;

/**
 * Created by brent on 6/15/16.
 */
public class HashModelItem {

     private String mFileId, mHash, mHashedAt;

     public HashModelItem(String fileId, String hash, String hashedAt) {

          this.mHash = hash;
          this.mHashedAt = hashedAt;
          this.mFileId = fileId;
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
