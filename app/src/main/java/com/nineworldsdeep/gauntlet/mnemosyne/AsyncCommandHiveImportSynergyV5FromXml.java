package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

public class AsyncCommandHiveImportSynergyV5FromXml extends AsyncCommand {

    public AsyncCommandHiveImportSynergyV5FromXml(IStatusActivity statusActivity){
        super(statusActivity, "Import Synergy V5 from Hive Xml");

    }

    @Override
    public void executeCommand() {

        AsyncOperationHiveImportSynergyV5FromXml op =
                new AsyncOperationHiveImportSynergyV5FromXml(statusActivity);

        op.executeAsync();
    }
}
