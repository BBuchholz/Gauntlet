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

    public boolean isCompleted() {

        if(mCompletedAt == null){

            return false;

        }else if(mActivatedAt == null){

            return true;

        }else{

            return mCompletedAt.compareTo(mActivatedAt) > 0;
        }
    }

    public boolean isActive() {

        if(mActivatedAt == null &&
                (mCompletedAt != null ||
                 mArchivedAt != null)){

            //if active is null and either of the other two are non-null,
            //its not active
            return false;

        }else{

            boolean activeGreaterThanCompleted = true;
            boolean activeGreaterThanArchived = true;

            if(mCompletedAt != null){

                activeGreaterThanCompleted =
                    mActivatedAt.compareTo(mCompletedAt) >= 0;
            }

            if(mArchivedAt != null){

                activeGreaterThanArchived =
                    mActivatedAt.compareTo(mArchivedAt) >= 0;
            }

            return activeGreaterThanCompleted && activeGreaterThanArchived;
        }
    }

    public SynergyV5ToDo getCopy() {



        SynergyV5ToDo newCopy =
                new SynergyV5ToDo();

        Date newActivated = null;
        Date newCompleted = null;
        Date newArchived = null;

        if(mActivatedAt != null){

            newActivated = new Date(mActivatedAt.getTime());
        }

        if(mCompletedAt != null){

            newCompleted = new Date(mCompletedAt.getTime());
        }

        if(mArchivedAt != null){

            newArchived = new Date(mArchivedAt.getTime());
        }

        newCopy.setTimeStamps(newActivated, newCompleted, newArchived);

        return newCopy;
    }
}
