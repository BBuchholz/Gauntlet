package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;
import com.nineworldsdeep.gauntlet.model.FileNode;
import com.nineworldsdeep.gauntlet.model.LocalConfigNode;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.xml.Xml;

import java.io.File;
import java.util.List;


public class AsyncOperationImportMnemosyneV4 extends AsyncOperation {

    public AsyncOperationImportMnemosyneV4(IStatusActivity statusActivity) {
        super(statusActivity, "Importing Mnemosyne V4");
    }

    @Override
    protected void runOperation() {

        Context ctx = statusEnabledActivity.getAsActivity();

        try {




            publishProgress("import logic in progress.");

        } catch(Exception ex) {

            publishProgress("Error importing Mnemosyne V4 to V5: " +
                    ex.getMessage());
        }
    }
}
