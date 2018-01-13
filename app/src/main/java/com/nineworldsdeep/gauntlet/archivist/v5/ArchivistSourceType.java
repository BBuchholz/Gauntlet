package com.nineworldsdeep.gauntlet.archivist.v5;

/**
 * Created by brent on 1/12/18.
 */

public class ArchivistSourceType {

    private String sourceTypeName;

    public ArchivistSourceType(String name){
        sourceTypeName = name;
    }

    public String getSourceTypeName() {
        return sourceTypeName;
    }

    public void setSourceTypeName(String sourceTypeName) {
        this.sourceTypeName = sourceTypeName;
    }
}
