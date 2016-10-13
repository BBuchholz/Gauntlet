package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusEnabledActivity;

/**
 * Created by brent on 10/8/16.
 */
public class AsynCommandImportHashTagIndex extends AsyncCommand {

    public AsynCommandImportHashTagIndex(IStatusEnabledActivity statusEnabledActivity) {
        super(statusEnabledActivity, "Export XML");
    }

    @Override
    public void executeCommand() {

        AsyncOperationExportXml op = new AsyncOperationExportXml(statusActivity);
        op.executeAsync();
    }
}
