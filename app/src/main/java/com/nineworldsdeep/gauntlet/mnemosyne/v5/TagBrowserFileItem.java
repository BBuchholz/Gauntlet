package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.support.annotation.NonNull;

import java.util.Objects;

public class TagBrowserFileItem implements Comparable<TagBrowserFileItem> {

    private String fileName;
    private String hash;
    private String tagString;

    public TagBrowserFileItem(String fileName){
        this.fileName = fileName;
        this.tagString = "tag string goes here";
    }

    public String getFilename() {
        return fileName;
    }
    public String getTagString() {return tagString; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagBrowserFileItem that = (TagBrowserFileItem) o;
        return Objects.equals(fileName, that.fileName) &&
                Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fileName, hash);
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public int compareTo(@NonNull TagBrowserFileItem tagBrowserFileItem) {

        return this.fileName.compareTo(tagBrowserFileItem.fileName);
    }
}
