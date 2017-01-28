package com.nineworldsdeep.gauntlet.mnemosyne.v5;

/**
 * Created by brent on 1/28/17.
 */
public class MediaDevice {

    private int mediaDeviceId = -1;
    private String mediaDeviceDescription = "";

    public int getMediaDeviceId() {
        return mediaDeviceId;
    }

    public void setMediaDeviceId(int mediaDeviceId) {
        this.mediaDeviceId = mediaDeviceId;
    }

    public String getMediaDeviceDescription() {
        return mediaDeviceDescription;
    }

    public void setMediaDeviceDescription(String mediaDeviceDescription) {
        this.mediaDeviceDescription = mediaDeviceDescription;
    }

    @Override
    public String toString(){
        return mediaDeviceDescription;
    }
}