package com.nineworldsdeep.gauntlet.bookSegments;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Fragment;
import com.nineworldsdeep.gauntlet.synergy.v2.LineItemListFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 11/8/15.
 */
public class AliasListFile extends LineItemListFile {

    public AliasListFile() {
        super("AliasList", Configuration.getConfigDirectory());
    }

    public void addAlias(String aliasText,
                         String titleText,
                         String authorText,
                         String yearText) {

        String entry = "alias={" + aliasText + "} ";
        entry += "title={" + titleText + "} ";
        entry += "author={" + authorText +"} ";
        entry += "year={" + yearText + "} ";

        add(entry);
    }

    public List<Alias> getAliases() {

        ArrayList<Alias> lst =
                new ArrayList<>();

        for(String item : getItems()){
            lst.add(new Alias(item));
        }

        return lst;
    }
}
