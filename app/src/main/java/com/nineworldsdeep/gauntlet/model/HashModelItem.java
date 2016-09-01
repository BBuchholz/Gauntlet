package com.nineworldsdeep.gauntlet.model;

import java.util.ArrayList;

/**
 * Created by brent on 6/15/16.
 */
public class HashModelItem {

    private String mHash, mHashedAt;
    private ArrayList<FileModelItem> mFiles =
            new ArrayList<>();
    private ArrayList<TagModelItem> mTags =
            new ArrayList<>();

    public HashModelItem(String hash, String hashedAt) {

        this(null, hash, hashedAt);
    }

    public HashModelItem(FileModelItem file, String hashValue, String hashedAt) {

        addFile(file);
        setHash(hashValue);
        setHashedAt(hashedAt);
    }

    public ArrayList<FileModelItem> getFiles() {
          return this.mFiles;
     }

    public void addFile(FileModelItem file) {
          this.mFiles.add(file);
     }

    public ArrayList<TagModelItem> getTags() {
          return this.mTags;
     }

    public void addTag(TagModelItem tag) {
          this.mTags.add(tag);
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
