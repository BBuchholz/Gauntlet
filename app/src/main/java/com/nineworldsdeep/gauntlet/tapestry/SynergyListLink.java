package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.R;

/**
 * Created by brent on 4/29/16.
 */
public class SynergyListLink extends TapestryNodeLink {
    public SynergyListLink(String synergyListName) {
        super(processName(synergyListName), LinkType.SynergyListLink);

        put("img", String.valueOf(R.mipmap.ic_nwd_singlenode));
    }

    private static String processName(String synergyListName) {

        //TODO: call whatever function is used to validate names elsewhere
        return synergyListName;
    }
}
