package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusEnabledActivity;

/**
 * Created by brent on 10/8/16.
 */
public class AsyncCommandExportDb extends AsyncCommand {

    public AsyncCommandExportDb(IStatusEnabledActivity statusEnabledActivity) {
        super(statusEnabledActivity, "Export DB");
    }

    @Override
    public void executeCommand() {

        AsyncOperationExportDb op = new AsyncOperationExportDb(statusActivity);
        op.executeAsync();
    }
}
