package com.nineworldsdeep.gauntlet.model;

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
}
