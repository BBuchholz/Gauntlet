package com.nineworldsdeep.gauntlet.core;

import java.util.Date;

public class VerifiablePresenceBase {

    private Date verifiedPresent, verifiedMissing;


    public Date getVerifiedPresent() {
        return verifiedPresent;
    }

    public Date getVerifiedMissing() {
        return verifiedMissing;
    }

    /**
     * will resolve conflicts, newest date will always take precedence
     * passing null values allowed as well to just set one or the other
     * null values always resolve to the non-null value (unless both null)
     */
    public void setTimeStamps(Date newVerifiedPresent, Date newVerifiedMissing){

        if(newVerifiedPresent != null){

            if(verifiedPresent == null || verifiedPresent.compareTo(newVerifiedPresent) < 0){

                //verifiedPresent is older or null
                verifiedPresent = newVerifiedPresent;
            }
        }

        if(newVerifiedMissing != null){

            if(verifiedMissing == null || verifiedMissing.compareTo(newVerifiedMissing) < 0){

                //verifiedMissing is older
                verifiedMissing = newVerifiedMissing;
            }
        }

    }


    public void verifyPresent(){

        verifiedPresent = TimeStamp.now();
    }

    public void verifiyMissing(){

        verifiedMissing = TimeStamp.now();
    }

    /**
     *
     * @return true if verifiedPresent value is greater than or
     * equal to verifiedMissing value
     */
    public boolean isPresent() {

        if(verifiedMissing != null && verifiedPresent != null) {

            return verifiedMissing.compareTo(verifiedPresent) < 0;
        }

        return verifiedMissing == null;

    }
}
