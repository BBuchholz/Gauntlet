package com.nineworldsdeep.gauntlet.archivist.v5;

import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.core.VerifiablePresenceBase;

public class ArchivistSourceLocationEntry extends VerifiablePresenceBase {

    private int sourceId, sourceLocationSubsetId, sourceLocationSubsetEntryId;
    private String sourceLocationSubsetEntryName, sourceLocationValue, sourceLocationSubsetValue;

    public ArchivistSourceLocationEntry(int sourceId, int sourceLocationSubsetId, String sourceLocationSubsetEntryName) {

        this.sourceId = sourceId;
        this.sourceLocationSubsetId = sourceLocationSubsetId;
        this.sourceLocationSubsetEntryName = sourceLocationSubsetEntryName;
    }

    public ArchivistSourceLocationEntry(int sourceId,
                                        int sourceLocationSubsetId,
                                        int sourceLocationSubsetEntryId,
                                        String sourceLocationValue,
                                        String sourceLocationSubsetValue,
                                        String sourceLocationSubsetEntryName) {

        this(sourceId,sourceLocationSubsetId,sourceLocationSubsetEntryName);

        this.sourceLocationSubsetEntryId = sourceLocationSubsetEntryId;
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

        String status;

        if(isPresent()){

            status = "verified present: " +
                    TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(getVerifiedPresent());

        }else{

            status = "verified missing: " +
                    TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(getVerifiedMissing());
        }

        return status;
    }

    public int getSourceLocationSubsetEntryId() {
        return sourceLocationSubsetEntryId;
    }
}
