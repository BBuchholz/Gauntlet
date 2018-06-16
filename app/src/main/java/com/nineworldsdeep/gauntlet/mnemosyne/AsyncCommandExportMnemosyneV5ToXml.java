package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;

public class AsyncCommandExportMnemosyneV5ToXml extends AsyncCommand {

    public AsyncCommandExportMnemosyneV5ToXml(IStatusResponsive statusEnabledActivity) {
        super(statusEnabledActivity, "Export Mnemosyne V5 to XML");
    }

    @Override
    public void executeCommand() {

        AsyncOperationExportMnemosyneV5ToXml op =
                new AsyncOperationExportMnemosyneV5ToXml(statusActivity);
        op.executeAsync();
    }
}
