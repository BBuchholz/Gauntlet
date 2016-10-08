package com.nineworldsdeep.gauntlet.mnemosyne;

import android.app.Activity;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.IStatusEnabledActivity;

/**
 * Created by brent on 10/5/16.
 */
public class AsyncOperationBridgeTables extends AsyncOperation {

    public AsyncOperationBridgeTables(IStatusEnabledActivity statusActivity) {
        super(statusActivity, "Bridging Tables");
    }

    @Override
    protected void runOperation() {

        //bridge tables here
    }
}
