package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

/**
 * Created by brent on 10/5/16.
 */
public class AsyncOperationImportXml extends AsyncOperation {

    public AsyncOperationImportXml(IStatusActivity statusActivity) {
        super(statusActivity, "Importing XML");
    }

    @Override
    protected void runOperation() {

        //import xml here
    }
}
