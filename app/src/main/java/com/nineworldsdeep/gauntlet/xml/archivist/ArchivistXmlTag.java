package com.nineworldsdeep.gauntlet.xml.archivist;

public class ArchivistXmlTag {

    private String tagValue, taggedAt, untaggedAt;

    public ArchivistXmlTag(){

        //we don't want nulls (messes with db save)
        tagValue = "";
        taggedAt = "";
        untaggedAt = "";
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getTaggedAt() {
        return taggedAt;
    }

    public void setTaggedAt(String taggedAt) {
        this.taggedAt = taggedAt;
    }

    public String getUntaggedAt() {
        return untaggedAt;
    }

    public void setUntaggedAt(String untaggedAt) {
        this.untaggedAt = untaggedAt;
    }
}
