package com.nineworldsdeep.gauntlet.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

/**
 * Created by brent on 9/1/16.
 */
public class TagNode implements TapestryNode {

    private String mTag;
    private ArrayList<FileNode> mFiles =
            new ArrayList<>();

    public TagNode(FileNode file, String tag){

        mTag = tag;

        addFile(file);
    }

    public void addFile(FileNode file) {

        if(file != null && !mFiles.contains(file)) {

            mFiles.add(file);
        }
    }

    public String value(){

        return mTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TagNode that = (TagNode) o;

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

    @Override
    public boolean supersedes(TapestryNode nd) {
        return false;
    }
}
