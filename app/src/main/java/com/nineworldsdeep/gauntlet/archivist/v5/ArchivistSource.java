package com.nineworldsdeep.gauntlet.archivist.v5;

/**
 * Created by brent on 1/12/18.
 */

public class ArchivistSource {

    private String sourceTitle;
    private String sourceDescription; //just for dev scaffolding

    public ArchivistSource(String title, String desc){
        sourceTitle = title;
        sourceDescription = desc;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public void setSourceDescription(String sourceDescription) {
        this.sourceDescription = sourceDescription;
    }
}
