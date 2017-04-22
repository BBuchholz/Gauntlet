package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import com.nineworldsdeep.gauntlet.MultiMap;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;

public class Media {

    int mediaId;
    String mediaFileName, mediaDescription, mediaHash;
    ArrayList<MediaTagging> mediaTaggings;
    MultiMap<String, DevicePath> devicePaths;

    public Media(){

        mediaId = -1;

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
    public void add(DevicePath dp) throws Exception {

        //trying this, can remove x ? y : z code if it works
        if(Utils.stringIsNullOrWhitespace(dp.getDeviceName())){

            throw new Exception("cannot add device path to media if device name is not set");
        }

        String deviceName =
                dp.getDeviceName() == null ? "" : dp.getDeviceName();

        devicePaths.put(deviceName, dp);
    }

    public void add(MediaTagging mt) throws Exception {

        //mediaTaggings.add(mt);

        if(!Utils.stringIsNullOrWhitespace(mt.getMediaTagValue())){
            getTag(mt.getMediaTagValue()).merge(mt);
        }
    }

    /**
     * mediaFileName, mediaDescription, and mediaHash will default to non-empty, non-null value.
     * mediaId will default to value greater than zero.
     *
     * if values are set for both objects on any of the above properties,
     * and differ, an error will be thrown
     *
     * mediaTaggings will be Merged with any existing taggings
     *
     * devicePaths will simply be added, without regard to duplication
     * @param m
     */
    public void merge(Media m) throws Exception {

        setMediaFileName(
            UtilsMnemosyneV5.tryMergeString(
                getMediaFileName(), m.getMediaFileName()));

        setMediaDescription(
            UtilsMnemosyneV5.tryMergeString(
                getMediaDescription(), m.getMediaDescription()));

        setMediaHash(
            UtilsMnemosyneV5.tryMergeString(
                getMediaHash(), m.getMediaHash()));

        setMediaId(
            UtilsMnemosyneV5.tryMergeInt(
                getMediaId(), m.getMediaId()));

        for(MediaTagging mt : m.getMediaTaggings()){

            getTag(mt.getMediaTagValue()).merge(mt);
        }

        for(DevicePath dp : m.getDevicePaths().getAll()){

            add(dp);
        }
    }

    public boolean hasTag(String tag) {

        boolean found = false;

        for(MediaTagging mt : mediaTaggings){

            //tags are case sensitive
            if(mt.getMediaTagValue().equals(tag)){

                found = true;
                break;
            }
        }

        return found;
    }

    /**
     * will retrieve media device path with specified
     * device description and path value,
     * creating a new entry if it doesn't exist
     * @return
     */
    public DevicePath getDevicePath(String deviceDescription, String pathValue) throws Exception {

        if(devicePaths.containsKey(deviceDescription)) {

            for (DevicePath dp : devicePaths.get(deviceDescription)) {

                if (dp.getPath().equalsIgnoreCase(pathValue)) {

                    return dp;
                }
            }
        }

        DevicePath newDp = new DevicePath();
        newDp.setDeviceName(deviceDescription);
        newDp.setPath(pathValue);
        add(newDp);

        return newDp;
    }

    /**
     * will retrieve media tagging with specified tag value,
     * creating a new entry if it doesn't exist
     * @param tag
     * @return
     */
    public MediaTagging getTag(String tag){

        for(MediaTagging mt : mediaTaggings){

//            if(mt.getMediaTagValue().equalsIgnoreCase(tag)){
//
//                return mt;
//            }

            if(mt.getMediaTagValue().equals(tag)){

                return mt;
            }
        }

        MediaTagging newMt = new MediaTagging();
        mediaTaggings.add(newMt);
        newMt.setMediaTagValue(tag);

        return newMt;
    }

    public void untag(String tag) {

        getTag(tag).untag();
    }

    public void tag(String tag) {

        getTag(tag).tag();
    }

}
