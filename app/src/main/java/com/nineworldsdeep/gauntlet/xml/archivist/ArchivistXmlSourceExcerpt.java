package com.nineworldsdeep.gauntlet.xml.archivist;

import java.util.ArrayList;

public class ArchivistXmlSourceExcerpt {

    private String pages, beginTime, endTime, excerptValue;
    private ArrayList<ArchivistXmlTag> tags;


    public ArchivistXmlSourceExcerpt(){
        tags = new ArrayList<>();

        //we don't want nulls (messes with db save)
        pages = "";
        beginTime = "";
        endTime = "";
        excerptValue = "";
    }

    public void add(ArchivistXmlTag tag){
        tags.add(tag);
    }

    public ArrayList<ArchivistXmlTag> getTags() {
        return tags;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {

        if(pages == null){
            pages = "";
        }

        this.pages = pages;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {

        if(beginTime == null){
            beginTime = "";
        }

        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {

        if(endTime == null){
            endTime = "";
        }

        this.endTime = endTime;
    }

    public String getExcerptValue() {
        return excerptValue;
    }

    public void setExcerptValue(String excerptValue) {

        if(excerptValue == null){
            excerptValue = "";
        }

        this.excerptValue = excerptValue;
    }
}
