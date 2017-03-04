package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

public class AsyncCommandImportMnemosyneV4toV5 extends AsyncCommand {

    public AsyncCommandImportMnemosyneV4toV5(IStatusActivity statusEnabledActivity) {
        super(statusEnabledActivity, "Import Mnemosyne V4");
    }

    @Override
    public void executeCommand() {

        AsyncOperationImportMnemosyneV4 op =
                new AsyncOperationImportMnemosyneV4(statusActivity);
        op.executeAsync();
    }
}
