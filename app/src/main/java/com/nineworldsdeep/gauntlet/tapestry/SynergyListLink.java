package com.nineworldsdeep.gauntlet.tapestry;

import android.content.Context;
import android.content.Intent;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListActivity;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyV3MainActivity;

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

    @Override
    public Intent getIntent(Context c) {

        Intent intent = new Intent(c, SynergyListActivity.class);
        intent.putExtra(
                SynergyV3MainActivity.EXTRA_SYNERGYMAIN_LISTNAME,
                getListName()
        );

        return intent;
    }
}
