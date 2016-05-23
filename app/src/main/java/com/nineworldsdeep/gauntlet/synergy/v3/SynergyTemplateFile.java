package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.Configuration;

import java.io.File;

/**
 * Created by brent on 5/23/16.
 */
public class SynergyTemplateFile extends SynergyListFile {

    public SynergyTemplateFile(String listName){
        super(listName,
                new File(Configuration.getTemplateDirectory(), listName + ".txt"));
    }
}
