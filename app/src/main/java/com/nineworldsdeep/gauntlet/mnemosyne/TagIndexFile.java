package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.LineItemListFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 11/17/15.
 */
public class TagIndexFile extends LineItemListFile{

    public TagIndexFile() {
        super("TagIndex",
                Configuration.getConfigDirectory());
    }


    public void addTagString(String tags, String path) {

        String entry = "tags={" + tags + "} ";
        entry += "path={" + path + "} ";

        add(entry);
    }

    public List<FileTagFragment> getFileTagFragments() {

        ArrayList<FileTagFragment> lst = new ArrayList<>();

        for(String item : getItems()){
            lst.add(getFileTagFragmentFromLineItem(item));
        }

        return lst;
    }

    private FileTagFragment getFileTagFragmentFromLineItem(String item) {

        FileTagFragment ftf =
                new FileTagFragment(item);

        return ftf;
    }
}
