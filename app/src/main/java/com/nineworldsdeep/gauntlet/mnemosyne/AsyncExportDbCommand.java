package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncOperationCommand;
import com.nineworldsdeep.gauntlet.core.IStatusEnabledActivity;

/**
 * Created by brent on 10/5/16.
 */
public class AsyncExportDbCommand extends AsyncOperationCommand {

    public AsyncExportDbCommand(IStatusEnabledActivity statusActivity) {
        super(statusActivity, "Exporting DB", "Export DB");
    }

    @Override
    protected void runOperation() {

        this.statusEnabledActivity.updateStatus(operationVerb + " here");
    }
}
