package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import java.util.Date;

/**
 * Created by brent on 3/5/17.
 */

public class DevicePath {

    private int devicePathId, mediaId, mediaDeviceId, mediaPathId;
    private String path, deviceName;
    private Date verifiedPresent, verifiedMissing;

    /**
     * just a convenience constructor.
     *
     * only calls setPath(filePath) and
     * makes no assumptions about mediaDeviceId, &c.
     * @param filePath
     */
    public DevicePath(String filePath) {

        setPath(filePath);
    }

    public Date getVerifiedPresent() {
        return verifiedPresent;
    }

    public Date getVerifiedMissing() {
        return verifiedMissing;
    }

    public int getDevicePathId() {
        return devicePathId;
    }

    public void setDevicePathId(int devicePathId) {
        this.devicePathId = devicePathId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getMediaDeviceId() {
        return mediaDeviceId;
    }

    public void setMediaDeviceId(int mediaDeviceId) {
        this.mediaDeviceId = mediaDeviceId;
    }

    public int getMediaPathId() {
        return mediaPathId;
    }

    public void setMediaPathId(int mediaPathId) {
        this.mediaPathId = mediaPathId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * will resolve conflicts, newest date will always take precedence
     * passing null values allowed as well to just set one or the other
     * null values always resolve to the non-null value (unless both null)
     */
    public void setTimeStamps(
            Date newVerifiedPresent, Date newVerifiedMissing){

        if(newVerifiedPresent != null){

            if(verifiedPresent == null ||
                    verifiedPresent.compareTo(newVerifiedPresent) < 0){

                //verifiedPresent is older or null
                verifiedPresent = newVerifiedPresent;
            }
        }

        if(newVerifiedMissing != null){

            if(verifiedMissing == null ||
                    verifiedMissing.compareTo(newVerifiedMissing) < 0){

                //verifiedMissing at is older
                verifiedMissing = newVerifiedMissing;
            }
        }

    }
}
