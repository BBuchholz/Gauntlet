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
public interface DataMapper {

    ArrayList<FileNode> getFiles();
    ArrayList<HashNode> getHashes();
    ArrayList<TagNode> getTags();
    ArrayList<LocalConfigNode> getLocalConfig();
    ArrayList<SynergyListNode> getSynergyLists();
    ArrayList<SynergyListItemNode> getSynergyListItems();

}
