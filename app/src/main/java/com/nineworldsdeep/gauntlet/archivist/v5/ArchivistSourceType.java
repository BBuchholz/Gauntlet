package com.nineworldsdeep.gauntlet.archivist.v5;

import android.graphics.drawable.Drawable;

/**
 * Created by brent on 1/12/18.
 */

public class ArchivistSourceType {

    private String sourceTypeName;
    private int sourcePicDrawableResourceId;

    public ArchivistSourceType(String sourceTypeName, int sourcePicDrawableResourceId){
        this.sourceTypeName = sourceTypeName;
        this.sourcePicDrawableResourceId = sourcePicDrawableResourceId;
    }

    public String getSourceTypeName() {
        return sourceTypeName;
    }

    public void setSourceTypeName(String sourceTypeName) {
        this.sourceTypeName = sourceTypeName;
    }

    public int getSourcePicDrawableResourceId() {
        return sourcePicDrawableResourceId;
    }

    public void setSourcePicDrawableResourceId(int sourcePicDrawableResourceId) {
        this.sourcePicDrawableResourceId = sourcePicDrawableResourceId;
    }
}
