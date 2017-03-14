package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import com.nineworldsdeep.gauntlet.MultiMap;

import java.util.ArrayList;

public class Media {

    int mediaId;
    String mediaFileName, mediaDescription, mediaHash;
    ArrayList<MediaTagging> mediaTaggings;
    MultiMap<String, DevicePath> devicePaths;

    public Media(){

        mediaTaggings = new ArrayList<>();
        devicePaths = new MultiMap<>();
    }

    public ArrayList<MediaTagging> getMediaTaggings() {
        return mediaTaggings;
    }

    public MultiMap<String, DevicePath> getDevicePaths() {
        return devicePaths;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaFileName() {
        return mediaFileName;
    }

    public void setMediaFileName(String mediaFileName) {
        this.mediaFileName = mediaFileName;
    }

    public String getMediaDescription() {
        return mediaDescription;
    }

    public void setMediaDescription(String mediaDescription) {
        this.mediaDescription = mediaDescription;
    }

    public String getMediaHash() {
        return mediaHash;
    }

    public void setMediaHash(String mediaHash) {
        this.mediaHash = mediaHash;
    }

    /**
     * uses DevicePath.getDeviceName() to add to MultiMap,
     * key/DeviceName will be "" if not set in DevicePath
     * @param dp
     */
    public void add(DevicePath dp) {

        String deviceName =
                dp.getDeviceName() == null ? "" : dp.getDeviceName();

        devicePaths.put(deviceName, dp);
    }

    public void add(MediaTagging mt) {

        mediaTaggings.add(mt);
    }
}
