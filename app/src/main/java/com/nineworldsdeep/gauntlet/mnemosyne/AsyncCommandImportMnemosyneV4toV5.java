package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;

public class AsyncCommandImportMnemosyneV4toV5 extends AsyncCommand {

    public AsyncCommandImportMnemosyneV4toV5(IStatusResponsive statusEnabledActivity) {
        super(statusEnabledActivity, "Import Mnemosyne V4");
    }

    @Override
    public void executeCommand() {

        AsyncOperationImportMnemosyneV4toV5 op =
                new AsyncOperationImportMnemosyneV4toV5(statusActivity);
        op.executeAsync();
    }
}
