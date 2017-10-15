package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import com.nineworldsdeep.gauntlet.Utils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by brent on 10/14/17.
 */

public class MnemosyneRegistry {

    private static HashMap<File, String> filesToHashes;

    static {

        filesToHashes = new HashMap<>();
    }

    public static void register(MediaListItem mli) throws Exception {

        File f = mli.getFile();

        if(!filesToHashes.containsKey(f)){

            mli.hashMedia();
            filesToHashes.put(f, mli.getHash());

        }else {

            mli.setHash(filesToHashes.get(f));
        }
    }
}
