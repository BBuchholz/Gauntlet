package com.nineworldsdeep.gauntlet.sqlite.model;

import com.nineworldsdeep.gauntlet.sqlite.NwdContract;

import java.util.Map;

/**
 * Created by brent on 6/14/16.
 */
public class FileTagModelItem {

    private String mFileId;
    private String mTagValue;

    public FileTagModelItem(Map<String, String> record) {

        if(record.containsKey(NwdContract.COLUMN_FILE_ID )){

            setFileId(record.get(NwdContract.COLUMN_FILE_ID ));
        }

        if(record.containsKey(NwdContract.COLUMN_TAG_VALUE )){

            setTagValue(record.get(NwdContract.COLUMN_TAG_VALUE ));
        }
    }

    public String getFileId() {
        return mFileId;
    }

    public void setFileId(String fileId) {
        this.mFileId = fileId;
    }

    public String getTagValue() {
        return mTagValue;
    }

    public void setTagValue(String mTagValue) {
        this.mTagValue = mTagValue;
    }
}
