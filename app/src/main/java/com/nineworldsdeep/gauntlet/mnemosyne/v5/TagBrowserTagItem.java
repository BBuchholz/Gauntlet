package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import java.util.ArrayList;

public class TagBrowserTagItem {

    private ArrayList<TagBrowserFileItem> fileItems;
    private String tagName;
    private int tagId;
    private boolean loaded;

    public TagBrowserTagItem(String tagName){
        this.tagName = tagName;
        fileItems = new ArrayList<>();
        loaded = false;
    }

    public TagBrowserTagItem(int tagId, String tagName){
        this(tagName); //chained constructor

        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public int getTaggedCount() {
        return fileItems.size();
    }

    public boolean isLoaded() {
        //we will be lazy loading tag items, this will
        //return true if the file items have been loaded from the
        //database and the item hasn't been added to (needs refresh)

        return loaded;
    }

    public void setLoaded(boolean loadedValue){
        this.loaded = loadedValue;
    }

    public String getTagDisplayName() {

        return getTagName() + " (" + Integer.toString(getTaggedCount()) + ")";
    }
}
