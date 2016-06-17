package com.nineworldsdeep.gauntlet.sqlite.model;

import com.nineworldsdeep.gauntlet.sqlite.NwdContract;

import org.w3c.dom.Element;

import java.util.Map;

/**
 * Created by brent on 6/14/16.
 */
public class LocalConfigModelItem {

    private String mKey, mValue;

    public LocalConfigModelItem(Map<String, String> record) {

        if(record.containsKey(NwdContract.COLUMN_LOCAL_CONFIG_KEY)){

            setKey(record.get(NwdContract.COLUMN_LOCAL_CONFIG_KEY));
        }

        if(record.containsKey(NwdContract.COLUMN_LOCAL_CONFIG_VALUE)){

            setValue(record.get(NwdContract.COLUMN_LOCAL_CONFIG_VALUE));
        }
    }

    public LocalConfigModelItem(String key, String value) {

        setKey(key);
        setValue(value);
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String mValue) {
        this.mValue = mValue;
    }
}
