package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

public class AsyncCommandExportDb extends AsyncCommand {

    public AsyncCommandExportDb(IStatusActivity statusEnabledActivity) {
        super(statusEnabledActivity, "Export DB");
    }

    @Override
    public void executeCommand() {

        AsyncOperationExportDb op = new AsyncOperationExportDb(statusActivity);
        op.executeAsync();
    }
}
