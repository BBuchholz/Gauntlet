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

        if(tagValue == null){

            tagValue = "";
        }

        this.tagValue = tagValue;
    }

    public String getTaggedAt() {
        return taggedAt;
    }

    public void setTaggedAt(String taggedAt) {

        if(taggedAt == null){

            taggedAt = "";
        }

        this.taggedAt = taggedAt;
    }

    public String getUntaggedAt() {
        return untaggedAt;
    }

    public void setUntaggedAt(String untaggedAt) {

        if(untaggedAt == null){

            untaggedAt = "";
        }

        this.untaggedAt = untaggedAt;
    }
}
