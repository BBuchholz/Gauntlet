package com.nineworldsdeep.gauntlet.xml.archivist;

import java.util.ArrayList;

/**
 * a stripped down version of ArchivistSource that only holds
 * what is available from XML import (to prevent attempts
 * to access values that wouldn't be populated if we
 * used ArchivistSource)
 */
public class ArchivistXmlSource {

    private String sourceType, author, director, title, year, url, retrievalDate, sourceTag;
    private ArrayList<ArchivistXmlLocationEntry> locationEntries;
    private ArrayList<ArchivistXmlSourceExcerpt> excerpts;

    public ArchivistXmlSource(){

        locationEntries = new ArrayList<>();
        excerpts = new ArrayList<>();

        //we don't want nulls (messes with db save)
        sourceType = "";
        author = "";
        director = "";
        title = "";
        year = "";
        url = "";
        retrievalDate = "";
        sourceTag = "";
    }

    public ArrayList<ArchivistXmlLocationEntry> getLocationEntries() {
        return locationEntries;
    }

    public ArrayList<ArchivistXmlSourceExcerpt> getExcerpts() {
        return excerpts;
    }

    public void add(ArchivistXmlLocationEntry axle){
        locationEntries.add(axle);
    }

    public void add(ArchivistXmlSourceExcerpt axse){
        excerpts.add(axse);
    }


    asdf; //all of these set methods need to check for null parameters
    //and set to "" if they are
    //go through entire archivist xml package and do this for all
    //model objects

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRetrievalDate() {
        return retrievalDate;
    }

    public void setRetrievalDate(String retrievalDate) {
        this.retrievalDate = retrievalDate;
    }

    public String getSourceTag() {
        return sourceTag;
    }

    public void setSourceTag(String sourceTag) {
        this.sourceTag = sourceTag;
    }
}
