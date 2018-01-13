package com.nineworldsdeep.gauntlet.archivist.v5;

/**
 * Created by brent on 1/12/18.
 */

public class ArchivistSourceExcerpt {

    private String excerptDescription; //just for dev scaffolding
    private String excerptContent;

    public ArchivistSourceExcerpt(String desc, String content){
        excerptDescription = desc;
        excerptContent = content;
    }

    public String getExcerptDescription() {
        return excerptDescription;
    }

    public void setExcerptDescription(String excerptDescription) {
        this.excerptDescription = excerptDescription;
    }

    public String getExcerptContent() {
        return excerptContent;
    }

    public void setExcerptContent(String excerptContent) {
        this.excerptContent = excerptContent;
    }
}
