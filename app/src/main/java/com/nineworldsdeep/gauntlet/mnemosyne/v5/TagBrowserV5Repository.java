package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import java.util.ArrayList;

public class TagBrowserV5Repository {

    public static ArrayList<TagBrowserTagItem> getTagItems() {

        ArrayList<TagBrowserTagItem> mockItems = new ArrayList<>();

        mockItems.add(new TagBrowserTagItem());

        return mockItems;
    }

    public static void loadFileItems(TagBrowserTagItem tagBrowserTagItem) {

    }

    public static ArrayList<TagBrowserFileItem> getFileItems(String tagFilter) {

        ArrayList<TagBrowserFileItem> mockItems = new ArrayList<>();

        for(int i = 0; i < 10; i++)

        mockItems.add(new TagBrowserFileItem(tagFilter + " " + Integer.toString(i)));

        return mockItems;
    }
}
