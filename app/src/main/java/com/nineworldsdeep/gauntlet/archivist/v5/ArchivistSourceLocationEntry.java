package com.nineworldsdeep.gauntlet.archivist.v5;

public class ArchivistSourceLocationEntry {

    private int sourceId, sourceLocationSubsetId;
    private String sourceLocationSubsetEntryName, sourceLocationValue, sourceLocationSubsetValue;

    public ArchivistSourceLocationEntry(int sourceId, int sourceLocationSubsetId, String sourceLocationSubsetEntryName) {

        this.sourceId = sourceId;
        this.sourceLocationSubsetId = sourceLocationSubsetId;
        this.sourceLocationSubsetEntryName = sourceLocationSubsetEntryName;
    }

    public ArchivistSourceLocationEntry(int sourceId,
                                        int sourceLocationSubsetId,
                                        String sourceLocationValue,
                                        String sourceLocationSubsetValue,
                                        String sourceLocationSubsetEntryName) {

        this(sourceId,sourceLocationSubsetId,sourceLocationSubsetEntryName);

        this.sourceLocationValue = sourceLocationValue;
        this.sourceLocationSubsetValue = sourceLocationSubsetValue;
    }

    public String getSourceLocationValue() {
        return sourceLocationValue;
    }

    public String getSourceLocationSubsetValue() {
        return sourceLocationSubsetValue;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getSourceLocationSubsetId() {
        return sourceLocationSubsetId;
    }

    public String getSourceLocationSubsetEntryName() {
        return sourceLocationSubsetEntryName;
    }

    public String getStatus() {
        return "ArchivistSourceLocationEntry status goes here";
    }
}
