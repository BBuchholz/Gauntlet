package com.nineworldsdeep.gauntlet.archivist.v5;

public class ArchivistSourceLocation {

    private int sourceLocationId;
    private String sourceLocationValue;

    public ArchivistSourceLocation(int sourceLocationId, String sourceLocationValue) {

        this.sourceLocationId = sourceLocationId;
        this.sourceLocationValue = sourceLocationValue;
    }

    public int getSourceLocationId() {
        return sourceLocationId;
    }

    public String getSourceLocationValue() {
        return sourceLocationValue;
    }

    @Override
    public String toString(){
        return getSourceLocationValue();
    }
}
