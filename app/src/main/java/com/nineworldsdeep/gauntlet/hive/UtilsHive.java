package com.nineworldsdeep.gauntlet.hive;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaListItem;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;

/**
 * Created by brent on 7/5/17.
 */

public class UtilsHive {

    public static ArrayList<MediaListItem> getMediaListItems(Lobe lobe) {

        //TODO: implement
        ArrayList<MediaListItem> lst = new ArrayList<>();

        for(int i = 0; i < 10; i++){

            lst.add(new MediaListItem("a/fake/path/for/testing"));
        }

        return lst;
    }

    /**
     * returns null if doesn't exist in the database
     * @param ctx
     * @param db
     * @return
     */
    public static HiveRoot getLocalHiveRoot(Context ctx, NwdDb db){

        String deviceName = Configuration.getLocalDeviceName();

        for(HiveRoot hr : db.getActiveHiveRoots(ctx)){

            if(hr.getHiveRootName().equalsIgnoreCase(deviceName)){

                return hr;
            }
        }

        return null;
    }
}
