package com.nineworldsdeep.gauntlet;

import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListFile;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListItem;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 5/10/16.
 */
public class Tags {

    public static List<String> getFrequent() {

        ArrayList<String> lst = new ArrayList<>();

        //default tags
        lst.add("lyricBit");
        lst.add("rhythmBit");
        lst.add("nwdPlanning");
        lst.add("topics and rants");

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
}
