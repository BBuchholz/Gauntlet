package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusEnabledActivity;

/**
 * Created by brent on 10/8/16.
 */
public class AsynCommandExportXml extends AsyncCommand {

    public AsynCommandExportXml(IStatusEnabledActivity statusEnabledActivity) {
        super(statusEnabledActivity, "Import Hash Tag Index");
    }

    @Override
    public void executeCommand() {

        AsyncOperationImportHashTagIndex op =
                new AsyncOperationImportHashTagIndex(statusActivity);
        op.executeAsync();
    }
}
