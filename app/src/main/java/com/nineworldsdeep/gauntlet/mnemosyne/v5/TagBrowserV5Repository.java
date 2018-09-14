package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.util.ArrayList;

public class TagBrowserV5Repository {

    private static ArrayList<TagBrowserTagItem> tagItems;

    static {

        tagItems = new ArrayList<>();

        loadMockItems();
    }

    private static void loadMockItems(){

        ArrayList<TagBrowserTagItem> mockItems = new ArrayList<>();


        Lorem lorem = LoremIpsum.getInstance();

        for(int i = 1; i <= 10; i++) {

            String loremValue = lorem.getWords(1, 3);

            tagItems.add(new TagBrowserTagItem(loremValue));
        }
    }

    public static ArrayList<TagBrowserTagItem> getTagItems() {

        return tagItems;
    }

    public static void loadFileItems(TagBrowserTagItem tagBrowserTagItem) {

    }

    public static ArrayList<TagBrowserFileItem> getFileItems(String tagFilter) {

        ArrayList<TagBrowserFileItem> mockItems = new ArrayList<>();

        for(int i = 0; i < 10; i++)

        mockItems.add(new TagBrowserFileItem(tagFilter + " file " + Integer.toString(i)));

        return mockItems;
    }
}
