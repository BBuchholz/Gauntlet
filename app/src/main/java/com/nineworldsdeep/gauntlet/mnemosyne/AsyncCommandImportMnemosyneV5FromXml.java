package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

public class AsyncCommandImportMnemosyneV5FromXml extends AsyncCommand {

    public AsyncCommandImportMnemosyneV5FromXml(IStatusActivity statusActivity){
        super(statusActivity, "Import Mnemosyne V5 from Xml");

    }

    @Override
    public void executeCommand() {

        AsyncOperationImportMnemosyneV5FromXml op =
                new AsyncOperationImportMnemosyneV5FromXml(statusActivity);

        op.executeAsync();
    }
}
