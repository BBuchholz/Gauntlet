package com.nineworldsdeep.gauntlet.mnemosyne;

import android.app.Activity;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.IStatusEnabledActivity;

/**
 * Created by brent on 10/5/16.
 */
public class AsyncOperationExportXml extends AsyncOperation {

    public AsyncOperationExportXml(IStatusEnabledActivity statusActivity) {
        super(statusActivity, "Exporting XML");
    }

    @Override
    protected void runOperation() {

        //export xml here
    }
}
