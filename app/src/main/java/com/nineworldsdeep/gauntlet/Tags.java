package com.nineworldsdeep.gauntlet;

import android.text.TextUtils;

import com.nineworldsdeep.gauntlet.sqlite.NwdContract;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListFile;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListItem;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brent on 5/10/16.
 */
public class Tags {

    public static ArrayList<String> getFrequent() {

        ArrayList<String> lst = new ArrayList<>();

        //default tags
//        lst.add("lyricBit");
//        lst.add("rhythmBit");
//        lst.add("nwdPlanning");
//        lst.add("topics and rants");

        SynergyListFile slf = new SynergyListFile("Tags-Frequent");

        //ensure
        if(!slf.exists()){
            slf.save();
        }

        slf.loadItems();

        for(SynergyListItem sli : slf.getItems()){

            lst.add(sli.getItem());
        }

        return lst;
    }

    /**
     * returns a new tag string with the tag toggled
     * (removed if it was in the original,
     * and added if it wasn't)
     * @param tag
     * @param currentString
     * @return
     */
    public static String toggleTag(String tag, String currentString) {

        ArrayList<String> lst = new ArrayList<>();

        boolean found = false;

        for(String existingTag : currentString.split(",")){

            existingTag = existingTag.trim();

            if(!Utils.stringIsNullOrWhitespace(existingTag)){

                if(existingTag.equalsIgnoreCase(tag)){

                    found = true;

                }else{

                    lst.add(existingTag);
                }
            }
        }

        if(!found){

            lst.add(tag);
        }

        return StringUtils.join(lst, ", ");
    }

    public static HashMap<String, String> getPathToActiveTagStringMap(NwdDb db) {

        List<Map<String, String>> records =
                db.getActiveV5PathTagRecordsForCurrentDevice();

        MultiMapString pathToTags =
                new MultiMapString();

        for(Map<String, String> map : records){

            String path = map.get(NwdContract.COLUMN_MEDIA_PATH_VALUE);
            String tag = map.get(NwdContract.COLUMN_MEDIA_TAG_VALUE);

            File f = new File(path);

            if(f.exists()) {

                pathToTags.put(path, tag);
            }
        }

//        HashMap<String, String> output =
//                convertPathTagMultiMapToPathTagStringHashMap(pathToTags);
//
//        return output;

        return convertPathTagMultiMapToPathTagStringHashMap(pathToTags);
    }

    public static HashMap<String, String>
        convertPathTagMultiMapToPathTagStringHashMap(
            MultiMapString pathToTags) {

        HashMap<String,String> pathToTagString =
                new HashMap<>();

        for(String path : pathToTags.keySet()){

            pathToTagString.put(path,
                    TextUtils.join(", ", pathToTags.get(path)));
        }

        return pathToTagString;
    }
}
