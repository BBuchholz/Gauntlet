package com.nineworldsdeep.gauntlet.hive;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaListItem;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.io.File;
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

    public static boolean checkAndRegisterNewRootFolders(NwdDb db, Context ctx) {

        ArrayList<String> rootNamesFromFolderNames = new ArrayList<>();
        ArrayList<String> unregisteredFolderNames = new ArrayList<>();
        ArrayList<HiveRoot> allRoots = db.getAllHiveRoots(ctx);

        //get immediate sub folders of SyncRoot (NWD-SYNC/hive/)
        for(File hiveRootFolder :
                Configuration.getFileSystemTopLevelHiveRootFolders()){

            boolean found = false;

            for(HiveRoot hr : allRoots){

                if(hr.getHiveRootName().equalsIgnoreCase(
                        hiveRootFolder.getName())){

                    found = true;
                }
            }

            if(!found){

                unregisteredFolderNames.add(hiveRootFolder.getName());
            }
        }

        //register them all
        for(String unregisteredRootName : unregisteredFolderNames){

            HiveRoot newHr = new HiveRoot(-1, unregisteredRootName);
            db.syncByName(ctx, newHr);
            newHr.deactivate();
            db.syncByName(ctx, newHr);
        }

        return unregisteredFolderNames.size() > 0;
    }
}
