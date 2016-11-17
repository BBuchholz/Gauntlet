package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

/**
 * Created by brent on 10/8/16.
 */
public class AsyncCommandBridgeTables extends AsyncCommand {
    public AsyncCommandBridgeTables(IStatusActivity statusEnabledActivity) {
        super(statusEnabledActivity, "Bridge Tables");
    }

    @Override
    public void executeCommand() {

        AsyncOperationBridgeTables op = new AsyncOperationBridgeTables(statusActivity);
        op.executeAsync();
    }
}
