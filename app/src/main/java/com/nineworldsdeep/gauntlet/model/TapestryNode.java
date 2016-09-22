package com.nineworldsdeep.gauntlet.model;

/**
 * Created by brent on 9/22/16.
 */

public interface TapestryNode {

    /**
     * determines if a TapestryNode supersedes the parameter node.
     * within the context of NWD, when two nodes are compared for truing,
     * a node supercedes another if it requires no update (because it either
     * wholly includes the other node, or its timestamps for all changes
     * are more recent). Nodes of different types can be compared, and it
     * is up to classes implementing this interface to decide which
     * other types it can compare with. Any incompatible comparisons should
     * return false. eg: FileNode may supercede HashNode if the Hash is one
     * of the hashes already registered for the File, however LocalConfigNode
     * will never supercede FileNode (nor will FileNode ever supercede
     * LocalConfigNode, they are incompatible types). When implementing
     * only Node types directly coupled should be tested for, to promote
     * loose coupling wherever possible (ie. test Files for Hashes but
     * leave out anything dealing with LocalConfig, they never touch
     * each other conceptually)
     * @param nd
     * @return
     */
    boolean supersedes(TapestryNode nd);
}
