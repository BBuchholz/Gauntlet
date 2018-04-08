package com.nineworldsdeep.gauntlet.archivist.v5;

import android.text.TextUtils;

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


    ArchivistSourceExcerpt(int excerptId,
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
        return "Location Goes Here";
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

    public String getTagString(){

        ArrayList<String> tagValues = new ArrayList<>();

        for(ArchivistSourceExcerptTagging set : excerptTaggings){

            if(set.isTagged()){
                tagValues.add(set.getMediaTagValue());
            }
        }

        return TextUtils.join(", ", tagValues);
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
