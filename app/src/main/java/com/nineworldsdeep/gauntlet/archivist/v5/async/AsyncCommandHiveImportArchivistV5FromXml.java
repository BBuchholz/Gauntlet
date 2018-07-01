package com.nineworldsdeep.gauntlet.archivist.v5.async;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;

public class AsyncCommandHiveImportArchivistV5FromXml extends AsyncCommand {

    public AsyncCommandHiveImportArchivistV5FromXml(IStatusResponsive statusActivity){
        super(statusActivity, "Import Archivist V5 from Hive Xml");

    }

    @Override
    public void executeCommand() {

        AsyncOperationHiveImportArchivistV5FromXml op =
                new AsyncOperationHiveImportArchivistV5FromXml(statusActivity);

        op.executeAsync();
    }
}
