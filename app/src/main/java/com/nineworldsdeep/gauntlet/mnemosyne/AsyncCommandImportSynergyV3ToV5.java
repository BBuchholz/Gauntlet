package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

/**
 * Created by brent on 11/16/16.
 */
public class AsyncCommandImportSynergyV3ToV5 extends AsyncCommand {
    public AsyncCommandImportSynergyV3ToV5(IStatusActivity statusEnabledActivity) {
        super(statusEnabledActivity, "Import Synergy V3 to V5");
    }

    @Override
    public void executeCommand() {

        AsyncOperationImportSynergyV3ToV5 op =
                new AsyncOperationImportSynergyV3ToV5(statusActivity);

        op.executeAsync();
    }
}
