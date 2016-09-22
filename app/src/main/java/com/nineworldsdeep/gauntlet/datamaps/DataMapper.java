package com.nineworldsdeep.gauntlet.datamaps;

import com.nineworldsdeep.gauntlet.model.DeviceNode;
import com.nineworldsdeep.gauntlet.model.FileNode;
import com.nineworldsdeep.gauntlet.model.HashNode;
import com.nineworldsdeep.gauntlet.model.LocalConfigNode;
import com.nineworldsdeep.gauntlet.model.SynergyItemNode;
import com.nineworldsdeep.gauntlet.model.SynergyListNode;
import com.nineworldsdeep.gauntlet.model.SynergyToDoNode;
import com.nineworldsdeep.gauntlet.model.TagNode;

import java.util.ArrayList;

/**
 * Created by brent on 8/11/16.
 */
public interface DataMapper {

    //for each node type, need load, populate(true), and upsert
    //as well as bulk versions of each (loadAll, populateAll, upsertAll)

    //region File Subset

    //Devices
    DeviceNode loadDeviceNode(String deviceName);
    void populate(DeviceNode nd);
    void upsert(DeviceNode nd);
    ArrayList<DeviceNode> loadAllDeviceNodes();
    void populateDeviceNodes(ArrayList<DeviceNode> lst);
    void upsertDeviceNodes(ArrayList<DeviceNode> lst);

    //Files
    FileNode loadFileNode(String deviceName, String path);
    void populate(FileNode nd);
    void upsert(FileNode nd);
    ArrayList<FileNode> loadAllFileNodes(String deviceName);
    void populateFileNodes(ArrayList<FileNode> lst);
    void upsertFileNodes(ArrayList<FileNode> lst);

    //Hashes
    HashNode loadHashNode(String sha1Hash);
    void populate(HashNode nd);
    void upsert(HashNode nd);
    ArrayList<HashNode> loadAllHashNodes();
    void populateHashNodes(ArrayList<HashNode> lst);
    void upsertHashNodes(ArrayList<HashNode> lst);

    //Tags
    TagNode loadTagNode(String tagValue);
    void populate(TagNode nd);
    void upsert(TagNode nd);
    ArrayList<TagNode> loadAllTagNodes();
    void populateTagNodes(ArrayList<TagNode> lst);
    void upsertTagNodes(ArrayList<TagNode> lst);

    //LocalConfig
    LocalConfigNode loadLocalConfigNode(String deviceName);
    void populate(LocalConfigNode nd);
    void upsert(LocalConfigNode nd);
    ArrayList<LocalConfigNode> loadAllLocalConfigNodes(String deviceName);
    void populateLocalConfigNodes(ArrayList<LocalConfigNode> lst);
    void upsertLocalConfigNodes(ArrayList<LocalConfigNode> lst);

    //endregion

    //region Synergy Subset

    //SynergyLists
    SynergyListNode loadSynergyListNode(String listName);
    void populate(SynergyListNode nd);
    void upsert(SynergyListNode nd);
    ArrayList<SynergyListNode> loadAllSynergyListNodes(String deviceName);
    void populateSynergyListNodes(ArrayList<SynergyListNode> lst);
    void upsertSynergyListNodes(ArrayList<SynergyListNode> lst);

    //SynergyItems
    SynergyItemNode loadSynergyItemNode(String listName, String itemText);
    void populate(SynergyItemNode nd);
    void upsert(SynergyItemNode nd);
    ArrayList<SynergyItemNode> loadAllSynergyItemNodes(String listName);
    void populateSynergyItemNodes(ArrayList<SynergyItemNode> lst);
    void upsertSynergyItemNodes(ArrayList<SynergyItemNode> lst);

    //SynergyToDos
    SynergyToDoNode loadSynergyToDoNode(String listName, String itemText);
    void populate(SynergyToDoNode nd);
    void upsert(SynergyToDoNode nd);
    ArrayList<SynergyToDoNode> loadAllSynergyToDoNodes(String listName);
    void populateSynergyToDoNodes(ArrayList<SynergyToDoNode> lst);
    void upsertSynergyToDoNodes(ArrayList<SynergyToDoNode> lst);

    //endregion

    //region Lyric Subset

    //TODO

    //endregion

    //region Sync Subset

    //TODO

    //endregion
}
