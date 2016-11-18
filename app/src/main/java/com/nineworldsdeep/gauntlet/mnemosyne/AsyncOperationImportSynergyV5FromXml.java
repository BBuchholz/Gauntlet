package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5List;
import com.nineworldsdeep.gauntlet.xml.Xml;
import com.nineworldsdeep.gauntlet.xml.XmlImporter;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by brent on 10/5/16.
 */
public class AsyncOperationImportSynergyV5FromXml extends AsyncOperation {

    public AsyncOperationImportSynergyV5FromXml(IStatusActivity statusActivity) {
        super(statusActivity, "Importing SynergyV5 from XML");
    }

    @Override
    protected void runOperation() {

        Context ctx = statusEnabledActivity.getAsActivity();
        NwdDb db = NwdDb.getInstance(ctx);
        db.open();

        List<File> synergyV5XmlFiles =
                Configuration.getIncomingXmlFilesBySuffix("nwd-synergy-v5");

        for(File f : synergyV5XmlFiles){

            publishProgress("importing " +
                    FilenameUtils.getBaseName(f.getAbsolutePath()));

            try {

                XmlImporter xi = Xml.getImporter(f);

                List<SynergyV5List> v5Lists =
                        xi.getSynergyV5Lists();

                for(SynergyV5List lst : v5Lists){

                    publishProgress("saving list [" + lst.getListName() + "]");

                    lst.save(ctx, db);
                }

            } catch (Exception ex) {

                publishProgress(ex.getMessage());
            }

        }

    }
}
