package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.AsyncOperationCommand;
import com.nineworldsdeep.gauntlet.core.IStatusEnabledActivity;

/**
 * Created by brent on 10/5/16.
 */
public class AsyncImportXmlCommand extends AsyncOperationCommand {

    public AsyncImportXmlCommand(IStatusEnabledActivity statusActivity) {
        super(statusActivity, "Importing XML", "Import XML");
    }

    @Override
    protected void runOperation() {

        this.statusEnabledActivity.updateStatus(operationVerb + " here");
    }
}
