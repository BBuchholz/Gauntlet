package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;

public class AsyncCommandHiveImportMnemosyneV5FromXml extends AsyncCommand {

    public AsyncCommandHiveImportMnemosyneV5FromXml(IStatusActivity statusActivity){
        super(statusActivity, "Import Mnemosyne V5 from Hive Xml");

    }

    @Override
    public void executeCommand() {

        AsyncOperationHiveImportMnemosyneV5FromXml op =
                new AsyncOperationHiveImportMnemosyneV5FromXml(statusActivity);

        op.executeAsync();
    }
}
