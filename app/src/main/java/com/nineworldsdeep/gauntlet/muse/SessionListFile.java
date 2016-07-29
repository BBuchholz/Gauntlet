package com.nineworldsdeep.gauntlet.muse;

import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.synergy.v2.LineItemListFile;

/**
 * Created by brent on 11/28/15.
 */
public class SessionListFile extends LineItemListFile {
    public SessionListFile(String sessionName) {
        super(sessionName, Configuration.getMuseSessionNotesDirectory());
    }
}
