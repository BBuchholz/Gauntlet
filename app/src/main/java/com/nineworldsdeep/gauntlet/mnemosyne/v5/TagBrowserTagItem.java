package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import java.util.ArrayList;

public class TagBrowserTagItem {

    private ArrayList<TagBrowserFileItem> fileItems;
    private String tagName;

    public TagBrowserTagItem(String tagName){
        this.tagName = tagName;
        fileItems = new ArrayList<>();
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

        return true;
    }

    public String getTagDisplayName() {

        return getTagName() + " (" + Integer.toString(getTaggedCount()) + ")";
    }
}
