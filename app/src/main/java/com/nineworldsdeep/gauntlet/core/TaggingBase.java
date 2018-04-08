package com.nineworldsdeep.gauntlet.core;

import java.util.Date;

public class TaggingBase {

    private Date taggedAt, untaggedAt;


    public Date getTaggedAt() {
        return taggedAt;
    }

    public Date getUntaggedAt() {
        return untaggedAt;
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

                //untaggedAt is older
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

    /**
     *
     * @return true if taggedAt value is greater than or
     * equal to untaggedAt value
     */
    public boolean isTagged() {

        if(untaggedAt != null && taggedAt != null) {

            return untaggedAt.compareTo(taggedAt) < 0;
        }

        return untaggedAt == null;

        //here, untagged is not null, and tagged is null
    }
}
