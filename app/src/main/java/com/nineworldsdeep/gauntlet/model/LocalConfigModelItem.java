package com.nineworldsdeep.gauntlet.model;

import com.nineworldsdeep.gauntlet.sqlite.NwdContract;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LocalConfigModelItem that = (LocalConfigModelItem) o;

        return new EqualsBuilder()
                .append(mKey, that.mKey)
                .append(mValue, that.mValue)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(mKey)
                .append(mValue)
                .toHashCode();
    }
}
