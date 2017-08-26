package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

public class AsyncCommandHiveExportSynergyV5ToXml extends AsyncCommand {

    public AsyncCommandHiveExportSynergyV5ToXml(IStatusActivity statusEnabledActivity) {
        super(statusEnabledActivity, "Export Synergy V5 to Hive XML");
    }

    @Override
    public void executeCommand() {

        AsyncOperationHiveExportSynergyV5ToXml op =
                new AsyncOperationHiveExportSynergyV5ToXml(statusActivity);
        op.executeAsync();
    }
}
