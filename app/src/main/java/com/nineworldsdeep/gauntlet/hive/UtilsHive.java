package com.nineworldsdeep.gauntlet.hive;

import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaListItem;

import java.util.ArrayList;

/**
 * Created by brent on 7/5/17.
 */

class UtilsHive {

    public static ArrayList<MediaListItem> getMediaListItems(Lobe lobe) {

        //TODO: implement
        ArrayList<MediaListItem> lst = new ArrayList<>();

        for(int i = 0; i < 10; i++){

            lst.add(new MediaListItem("a/fake/path/for/testing"));
        }

        return lst;
    }
}
