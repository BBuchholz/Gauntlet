package com.nineworldsdeep.gauntlet.archivist.v5;

import android.text.TextUtils;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.TaggingBase;

import java.util.ArrayList;

/**
 * Created by brent on 1/12/18.
 */

public class ArchivistSourceExcerpt {

    private int excerptId;
    private int sourceId;
    private String excerptValue;
    private String excerptPages;
    private String excerptBeginTime;
    private String excerptEndTime;

    //mimic MediaTagging
    private ArrayList<ArchivistSourceExcerptTagging> excerptTaggings;


//    private String excerptDescription; //just for dev scaffolding
//    private String excerptContent;

//    public ArchivistSourceExcerpt(String desc, String content){
////        excerptDescription = desc;
////        excerptContent = content;
//
//
//    }


    public ArchivistSourceExcerpt(int excerptId,
                                  int sourceId,
                                  String excerptValue,
                                  String excerptPages,
                                  String excerptBeginTime,
                                  String excerptEndTime) {
        this.excerptId = excerptId;
        this.sourceId = sourceId;
        this.excerptValue = excerptValue;
        this.excerptPages = excerptPages;
        this.excerptBeginTime = excerptBeginTime;
        this.excerptEndTime = excerptEndTime;

        this.excerptTaggings = new ArrayList<>();
    }

    public String getLocation(){
        //return pages, begin to end time, whatever is most relevant

        String location = "location not specified";

        if(!Utils.stringIsNullOrWhitespace(getExcerptPages())){

            location = "pages: " + getExcerptPages();


        }else if(!Utils.stringIsNullOrWhitespace(getExcerptBeginTime())){

            location = "time: " + getExcerptBeginTime();

            if(!Utils.stringIsNullOrWhitespace(getExcerptEndTime())){

                location += " - " + getExcerptEndTime();
            }

        }

        return location;
    }

    public int getExcerptId() {
        return excerptId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public String getExcerptValue() {
        return excerptValue;
    }

    public String getExcerptPages() {
        return excerptPages;
    }

    public String getExcerptBeginTime() {
        return excerptBeginTime;
    }

    public String getExcerptEndTime() {
        return excerptEndTime;
    }

    public ArrayList<ArchivistSourceExcerptTagging> getExcerptTaggings() {
        return excerptTaggings;
    }

    public String getTagString(){

        ArrayList<String> tagValues = new ArrayList<>();

        for(ArchivistSourceExcerptTagging set : excerptTaggings){

            if(set.isTagged()){
                tagValues.add(set.getMediaTagValue());
            }
        }

        return TextUtils.join(", ", tagValues);
    }

    /**
     * will retrieve media tagging with specified tag value,
     * creating a new entry if it doesn't exist
     * @param tag
     * @return
     */
    public ArchivistSourceExcerptTagging getTag(String tag){

        for(ArchivistSourceExcerptTagging set : excerptTaggings){


            if(set.getMediaTagValue().equals(tag)){

                return set;
            }
        }

        ArchivistSourceExcerptTagging newSet = new ArchivistSourceExcerptTagging();
        excerptTaggings.add(newSet);
        newSet.setMediaTagValue(tag);

        return newSet;
    }

    public void untag(String tag) {

        getTag(tag).untag();
    }

    public void tag(String tag) {

        getTag(tag).tag();
    }

    private ArrayList<String> toTagList(String tags) {

        //be sure to trim, leave case sensitive though, we have
        //case sensitive duplicates in the db right now, need to
        //allow them until they can be phased out entirely

        ArrayList<String> tagList = new ArrayList<>();

        for(String tag : tags.split(",")){

            tagList.add(tag.trim());
        }

        return tagList;
    }

    public void setTagsFromTagString(String tagString){

        //get list of existing tags
        ArrayList<String> existingTags = toTagList(getTagString());

        //get list of tags from new tag string
        ArrayList<String> newTags = toTagList(tagString);

        //for each in existing tags, if new tags !contain, untag
        for(String tag : existingTags){

            if(!newTags.contains(tag)){

                getTag(tag).untag();
            }
        }

        //for each in new tags, if existing tags !contain, tag
        for(String tag : newTags){

            if(!existingTags.contains(tag)){

                getTag(tag).tag();
            }
        }

    }


//    public String getExcerptDescription() {
//        return "desc";
//    }
//
//    public void setExcerptDescription(String excerptDescription) {
//        //this.excerptDescription = excerptDescription;
//    }
//
//    public String getExcerptContent() {
//        return "content";
//    }
//
//    public void setExcerptContent(String excerptContent) {
//        //this.excerptContent = excerptContent;
//    }
}
