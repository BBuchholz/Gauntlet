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
public abstract class TrueableNode {
    //NEED LAZY LOADING ON ALL

    //CONCEPTS:
    // have a negotiable interaction between truable nodes through an
    // abstract interface. for instance, supported model items
    // should be retrievable and then on negotiation, it can be determines
    // which node subsets are to be trued (instead of attempting to true
    // everything everytime, especially for large nodes)

    // examples of model items I would like to support include tagged paths,
    // or tagged media, or both; textual data or pdf annotations; lyric bits,
    // melody bits, rhythm bits, &c.
    // all of these use cases should be represented on the model level, and
    // then the node should be able to report on which items it supports)

    // a db node may support the entire data model, or just a specialized
    // subset. an xml node may only support a few model items that
    // are determined on a scan of non-empty nodes (dynamically determined)
    // these and other possibilities are what whet my interest
    // in pursing this node model.

    // UPDATE: let's have a method that takes all comparison types
    // and treats any unsupported types as an empty list

    public abstract void forceRefresh();
    public abstract ArrayList<FileModelItem> getFiles();
    public abstract ArrayList<HashModelItem> getHashes();
    public abstract ArrayList<TagModelItem> getTags();
    public abstract ArrayList<LocalConfigModelItem> getLocalConfig();
    public abstract ArrayList<SynergyListModelItem> getSynergyLists();
    public abstract ArrayList<SynergyListItemModelItem> getSynergyListItems();

}
