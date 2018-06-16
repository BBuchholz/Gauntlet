package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;


public class AsyncOperationExportDb extends AsyncOperation {

    public AsyncOperationExportDb(IStatusResponsive statusActivity) {
        super(statusActivity, "Exporting DB");
    }

    @Override
    protected void runOperation() {

        Context ctx = statusEnabledActivity.getAsActivity();

        try {

            String outputPath = NwdDb.getInstance(ctx).export();

            publishProgress("exported to: " + outputPath);

        } catch(Exception ex) {

            publishProgress("Error exporting db: " +
                    ex.getMessage());
        }
    }
}
