package com.nineworldsdeep.gauntlet.datamaps;

import com.nineworldsdeep.gauntlet.model.FileNode;
import com.nineworldsdeep.gauntlet.model.HashNode;
import com.nineworldsdeep.gauntlet.model.LocalConfigNode;
import com.nineworldsdeep.gauntlet.model.SynergyListItemNode;
import com.nineworldsdeep.gauntlet.model.SynergyListNode;
import com.nineworldsdeep.gauntlet.model.TagNode;

import java.util.ArrayList;

/**
 * Created by brent on 8/11/16.
 */
public class DatabaseMapper implements DataMapper {

    @Override
    public ArrayList<FileNode> getFiles() {
        return null;
    }

    @Override
    public ArrayList<HashNode> getHashes() {
        return null;
    }

    @Override
    public ArrayList<TagNode> getTags() {
        return null;
    }

    @Override
    public ArrayList<LocalConfigNode> getLocalConfig() {
        return null;
    }

    @Override
    public ArrayList<SynergyListNode> getSynergyLists() {
        return null;
    }

    @Override
    public ArrayList<SynergyListItemNode> getSynergyListItems() {
        return null;
    }
    //NEED LAZY LOADING ON ALL
}
