package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

/**
 * Created by brent on 10/8/16.
 */
public class AsynCommandExportSynergyV5ToXml extends AsyncCommand {

    public AsynCommandExportSynergyV5ToXml(IStatusActivity statusEnabledActivity) {
        super(statusEnabledActivity, "Export Synergy V5 to XML");
    }

    @Override
    public void executeCommand() {

        AsyncOperationExportSynergyV5ToXml op =
                new AsyncOperationExportSynergyV5ToXml(statusActivity);
        op.executeAsync();
    }
}
