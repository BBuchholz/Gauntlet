package com.nineworldsdeep.gauntlet.model;

/**
 * Created by brent on 9/22/16.
 */
public class DeviceNode implements TapestryNode {
    @Override
    public boolean supersedes(TapestryNode nd) {
        return false;
    }
}
