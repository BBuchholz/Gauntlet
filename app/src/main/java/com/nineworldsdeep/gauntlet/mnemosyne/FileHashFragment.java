package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.Fragment;

/**
 * Created by brent on 12/1/15.
 */
public class FileHashFragment extends Fragment {

    public FileHashFragment(String lineItem){
        super(lineItem);

        processExtract("path");
        processExtract("sha1Hash");

        setDisplayKey("path");
    }

    public String getHash() {
        return get("sha1Hash");
    }

    public String getPath() {
        return get("path");
    }
}
