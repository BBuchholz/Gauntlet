package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.AsyncOperationCommand;
import com.nineworldsdeep.gauntlet.core.IStatusEnabledActivity;

/**
 * Created by brent on 10/5/16.
 */
public class AsynExportXmlCommand extends AsyncOperationCommand {

    public AsynExportXmlCommand(IStatusEnabledActivity statusActivity) {
        super(statusActivity, "Exporting XML", "Export XML");
    }


    @Override
    protected void runOperation() {

        this.statusEnabledActivity.updateStatus(operationVerb + " here");
    }
}
