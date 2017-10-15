package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.io.File;
import java.util.HashMap;

/**
 * Created by brent on 10/14/17.
 */

public class MnemosyneRegistry {

    private static HashMap<File, String> filesToHashes;
    private static HashMap<String, MediaListItem> hashesToMediaListItems;

    static {

        filesToHashes = new HashMap<>();
        hashesToMediaListItems = new HashMap<>();
    }

    public static void register(MediaListItem mli) throws Exception {

        File f = mli.getFile();

        if(filesToHashes.containsKey(f)){

            if(mli.getHash() == null ||
                    !mli.getHash().equalsIgnoreCase(filesToHashes.get(f))) {
                //we test to avoid setting a dirty flag if the value is same
                mli.setHash(filesToHashes.get(f));
            }

        }else {

            mli.hashMedia();
            filesToHashes.put(f, mli.getHash());
        }
    }

    public static void sync(MediaListItem mli, NwdDb db) throws Exception {

        if(mli.getHash() == null){

            throw new Exception("Call MnemosyneRegistry.register() before sync()");
        }

        String hashToLower = mli.getHash().toLowerCase();

        if(Utils.stringIsNullOrWhitespace(hashToLower)){

            return;
        }

        if(hashesToMediaListItems.containsKey(hashToLower)){

            Media thisMedia =
                    hashesToMediaListItems.get(hashToLower).getMedia();

            if(thisMedia.isDirty()){

                db.sync(thisMedia);
                db.sync(mli.getMedia());
                hashesToMediaListItems.put(hashToLower, mli);

            }else if(mli.getMedia().isDirty()){

                db.sync(mli.getMedia());
                hashesToMediaListItems.put(hashToLower, mli);
            }

        }else{

            if(mli.getMedia().isDirty()){

                db.sync(mli.getMedia());
            }

            hashesToMediaListItems.put(hashToLower, mli);
        }
    }

}
