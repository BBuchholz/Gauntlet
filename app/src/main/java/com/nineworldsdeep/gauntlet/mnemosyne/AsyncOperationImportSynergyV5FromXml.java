package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5List;
import com.nineworldsdeep.gauntlet.xml.Xml;
import com.nineworldsdeep.gauntlet.xml.XmlImporter;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;

public class AsyncOperationImportSynergyV5FromXml extends AsyncOperation {

    public AsyncOperationImportSynergyV5FromXml(IStatusResponsive statusActivity) {
        super(statusActivity, "Importing Synergy V5 from XML");
    }

    @Override
    protected void runOperation() {

        Context ctx = statusEnabledActivity.getAsActivity();
        NwdDb db = NwdDb.getInstance(ctx);
        db.open();

        List<File> synergyV5XmlFiles =
                Configuration.getIncomingXmlFilesBySuffix(
                        Xml.FILE_NAME_SYNERGY_V5);

        for(File f : synergyV5XmlFiles){

            publishProgress("importing " +
                    FilenameUtils.getBaseName(f.getAbsolutePath()));

            try {

                XmlImporter xi = Xml.getImporter(f);

                List<SynergyV5List> v5Lists =
                        xi.getSynergyV5Lists();

                for(SynergyV5List lst : v5Lists){

                    publishProgress("saving list [" + lst.getListName() + "]");

                    lst.sync(ctx, db);
                }

            } catch (Exception ex) {

                publishProgress(ex.getMessage());
            }

        }

        for(File f : synergyV5XmlFiles){

            boolean successful = f.delete();

            if(!successful){

                publishProgress(("error deleting file: " +
                        f.getAbsolutePath()));
            }
        }

    }
}
