package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

public class AsyncCommandImportMnemosyneV3toV4 extends AsyncCommand {

    public AsyncCommandImportMnemosyneV3toV4(IStatusActivity statusEnabledActivity) {
        super(statusEnabledActivity, "Import Hash Tag Index");
    }

    @Override
    public void executeCommand() {

        AsyncOperationImportMnemosyneV3toV4 op =
                new AsyncOperationImportMnemosyneV3toV4(statusActivity);
        op.executeAsync();
    }
}
