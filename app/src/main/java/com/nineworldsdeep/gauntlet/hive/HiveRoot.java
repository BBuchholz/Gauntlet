package com.nineworldsdeep.gauntlet.hive;

import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.Date;

/**
 * Created by brent on 7/1/17.
 */

public class HiveRoot {

    private int hiveRootId;
    private String hiveRootName;
    private Date activatedAt;
    private Date deactivatedAt;

    public HiveRoot(int id, String name){
        setHiveRootId(id);
        setHiveRootName(name);
    }

    public Date getActivatedAt() {
        return activatedAt;
    }

    public Date getDeactivatedAt() {
        return deactivatedAt;
    }

    public int getHiveRootId() {
        return hiveRootId;
    }

    public void setHiveRootId(int hiveRootId) {
        this.hiveRootId = hiveRootId;
    }

    public String getHiveRootName() {
        return hiveRootName;
    }

    public void setHiveRootName(String hiveRootName) {
        this.hiveRootName = hiveRootName;
    }

    public boolean isActive() {

        if(deactivatedAt == null){
            return true;
        }

        if(activatedAt == null){
            //deactivatedAt is !null if we reach here
            return false;
        }

        return deactivatedAt.compareTo(activatedAt) < 0;
    }

        /**
     * will resolve conflicts, newest date will always take precedence
     * passing null values allowed as well to just set one or the other
     * null values always resolve to the non-null value (unless both null)
     * @param newActivatedAt
     * @param newDeactivatedAt
     */
    public void setTimeStamps(Date newActivatedAt, Date newDeactivatedAt){

        if(newActivatedAt != null){

            if(activatedAt == null || activatedAt.compareTo(newActivatedAt) < 0){
                //activatedAt is older or null
                activatedAt = newActivatedAt;
            }
        }

        if(newDeactivatedAt != null){

            if(deactivatedAt == null || deactivatedAt.compareTo(newDeactivatedAt) < 0){
                //deactivatedAt at is older
                deactivatedAt = newDeactivatedAt;
            }
        }

    }

    public void activate() {

        setTimeStamps(TimeStamp.now(), null);
    }

    public void deactivate() {

        setTimeStamps(null, TimeStamp.now());
    }
}
