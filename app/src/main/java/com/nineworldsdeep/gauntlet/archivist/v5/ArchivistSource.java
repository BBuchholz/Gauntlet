package com.nineworldsdeep.gauntlet.archivist.v5;

import com.nineworldsdeep.gauntlet.Utils;

public class ArchivistSource {

    private int sourceId;
    private int sourceTypeId;
    private String sourceTitle;
    private String sourceAuthor;
    private String sourceDirector;
    private String sourceYear;
    private String sourceUrl;
    private String sourceRetrievalDate;

    public ArchivistSource(
            int sourceId,
            int sourceTypeId,
            String sourceTitle,
            String sourceAuthor,
            String sourceDirector,
            String sourceYear,
            String sourceUrl,
            String sourceRetrievalDate){

        this.sourceId = sourceId;
        this.sourceTypeId = sourceTypeId;
        this.sourceTitle = sourceTitle;
        this.sourceAuthor = sourceAuthor;
        this.sourceDirector = sourceDirector;
        this.sourceYear = sourceYear;
        this.sourceUrl = sourceUrl;
        this.sourceRetrievalDate = sourceRetrievalDate;
    }

    public int getSourceTypeId() {
        return sourceTypeId;
    }

    public void setSourceTypeId(int sourceTypeId) {
        this.sourceTypeId = sourceTypeId;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public String getSourceAuthor() {
        return sourceAuthor;
    }

    public void setSourceAuthor(String sourceAuthor) {
        this.sourceAuthor = sourceAuthor;
    }

    public String getSourceDirector() {
        return sourceDirector;
    }

    public void setSourceDirector(String sourceDirector) {
        this.sourceDirector = sourceDirector;
    }

    public String getSourceYear() {
        return sourceYear;
    }

    public void setSourceYear(String sourceYear) {
        this.sourceYear = sourceYear;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceRetrievalDate() {
        return sourceRetrievalDate;
    }

    public void setSourceRetrievalDate(String sourceRetrievalDate) {
        this.sourceRetrievalDate = sourceRetrievalDate;
    }

    String getShortDescription() {

        if(!Utils.stringIsNullOrWhitespace(sourceTitle)){

            return sourceTitle;

        }else if(!Utils.stringIsNullOrWhitespace(sourceAuthor)){

            return sourceAuthor;

        }else if(!Utils.stringIsNullOrWhitespace(sourceDirector)){

            return sourceDirector;

        }else if(!Utils.stringIsNullOrWhitespace(sourceUrl)){

            return sourceUrl;

        }else{

            return "[UNKNOWN SOURCE]";
        }
    }
}
