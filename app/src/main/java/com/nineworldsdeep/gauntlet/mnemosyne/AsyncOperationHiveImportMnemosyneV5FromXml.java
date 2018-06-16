package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;
import android.util.Log;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.xml.Xml;
import com.nineworldsdeep.gauntlet.xml.XmlImporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AsyncOperationHiveImportMnemosyneV5FromXml extends AsyncOperation {

    public AsyncOperationHiveImportMnemosyneV5FromXml(IStatusResponsive statusActivity) {
        super(statusActivity, "Importing Mnemosyne V5 from Hive XML");
    }

    @Override
    protected void runOperation() {

        Context ctx = statusEnabledActivity.getAsActivity();
        NwdDb db = NwdDb.getInstance(ctx);
        db.open();

        List<File> mnemosyneV5XmlFiles =
                Configuration.getIncomingHiveXmlFilesBySuffix(
                        ctx,
                        db,
                        Xml.FILE_NAME_MNEMOSYNE_V5);

        int fileCount = 0;
        int fileTotal = mnemosyneV5XmlFiles.size();

        for(File f : mnemosyneV5XmlFiles){

            fileCount++;

//            publishProgress("importing " +
//                    FilenameUtils.getBaseName(f.getAbsolutePath()));


            ArrayList<Media> v5Media = new ArrayList<>();

            try {

                XmlImporter xi = Xml.getImporter(f);
                v5Media = xi.getMnemosyneV5Media();

            } catch (Exception e) {

                Log.e("ImportMnemosyneXml", Log.getStackTraceString(e));
            }

            try {

                db.beginTransaction();

                int count = 0;
                int total = v5Media.size();

                for(Media media : v5Media) {

                    count++;

                    publishProgress("File " +
                            fileCount + " of " + fileTotal +
                            " -> saving media " +
                            count + " of " + total +
                            " [" + media.getMediaHash() + "]");

                    db.sync(media, db.getSqliteDatabase());
                }

                db.setTransactionSuccessful();


            } catch (Exception ex) {

                Log.e("ImportMnemosyneXml", Log.getStackTraceString(ex));

            } finally {

                db.endTransaction();
            }

        }

        for(File f : mnemosyneV5XmlFiles){

            boolean successful = f.delete();

            if(!successful){

                publishProgress(("error deleting file: " +
                        f.getAbsolutePath()));
            }
        }

    }
}
