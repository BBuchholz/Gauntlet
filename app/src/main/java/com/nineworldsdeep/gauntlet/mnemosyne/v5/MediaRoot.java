package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import java.io.File;

/**
 * Created by brent on 2/6/17.
 */

public class MediaRoot {

    private int mediaRootId, mediaDeviceId;
    private File mediaRootPath;

    public int getMediaRootId() {
        return mediaRootId;
    }

    public void setMediaRootId(int mediaRootId) {
        this.mediaRootId = mediaRootId;
    }

    public int getMediaDeviceId() {
        return mediaDeviceId;
    }

    public void setMediaDeviceId(int mediaDeviceId) {
        this.mediaDeviceId = mediaDeviceId;
    }

    public File getMediaRootPath() {
        return mediaRootPath;
    }

    public void setMediaRootPath(File mediaRootPath) {
        this.mediaRootPath = mediaRootPath;
    }

    @Override
    public String toString(){

        return getMediaRootPath().getAbsolutePath();
    }
}
