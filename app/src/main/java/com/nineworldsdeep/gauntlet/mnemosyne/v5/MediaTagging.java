package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import com.nineworldsdeep.gauntlet.core.TaggingBase;
import com.nineworldsdeep.gauntlet.core.TimeStamp;

import java.util.Date;

/**
 * Created by brent on 3/5/17.
 */

public class MediaTagging extends TaggingBase{

    private int mediaTagId, mediaTaggingId, mediaId;
    private String mediaTagValue, mediaHash;

    //private Date taggedAt, untaggedAt;

    /**
     * just a convenience constructor.
     *
     * only calls setMediaTagValue(tagValue)
     */
    public MediaTagging(String tagValue) {

        setMediaTagValue(tagValue);
    }

    public MediaTagging(){
        //intentionally empty constructor
    }

//    public Date getVerifiedPresent() {
//        return taggedAt;
//    }
//
//    public Date getVerifiedMissing() {
//        return untaggedAt;
//    }

    public int getMediaTagId() {
        return mediaTagId;
    }

    public void setMediaTagId(int mediaTagId) {
        this.mediaTagId = mediaTagId;
    }

    private int getMediaTaggingId() {
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

    private String getMediaHash() {
        return mediaHash;
    }

    public void setMediaHash(String mediaHash) {
        this.mediaHash = mediaHash;
    }

//    /**
//     * will resolve conflicts, newest date will always take precedence
//     * passing null values allowed as well to just set one or the other
//     * null values always resolve to the non-null value (unless both null)
//     */
//    public void setTimeStamps(Date newTaggedAt, Date newUntaggedAt){
//
//        if(newTaggedAt != null){
//
//            if(taggedAt == null || taggedAt.compareTo(newTaggedAt) < 0){
//
//                //taggedAt is older or null
//                taggedAt = newTaggedAt;
//            }
//        }
//
//        if(newUntaggedAt != null){
//
//            if(untaggedAt == null || untaggedAt.compareTo(newUntaggedAt) < 0){
//
//                //untaggedAt is older
//                untaggedAt = newUntaggedAt;
//            }
//        }
//
//    }
//
//    public void tag(){
//
//        taggedAt = TimeStampV6.now();
//    }
//
//    public void untag(){
//
//        untaggedAt = TimeStampV6.now();
//    }
//
//    /**
//     *
//     * @return true if taggedAt value is greater than or
//     * equal to untaggedAt value
//     */
//    public boolean isTagged() {
//
//        if(untaggedAt != null && taggedAt != null) {
//
//            return untaggedAt.compareTo(taggedAt) < 0;
//        }
//
//        if(untaggedAt == null){
//
//            // taggedAt is either null or greater than,
//            // in either case, is considered "tagged"
//            return true;
//        }
//
//        //here, untagged is not null, and tagged is null
//        return false;
//    }

    public void merge(MediaTagging mt) throws Exception {

        setMediaTagValue(
            UtilsMnemosyneV5.tryMergeString(
                getMediaTagValue(), mt.getMediaTagValue()));

        setMediaHash(
            UtilsMnemosyneV5.tryMergeString(
                getMediaHash(), mt.getMediaHash()));

        setMediaTagId(
            UtilsMnemosyneV5.tryMergeInt(
                getMediaTagId(), mt.getMediaTagId()));

        setMediaTaggingId(
            UtilsMnemosyneV5.tryMergeInt(
                getMediaTaggingId(), mt.getMediaTaggingId()));

        setMediaId(
            UtilsMnemosyneV5.tryMergeInt(
                getMediaId(), mt.getMediaId()));

        setTimeStamps(mt.getTaggedAt(), mt.getUntaggedAt());
    }
}
