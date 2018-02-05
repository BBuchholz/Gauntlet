package com.nineworldsdeep.gauntlet.archivist.v5;

import android.graphics.drawable.Drawable;

/**
 * Created by brent on 1/12/18.
 */

public class ArchivistSourceType {

    private String sourceTypeName;
    private Drawable sourcePicDrawable;

    public ArchivistSourceType(String sourceTypeName, Drawable sourcePicDrawable){
        this.sourceTypeName = sourceTypeName;
        this.sourcePicDrawable = sourcePicDrawable;
    }

    public String getSourceTypeName() {
        return sourceTypeName;
    }

    public void setSourceTypeName(String sourceTypeName) {
        this.sourceTypeName = sourceTypeName;
    }

    public Drawable getSourcePicDrawable() {
        return sourcePicDrawable;
    }

    public void setSourcePicDrawable(Drawable sourcePicDrawable) {
        this.sourcePicDrawable = sourcePicDrawable;
    }
}
