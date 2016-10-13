package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.synergy.v2.LineItemListFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brent on 12/1/15.
 */
public class HashToTagsIndexFile extends LineItemListFile {

    public HashToTagsIndexFile(String nameWithoutExtension){
        super(nameWithoutExtension,
                Configuration.getConfigDirectory());
    }

    public List<HashToTagStringFragment> getHashToTagStringFragments() {

        List<HashToTagStringFragment> lst = new ArrayList<>();

        for(String item : getItems()){
            lst.add(new HashToTagStringFragment(item));
        }

        return lst;
    }
}
