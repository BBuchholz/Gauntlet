package com.nineworldsdeep.gauntlet.mnemosyne;

import android.app.Activity;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.IStatusEnabledActivity;

/**
 * Created by brent on 10/5/16.
 */
public class AsyncOperationExportDb extends AsyncOperation {

    public AsyncOperationExportDb(IStatusEnabledActivity statusActivity) {
        super(statusActivity, "Exporting DB");
    }

    @Override
    protected void runOperation() {

        //export db here
    }
}
