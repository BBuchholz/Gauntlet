package com.nineworldsdeep.gauntlet.synergy.v5;

import java.util.Date;

/**
 * Created by brent on 12/7/16.
 */
public class SynergyV5ToDo {

    private int mToDoId;
    private Date mActivatedAt, mCompletedAt, mArchivedAt;

    public int getToDoId() {
        return mToDoId;
    }

    public void setToDoId(int toDoId) {
        this.mToDoId = toDoId;
    }

    public Date getActivatedAt() {
        return mActivatedAt;
    }

    public Date getCompletedAt() {
        return mCompletedAt;
    }

    public Date getArchivedAt() {
        return mArchivedAt;
    }

    /**
     * will resolve conflicts, newest date will always take precedence
     * passing null values allowed as well to just set one or the other
     * null values always resolve to the non-null value (unless both null)
     */
    public void setTimeStamps(Date activatedAt,
                              Date completedAt,
                              Date archivedAt){

        if(activatedAt != null){

            if(mActivatedAt == null || mActivatedAt.compareTo(activatedAt) < 0){
                //mActivated is older or null
                mActivatedAt = activatedAt;
            }
        }

        if(completedAt != null){

            if(mCompletedAt == null || mCompletedAt.compareTo(completedAt) < 0){
                //mCompletedAt at is older
                mCompletedAt = completedAt;
            }
        }

        if(archivedAt != null){

            if(mArchivedAt == null || mArchivedAt.compareTo(archivedAt) < 0){
                //mArchivedAt at is older
                mArchivedAt = archivedAt;
            }
        }

    }
}
