package com.nineworldsdeep.gauntlet.synergy.v2;

import com.nineworldsdeep.gauntlet.Configuration;

import java.io.File;

/**
 * Created by brent on 10/31/15.
 */
public class SynergyTemplateFile extends LineItemListFile {
    public SynergyTemplateFile(String name) {
        super(name, Configuration.getTemplateDirectory());
    }
}
