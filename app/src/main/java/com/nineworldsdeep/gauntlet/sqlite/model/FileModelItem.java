package com.nineworldsdeep.gauntlet.sqlite.model;

import com.nineworldsdeep.gauntlet.sqlite.NwdContract;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by brent on 6/14/16.
 */
public class FileModelItem {

    private String mId;
    private String mDevice, mDisplayName, mPath,
            mAudioTranscript, mDescription, mName;
    private ArrayList<String> mTags = new ArrayList<>();
    private ArrayList<HashModelItem> mHashes = new ArrayList<>();

    public FileModelItem(Map<String, String> record) {

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

        if(getId() != null && hash != null){

            getHashes().add(new HashModelItem(getId(), hash, hashedAt));
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

    public FileModelItem(String device, String path) {

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

    public ArrayList<String> getTags() {
        return mTags;
    }

    public ArrayList<HashModelItem> getHashes() {
        return mHashes;
    }
}