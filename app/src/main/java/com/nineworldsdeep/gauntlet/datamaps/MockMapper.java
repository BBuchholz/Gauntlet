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
 * Created by brent on 9/22/16.
 */

public class MockMapper implements DataMapper {

    //region File Subset

    //region Devices

    @Override
    public DeviceNode loadDeviceNode(String deviceName) {
        return null;
    }

    @Override
    public void populate(DeviceNode nd) {

    }

    @Override
    public void upsert(DeviceNode nd) {

    }

    @Override
    public ArrayList<DeviceNode> loadAllDeviceNodes() {
        return null;
    }

    @Override
    public void populateDeviceNodes(ArrayList<DeviceNode> lst) {

    }

    @Override
    public void upsertDeviceNodes(ArrayList<DeviceNode> lst) {

    }

    //endregion

    //region Files

    @Override
    public FileNode loadFileNode(String deviceName, String path) {
        return null;
    }

    @Override
    public void populate(FileNode nd) {

    }

    @Override
    public void upsert(FileNode nd) {

    }

    @Override
    public ArrayList<FileNode> loadAllFileNodes(String deviceName) {
        return null;
    }

    @Override
    public void populateFileNodes(ArrayList<FileNode> lst) {

    }

    @Override
    public void upsertFileNodes(ArrayList<FileNode> lst) {

    }

    //endregion

    //region Hashes

    @Override
    public HashNode loadHashNode(String sha1Hash) {
        return null;
    }

    @Override
    public void populate(HashNode nd) {

    }

    @Override
    public void upsert(HashNode nd) {

    }

    @Override
    public ArrayList<HashNode> loadAllHashNodes() {
        return null;
    }

    @Override
    public void populateHashNodes(ArrayList<HashNode> lst) {

    }

    @Override
    public void upsertHashNodes(ArrayList<HashNode> lst) {

    }

    //endregion

    //region Tags

    @Override
    public TagNode loadTagNode(String tagValue) {
        return null;
    }

    @Override
    public void populate(TagNode nd) {

    }

    @Override
    public void upsert(TagNode nd) {

    }

    @Override
    public ArrayList<TagNode> loadAllTagNodes() {
        return null;
    }

    @Override
    public void populateTagNodes(ArrayList<TagNode> lst) {

    }

    @Override
    public void upsertTagNodes(ArrayList<TagNode> lst) {

    }

    //endregion

    //region Local Config

    @Override
    public LocalConfigNode loadLocalConfigNode(String deviceName) {
        return null;
    }

    @Override
    public void populate(LocalConfigNode nd) {

    }

    @Override
    public void upsert(LocalConfigNode nd) {

    }

    @Override
    public ArrayList<LocalConfigNode> loadAllLocalConfigNodes(String deviceName) {
        return null;
    }

    @Override
    public void populateLocalConfigNodes(ArrayList<LocalConfigNode> lst) {

    }

    @Override
    public void upsertLocalConfigNodes(ArrayList<LocalConfigNode> lst) {

    }

    //endregion

    //endregion

    //region Synergy Subset

    //region Synergy Lists

    @Override
    public SynergyListNode loadSynergyListNode(String listName) {
        return null;
    }

    @Override
    public void populate(SynergyListNode nd) {

    }

    @Override
    public void upsert(SynergyListNode nd) {

    }

    @Override
    public ArrayList<SynergyListNode> loadAllSynergyListNodes(String deviceName) {
        return null;
    }

    @Override
    public void populateSynergyListNodes(ArrayList<SynergyListNode> lst) {

    }

    @Override
    public void upsertSynergyListNodes(ArrayList<SynergyListNode> lst) {

    }

    //endregion

    //region Synergy Items

    @Override
    public SynergyItemNode loadSynergyItemNode(String listName, String itemText) {
        return null;
    }

    @Override
    public void populate(SynergyItemNode nd) {

    }

    @Override
    public void upsert(SynergyItemNode nd) {

    }

    @Override
    public ArrayList<SynergyItemNode> loadAllSynergyItemNodes(String listName) {
        return null;
    }

    @Override
    public void populateSynergyItemNodes(ArrayList<SynergyItemNode> lst) {

    }

    @Override
    public void upsertSynergyItemNodes(ArrayList<SynergyItemNode> lst) {

    }

    //endregion

    //region Synergy ToDos

    @Override
    public SynergyToDoNode loadSynergyToDoNode(String listName, String itemText) {
        return null;
    }

    @Override
    public void populate(SynergyToDoNode nd) {

    }

    @Override
    public void upsert(SynergyToDoNode nd) {

    }

    @Override
    public ArrayList<SynergyToDoNode> loadAllSynergyToDoNodes(String listName) {
        return null;
    }

    @Override
    public void populateSynergyToDoNodes(ArrayList<SynergyToDoNode> lst) {

    }

    @Override
    public void upsertSynergyToDoNodes(ArrayList<SynergyToDoNode> lst) {

    }

    //endregion

    //endregion


}
