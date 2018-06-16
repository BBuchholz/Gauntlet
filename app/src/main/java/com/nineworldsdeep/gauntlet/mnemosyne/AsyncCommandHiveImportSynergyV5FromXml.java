package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;

public class AsyncCommandHiveImportSynergyV5FromXml extends AsyncCommand {

    public AsyncCommandHiveImportSynergyV5FromXml(IStatusResponsive statusActivity){
        super(statusActivity, "Import Synergy V5 from Hive Xml");

    }

    @Override
    public void executeCommand() {

        AsyncOperationHiveImportSynergyV5FromXml op =
                new AsyncOperationHiveImportSynergyV5FromXml(statusActivity);

        op.executeAsync();
    }
}
