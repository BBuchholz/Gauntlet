package com.nineworldsdeep.gauntlet.synergy.v2;

import com.nineworldsdeep.gauntlet.core.Configuration;

/**
 * Created by brent on 10/31/15.
 */
public class SynergyArchiveFile extends LineItemListFile {
    public SynergyArchiveFile(String name) {
        super(name, Configuration.getArchiveDirectory());
    }
}
