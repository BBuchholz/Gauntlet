package com.nineworldsdeep.gauntlet.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

/**
 * Created by brent on 9/1/16.
 */
public class TagModelItem {

    private String mTag;
    private ArrayList<FileModelItem> mFiles =
            new ArrayList<>();

    public TagModelItem(FileModelItem file, String tag){

        mTag = tag;
        mFiles.add(file);
    }

    public String value(){

        return mTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TagModelItem that = (TagModelItem) o;

        return new EqualsBuilder()
                .append(mTag, that.mTag)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(mTag)
                .toHashCode();
    }
}
