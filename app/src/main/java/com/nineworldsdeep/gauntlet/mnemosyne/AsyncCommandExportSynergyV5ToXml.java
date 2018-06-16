package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;

public class AsyncCommandExportSynergyV5ToXml extends AsyncCommand {

    public AsyncCommandExportSynergyV5ToXml(IStatusResponsive statusEnabledActivity) {
        super(statusEnabledActivity, "Export Synergy V5 to XML");
    }

    @Override
    public void executeCommand() {

        AsyncOperationExportSynergyV5ToXml op =
                new AsyncOperationExportSynergyV5ToXml(statusActivity);
        op.executeAsync();
    }
}
