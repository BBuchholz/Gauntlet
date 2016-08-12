package com.nineworldsdeep.gauntlet.tapestry.v2;

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

    public abstract void forceRefresh();
}
