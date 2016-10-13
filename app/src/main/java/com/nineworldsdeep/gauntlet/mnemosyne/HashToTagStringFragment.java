package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.Fragment;

/**
 * Created by brent on 12/1/15.
 */
public class HashToTagStringFragment extends Fragment {

    public HashToTagStringFragment(String lineItem){
        super(lineItem);

        processExtract("tags");
        processExtract("sha1Hash");

        setDisplayKey("path");
    }

    public String getHash() {
        return get("sha1Hash");
    }

    public String getTagString() {
        return get("tags");
    }
}
