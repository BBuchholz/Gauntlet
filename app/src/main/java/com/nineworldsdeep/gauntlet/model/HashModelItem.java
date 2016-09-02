package com.nineworldsdeep.gauntlet.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

        if(file != null){

            this.mFiles.add(file);
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HashModelItem that = (HashModelItem) o;

        return new EqualsBuilder()
                .append(mHash, that.mHash)
                .append(mHashedAt, that.mHashedAt)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(mHash)
                .append(mHashedAt)
                .toHashCode();
    }
}
