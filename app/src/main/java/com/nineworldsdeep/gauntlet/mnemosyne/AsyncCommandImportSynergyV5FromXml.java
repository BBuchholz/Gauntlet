package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

/**
 * Created by brent on 10/8/16.
 */
public class AsyncCommandImportSynergyV5FromXml extends AsyncCommand {

    public AsyncCommandImportSynergyV5FromXml(IStatusActivity statusActivity){
        super(statusActivity, "Import Synergy V5 from Xml");

    }

    @Override
    public void executeCommand() {

        AsyncOperationImportSynergyV5FromXml op =
                new AsyncOperationImportSynergyV5FromXml(statusActivity);

        op.executeAsync();
    }
}
