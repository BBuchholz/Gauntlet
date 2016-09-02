package com.nineworldsdeep.gauntlet.tapestry.v2;

import com.nineworldsdeep.gauntlet.model.FileModelItem;
import com.nineworldsdeep.gauntlet.model.HashModelItem;
import com.nineworldsdeep.gauntlet.model.LocalConfigModelItem;
import com.nineworldsdeep.gauntlet.model.SynergyListItemModelItem;
import com.nineworldsdeep.gauntlet.model.SynergyListModelItem;
import com.nineworldsdeep.gauntlet.model.TagModelItem;

import java.util.ArrayList;

/**
 * Created by brent on 8/11/16.
 */
public class DatabaseNode extends TrueableNode {
    @Override
    public void forceRefresh() {

    }

    @Override
    public ArrayList<FileModelItem> getFiles() {
        return null;
    }

    @Override
    public ArrayList<HashModelItem> getHashes() {
        return null;
    }

    @Override
    public ArrayList<TagModelItem> getTags() {
        return null;
    }

    @Override
    public ArrayList<LocalConfigModelItem> getLocalConfig() {
        return null;
    }

    @Override
    public ArrayList<SynergyListModelItem> getSynergyLists() {
        return null;
    }

    @Override
    public ArrayList<SynergyListItemModelItem> getSynergyListItems() {
        return null;
    }
    //NEED LAZY LOADING ON ALL
}
