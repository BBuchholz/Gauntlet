package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

/**
 * Created by brent on 10/5/16.
 */
public class AsyncOperationExportDb extends AsyncOperation {

    public AsyncOperationExportDb(IStatusActivity statusActivity) {
        super(statusActivity, "Exporting DB");
    }

    @Override
    protected void runOperation() {

        //export db here
    }
}
