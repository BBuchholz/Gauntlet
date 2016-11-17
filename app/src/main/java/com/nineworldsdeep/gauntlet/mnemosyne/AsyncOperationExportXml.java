package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

/**
 * Created by brent on 10/5/16.
 */
public class AsyncOperationExportXml extends AsyncOperation {

    public AsyncOperationExportXml(IStatusActivity statusActivity) {
        super(statusActivity, "Exporting XML");
    }

    @Override
    protected void runOperation() {

        //export xml here
    }
}
