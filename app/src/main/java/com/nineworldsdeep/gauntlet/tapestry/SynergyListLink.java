package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

/**
 * Created by brent on 4/29/16.
 */
public class SynergyListLink extends TapestryNodeLink {
    public SynergyListLink(String synergyListName) {
        super(processName(synergyListName), LinkType.SynergyListLink);

        put("img", String.valueOf(R.mipmap.ic_nwd_synergy_list));

        setListName(processName(synergyListName));
    }

    private static String processName(String synergyListName) {

        return Utils.processName(synergyListName);
    }

    public String getListName(){

        return get("listName");
    }

    public void setListName(String listName){

        put("listName", listName);
    }
}
