package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

public class AsyncCommandHiveExportMnemosyneV5ToXml extends AsyncCommand {

    public AsyncCommandHiveExportMnemosyneV5ToXml(IStatusActivity statusEnabledActivity) {
        super(statusEnabledActivity, "Export Mnemosyne V5 to Hive XML");
    }

    @Override
    public void executeCommand() {

        AsyncOperationHiveExportMnemosyneV5ToXml op =
                new AsyncOperationHiveExportMnemosyneV5ToXml(statusActivity);
        op.executeAsync();
    }
}
