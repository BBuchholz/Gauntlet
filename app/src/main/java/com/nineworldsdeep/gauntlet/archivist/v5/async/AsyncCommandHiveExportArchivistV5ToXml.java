package com.nineworldsdeep.gauntlet.archivist.v5.async;

import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;

public class AsyncCommandHiveExportArchivistV5ToXml extends AsyncCommand {

    public AsyncCommandHiveExportArchivistV5ToXml(IStatusResponsive statusEnabledActivity) {
        super(statusEnabledActivity, "Export Archivist V5 to Hive XML");
    }

    @Override
    public void executeCommand() {

        AsyncOperationHiveExportArchivistV5ToXml op =
                new AsyncOperationHiveExportArchivistV5ToXml(statusActivity);
        op.executeAsync();
    }
}
