package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListFile;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListItem;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyUtils;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5List;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5ListItem;

/**
 * Created by brent on 11/16/16.
 */
public class AsyncOperationImportSynergyV3ToV5 extends AsyncOperation {

    public AsyncOperationImportSynergyV3ToV5(IStatusActivity statusActivity){
        super(statusActivity, "Importing Synergy V3 to V5");
    }

    @Override
    protected void runOperation() {

        NwdDb db = NwdDb.getInstance(statusEnabledActivity.getAsActivity());
        db.open();

        for (String listName : SynergyUtils.getAllListNames()){

            publishProgress("importing list [" + listName + "]...");

            SynergyListFile slf = new SynergyListFile(listName);
            slf.loadItems();

            SynergyV5List v5List = new SynergyV5List(listName);

            for(SynergyListItem sli : slf.getItems()){

                int position = v5List.getAllItems().size();

                SynergyV5ListItem v5ListItem =
                        new SynergyV5ListItem(sli.getItem());

                v5List.add(position, v5ListItem);
            }

            v5List.sync(statusEnabledActivity.getAsActivity(), db);
        }
    }
}
