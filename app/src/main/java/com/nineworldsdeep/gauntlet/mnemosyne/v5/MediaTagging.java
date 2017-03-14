package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import com.nineworldsdeep.gauntlet.core.TimeStamp;

import java.util.Date;

/**
 * Created by brent on 3/5/17.
 */

public class MediaTagging {

    private int mediaTagId, mediaTaggingId, mediaId;
    private String mediaTagValue, mediaHash;
    private Date taggedAt, untaggedAt;

    /**
     * just a convenience constructor.
     *
     * only calls setMediaTagValue(tagValue)
     * @param tagValue
     */
    public MediaTagging(String tagValue) {

        setMediaTagValue(tagValue);
    }

    public MediaTagging(){
        //intentionally empty constructor
    }

    public Date getTaggedAt() {
        return taggedAt;
    }

    public Date getUntaggedAt() {
        return untaggedAt;
    }

    public int getMediaTagId() {
        return mediaTagId;
    }

    public void setMediaTagId(int mediaTagId) {
        this.mediaTagId = mediaTagId;
    }

    public int getMediaTaggingId() {
        return mediaTaggingId;
    }

    public void setMediaTaggingId(int mediaTaggingId) {
        this.mediaTaggingId = mediaTaggingId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaTagValue() {
        return mediaTagValue;
    }

    public void setMediaTagValue(String mediaTagValue) {
        this.mediaTagValue = mediaTagValue;
    }

    public String getMediaHash() {
        return mediaHash;
    }

    public void setMediaHash(String mediaHash) {
        this.mediaHash = mediaHash;
    }

    /**
     * will resolve conflicts, newest date will always take precedence
     * passing null values allowed as well to just set one or the other
     * null values always resolve to the non-null value (unless both null)
     */
    public void setTimeStamps(Date newTaggedAt, Date newUntaggedAt){

        if(newTaggedAt != null){

            if(taggedAt == null || taggedAt.compareTo(newTaggedAt) < 0){

                //taggedAt is older or null
                taggedAt = newTaggedAt;
            }
        }

        if(newUntaggedAt != null){

            if(untaggedAt == null || untaggedAt.compareTo(newUntaggedAt) < 0){

                //untaggedAt at is older
                untaggedAt = newUntaggedAt;
            }
        }

    }

    public void tag(){

        taggedAt = TimeStamp.now();
    }

    public void untag(){

        untaggedAt = TimeStamp.now();
    }

}
