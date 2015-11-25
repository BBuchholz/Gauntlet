package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.LineItemListFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 11/17/15.
 */
public class DisplayNameIndexFile extends LineItemListFile{

    public DisplayNameIndexFile() {
        super("DisplayNameIndex",
                Configuration.getConfigDirectory());
    }


    public void addDisplayName(String name, String path) {

        String entry = "displayName={" + name + "} ";
        entry += "path={" + path + "} ";

        add(entry);
    }

    public List<FileListItem> getImageListItems() {

        ArrayList<FileListItem> lst = new ArrayList<>();

        for(String item : getItems()){
            lst.add(getImageListItemFromLineItem(item));
        }

        return lst;
    }

    private FileListItem getImageListItemFromLineItem(String item) {

        FileListItem ili =
                new FileListItem(Utils.processExtract(item, "path"),
                        Utils.processExtract(item, "displayName"));

        return ili;
    }
}
