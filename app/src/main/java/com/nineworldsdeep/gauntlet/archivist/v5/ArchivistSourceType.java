package com.nineworldsdeep.gauntlet.archivist.v5;

class ArchivistSourceType {

    private String sourceTypeName;
    private int sourcePicDrawableResourceId;
    private int sourceTypeId;

    ArchivistSourceType(int sourceTypeId, String sourceTypeName, int sourcePicDrawableResourceId){
        this.sourceTypeId = sourceTypeId;
        this.sourceTypeName = sourceTypeName;
        this.sourcePicDrawableResourceId = sourcePicDrawableResourceId;
    }

    String getSourceTypeName() {
        return sourceTypeName;
    }

    int getSourceTypeId() { return sourceTypeId; }

    int getSourcePicDrawableResourceId() {
        return sourcePicDrawableResourceId;
    }

}
