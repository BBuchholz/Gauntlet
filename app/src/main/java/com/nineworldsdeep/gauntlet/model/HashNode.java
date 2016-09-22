package com.nineworldsdeep.gauntlet.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

/**
 * Created by brent on 6/15/16.
 */
public class HashNode implements TapestryNode {

    private String mHash, mHashedAt;
    private ArrayList<FileNode> mFiles =
            new ArrayList<>();
    private ArrayList<TagNode> mTags =
            new ArrayList<>();

    public HashNode(String hash, String hashedAt) {

        this(null, hash, hashedAt);
    }

    public HashNode(FileNode file, String hashValue, String hashedAt) {

        addFile(file);
        setHash(hashValue);
        setHashedAt(hashedAt);
    }

    public ArrayList<FileNode> getFiles() {
          return this.mFiles;
     }

    public void addFile(FileNode file) {

        if(file != null && !mFiles.contains(file)){

            this.mFiles.add(file);
        }
     }

    public ArrayList<TagNode> getTags() {
          return this.mTags;
     }

    public void addTag(TagNode tag) {

        if(tag != null && !mTags.contains(tag))

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

        HashNode that = (HashNode) o;

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

    @Override
    public boolean supersedes(TapestryNode nd) {
        return false;
    }
}
