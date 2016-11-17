package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

/**
 * Created by brent on 10/8/16.
 */
public class AsyncCommandImportXml extends AsyncCommand {

    public AsyncCommandImportXml(IStatusActivity statusActivity){
        super(statusActivity, "Import Xml");

    }

    @Override
    public void executeCommand() {

        AsyncOperationImportXml op = new AsyncOperationImportXml(statusActivity);
        op.executeAsync();
    }
}
