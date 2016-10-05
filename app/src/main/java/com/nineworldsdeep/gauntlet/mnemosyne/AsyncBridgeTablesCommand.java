package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncOperationCommand;
import com.nineworldsdeep.gauntlet.core.IStatusEnabledActivity;

/**
 * Created by brent on 10/5/16.
 */
public class AsyncBridgeTablesCommand extends AsyncOperationCommand {

    public AsyncBridgeTablesCommand(IStatusEnabledActivity statusActivity) {
        super(statusActivity, "Bridging Tables", "Bridge Tables");
    }

    @Override
    protected void runOperation() {

        this.statusEnabledActivity.updateStatus(operationVerb + " here");
    }
}
