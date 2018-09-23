package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import java.util.Objects;

public class TagBrowserFileItem {

    private String fileName;

    public TagBrowserFileItem(String fileName){
        this.fileName = fileName;
    }

    public String getFilename() {
        return fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagBrowserFileItem that = (TagBrowserFileItem) o;
        return Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fileName);
    }
}
