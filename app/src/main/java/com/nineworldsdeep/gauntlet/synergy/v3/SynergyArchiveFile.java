package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.core.Configuration;

import java.io.File;

/**
 * Created by brent on 1/28/16.
 */
public class SynergyArchiveFile extends SynergyListFile {

    public SynergyArchiveFile(String listName){
        super(listName,
                new File(Configuration.getArchiveDirectory(), listName + ".txt"));
    }
}
