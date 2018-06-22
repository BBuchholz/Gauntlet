package com.nineworldsdeep.gauntlet.archivist.v5;

import android.support.annotation.NonNull;

import com.nineworldsdeep.gauntlet.R;

public class ArchivistSourceType implements Comparable<ArchivistSourceType> {

    public static final String ALL_SOURCE_TYPES_NAME = "Misc Source";
    private String sourceTypeName;
    private int sourcePicDrawableResourceId;
    private int sourceTypeId;

    public ArchivistSourceType(int sourceTypeId, String sourceTypeName){
        this.sourceTypeId = sourceTypeId;
        this.sourceTypeName = sourceTypeName;
        this.sourcePicDrawableResourceId = inferDrawableResourceId(sourceTypeName);
    }

    private int inferDrawableResourceId(String sourceTypeName) {

        switch (sourceTypeName){

            case "Article":
                return R.drawable.article;
            case "Book":
                return R.drawable.book;
            case "Movie":
                return R.drawable.movie;
            case "Quote":
                return R.drawable.quote;
            case "Video":
                return R.drawable.video;
            case "Web":
                return R.drawable.web;
            default:
                return R.drawable.misc_source;
        }

    }

    public String getSourceTypeName() {
        return sourceTypeName;
    }

    public int getSourceTypeId() { return sourceTypeId; }

    int getSourcePicDrawableResourceId() {
        return sourcePicDrawableResourceId;
    }

    @Override
    public int compareTo(@NonNull ArchivistSourceType other) {
        return sourceTypeName.compareTo(other.sourceTypeName);
    }
}
