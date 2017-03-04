package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;
import com.nineworldsdeep.gauntlet.model.FileNode;
import com.nineworldsdeep.gauntlet.model.LocalConfigNode;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.xml.Xml;

import java.io.File;
import java.util.List;


public class AsyncOperationExportDb extends AsyncOperation {

    public AsyncOperationExportDb(IStatusActivity statusActivity) {
        super(statusActivity, "Exporting DB");
    }

    @Override
    protected void runOperation() {

        Context ctx = statusEnabledActivity.getAsActivity();

        try {

            NwdDb db = NwdDb.getInstance(ctx);
            List<LocalConfigNode> cfg = db.getLocalConfig(ctx);
            List<FileNode> files = db.getFiles(ctx);
            File destination =
                    Configuration.getOutgoingXmlFile_yyyyMMddHHmmss("nwd");

            Xml.exportFromDb(ctx, cfg, files, destination);

            publishProgress("exported to: " +
                    destination.getAbsolutePath());

        } catch(Exception ex) {

            publishProgress("Error exporting db to xml: " +
                    ex.getMessage());
        }
    }
}
