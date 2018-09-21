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

    public static ArrayList<TagBrowserTagItem> getTagItems(String tagFilter) {

        ArrayList<TagBrowserTagItem> filteredItems =
                new ArrayList<>();

        for(TagBrowserTagItem tagBrowserTagItem : tagItems){

            if(tagBrowserTagItem.getTagName().toLowerCase().contains(tagFilter.toLowerCase())){
                filteredItems.add(tagBrowserTagItem);
            }
        }

        return filteredItems;
    }

    public static void loadFileItems(TagBrowserTagItem tagBrowserTagItem) {

        //lazy load file items here, just mocking this at the moment
        tagBrowserTagItem.setLoaded(true);
    }

    public static ArrayList<TagBrowserFileItem> getFileItems(String tagFilter, String fileFilter) {


        /////////////////////////////begin - just for mock
        ArrayList<TagBrowserFileItem> mockItems = new ArrayList<>();

        for(int i = 0; i < 10; i++) {

            TagBrowserFileItem tagBrowserFileItem = new TagBrowserFileItem(tagFilter + " file " + Integer.toString(i));

            mockItems.add(tagBrowserFileItem);
        }
        ////////////////////////////end - just for mock

        ArrayList<TagBrowserFileItem> filteredItems = new ArrayList<>();

        for(TagBrowserFileItem tagBrowserFileItem : mockItems){

            if(tagBrowserFileItem.getFilename().toLowerCase().contains(
                    fileFilter.toLowerCase())){
                filteredItems.add(tagBrowserFileItem);
            }
        }


        return filteredItems;
    }
}
