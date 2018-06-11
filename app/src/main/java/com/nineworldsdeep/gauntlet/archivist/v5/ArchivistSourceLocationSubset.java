package com.nineworldsdeep.gauntlet.archivist.v5;

public class ArchivistSourceLocationSubset {

    private int sourceLocationsSubsetId, sourceLocationId;
    private String sourceLocationSubsetValue;

    public ArchivistSourceLocationSubset(int sourceLocationsSubsetId,
                                         int sourceLocationId,
                                         String sourceLocationSubsetValue) {

        this.sourceLocationsSubsetId = sourceLocationsSubsetId;
        this.sourceLocationId = sourceLocationId;
        this.sourceLocationSubsetValue = sourceLocationSubsetValue;
    }

    public int getSourceLocationsSubsetId() {
        return sourceLocationsSubsetId;
    }

    public int getSourceLocationId() {
        return sourceLocationId;
    }

    public String getSourceLocationSubsetValue() {
        return sourceLocationSubsetValue;
    }

    @Override
    public String toString(){
        return getSourceLocationSubsetValue();
    }
}
