package com.nineworldsdeep.gauntlet.model;

import com.nineworldsdeep.gauntlet.sqlite.NwdContract;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by brent on 6/14/16.
 */
public class FileNode implements TapestryNode {

    private String mId;
    private String mDevice, mDisplayName, mPath,
            mAudioTranscript, mDescription, mName;
    private ArrayList<TagNode> mTags = new ArrayList<>();
    private ArrayList<HashNode> mHashes = new ArrayList<>();

    //TODO: THIS NEEDS TO MOVE TO THE DATA MAPPER (see DATA MAPPER pattern)
    public FileNode(Map<String, String> record) {

        if(record.containsKey(NwdContract.COLUMN_FILE_ID )){

            setId(record.get(NwdContract.COLUMN_FILE_ID ));
        }

        if(record.containsKey(NwdContract.COLUMN_DEVICE_DESCRIPTION )){

            setDevice(record.get(NwdContract.COLUMN_DEVICE_DESCRIPTION ));
        }

        if(record.containsKey(NwdContract.COLUMN_PATH_VALUE )){

            setPath(record.get(NwdContract.COLUMN_PATH_VALUE ));
        }

        if(record.containsKey(NwdContract.COLUMN_DISPLAY_NAME_VALUE )){

            setDisplayName(record.get(NwdContract.COLUMN_DISPLAY_NAME_VALUE ));
        }

        String hash = null;
        String hashedAt = null;

        if(record.containsKey(NwdContract.COLUMN_HASH_VALUE )){

            hash = record.get(NwdContract.COLUMN_HASH_VALUE );
        }

        if(record.containsKey(NwdContract.COLUMN_FILE_HASHED_AT )){

            hashedAt = record.get(NwdContract.COLUMN_FILE_HASHED_AT );
        }

        if(hash != null){

            add(new HashNode(this, hash, hashedAt));
        }

        if(record.containsKey(NwdContract.COLUMN_FILE_DESCRIPTION )){

            setDescription(record.get(NwdContract.COLUMN_FILE_DESCRIPTION ));
        }

        if(record.containsKey(NwdContract.COLUMN_FILE_NAME)) {

            setName(record.get(NwdContract.COLUMN_FILE_NAME));
        }

        if(record.containsKey(NwdContract.COLUMN_AUDIO_TRANSCRIPT_VALUE)) {

            setAudioTranscript(record.get(NwdContract.COLUMN_AUDIO_TRANSCRIPT_VALUE));
        }
    }

    public FileNode(String device, String path) {

        setDevice(device);
        setPath(path);
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getDevice() {
        return mDevice;
    }

    public void setDevice(String mDevice) {
        this.mDevice = mDevice;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }

    public String getAudioTranscript() {
        return mAudioTranscript;
    }

    public void setAudioTranscript(String mAudioTranscript) {
        this.mAudioTranscript = mAudioTranscript;
    }

    public Iterator<TagNode> getTags() {
        return mTags.iterator();
    }

    public void add(TagNode tmi){

        if(!mTags.contains(tmi)){

            mTags.add(tmi);
        }
    }

    public Iterator<HashNode> getHashes() {
        return mHashes.iterator();
    }

    public void add(HashNode hmi){

        if(!mHashes.contains(hmi)){

            mHashes.add(hmi);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FileNode that = (FileNode) o;

        return new EqualsBuilder()
                .append(mDevice, that.mDevice)
                .append(mPath, that.mPath)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(mDevice)
                .append(mPath)
                .toHashCode();
    }

    public int hashCount() {

        return mHashes.size();
    }

    public int tagCount() {

        return mTags.size();
    }

    @Override
    public boolean supersedes(TapestryNode nd) {
        return false;
    }
}
