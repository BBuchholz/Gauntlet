package com.nineworldsdeep.gauntlet.synergy.v2;

import com.nineworldsdeep.gauntlet.core.Configuration;

/**
 * Created by brent on 10/31/15.
 */
public class SynergyListFile extends LineItemListFile {

    public SynergyListFile(String name) {
        super(name, Configuration.getSynergyDirectory());
    }
}
