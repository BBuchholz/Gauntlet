package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.content.Context;

import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class TagBrowserV5Repository {

    private static ArrayList<TagBrowserTagItem> tagItems;
    private static boolean loaded;

    static {

        tagItems = new ArrayList<>();
        loaded = false;

        //loadMockItems();
    }

    public static void loadTagItems(NwdDb db, Context context){

        for(TagBrowserTagItem tagBrowserTagItem : db.getAllTagBrowserTagItems(context)){
            tagItems.add(tagBrowserTagItem);
        }

        loaded = true;
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

        String[] tagSubFilters = tagFilter.split(",");

        for(TagBrowserTagItem tagBrowserTagItem : tagItems){

            for(String tagSubFilter : tagSubFilters) {

                if (tagBrowserTagItem.getTagName().toLowerCase().contains(tagSubFilter.trim().toLowerCase())) {
                    filteredItems.add(tagBrowserTagItem);
                }
            }
        }

        return filteredItems;
    }

    public static TagBrowserTagItem getTagItemForTagName(String tagName) {

        TagBrowserTagItem foundTagBrowserTagItem = null;

        for(TagBrowserTagItem tagBrowserTagItem : tagItems){

            if(tagBrowserTagItem.getTagName().equalsIgnoreCase(tagName)){
                foundTagBrowserTagItem = tagBrowserTagItem;
            }
        }

        return foundTagBrowserTagItem;
    }

    public static void loadFileItems(TagBrowserTagItem tagBrowserTagItem, NwdDb db) {

        if(!tagBrowserTagItem.isLoaded()) {

            for (TagBrowserFileItem tagBrowserFileItem :
                    db.getTagBrowserFileItemsForTagId(
                            tagBrowserTagItem.getTagId())) {

                tagBrowserTagItem.addFileItem(tagBrowserFileItem);
            }

            tagBrowserTagItem.setLoaded(true);
        }
    }

    public static TreeSet<TagBrowserFileItem> getFileItems(String tagName, String fileFilter) {

        TreeSet<TagBrowserFileItem> filteredItems = new TreeSet<>();


//        /////////////////////////////begin - just for mock
//        ArrayList<TagBrowserFileItem> mockItems = new ArrayList<>();
//
//        for(int i = 0; i < 10; i++) {
//
//            TagBrowserFileItem tagBrowserFileItem = new TagBrowserFileItem(tagFilter + " file " + Integer.toString(i));
//
//            mockItems.add(tagBrowserFileItem);
//        }
//
//
//        for(TagBrowserFileItem tagBrowserFileItem : mockItems){
//
//            if(tagBrowserFileItem.getFilename().toLowerCase().contains(
//                    fileFilter.toLowerCase())){
//                filteredItems.add(tagBrowserFileItem);
//            }
//        }
//
//        ////////////////////////////end - just for mock




        // this supports a single tag (treats the tag filter as a single string)
        // will be updated to support multi tag filter before implementation
        // is complete, this is just an intermediate implementation

        TagBrowserTagItem tagBrowserTagItem = getTagItemForTagName(tagName);

        for (TagBrowserFileItem tagBrowserFileItem : tagBrowserTagItem.getFileItems()) {

            if (tagBrowserFileItem.getFilename().toLowerCase().contains(
                    fileFilter.toLowerCase())) {
                filteredItems.add(tagBrowserFileItem);
            }
        }




        return filteredItems;
    }

    public static boolean isLoaded() {
        return loaded;
    }
}
