package com.nineworldsdeep.gauntlet.archivist.v5.async;

import android.content.Context;
import android.util.Log;

import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSource;
import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.xml.Xml;
import com.nineworldsdeep.gauntlet.xml.XmlImporter;
import com.nineworldsdeep.gauntlet.xml.archivist.ArchivistXmlSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AsyncOperationHiveImportArchivistV5FromXml extends AsyncOperation {

    public AsyncOperationHiveImportArchivistV5FromXml(IStatusResponsive statusActivity) {
        super(statusActivity, "Importing Mnemosyne V5 from Hive XML");
    }

    @Override
    protected void runOperation() {

        Context ctx = statusEnabledActivity.getAsActivity();
        NwdDb db = NwdDb.getInstance(ctx);
        db.open();

        List<File> archivistV5XmlFiles =
                Configuration.getIncomingHiveXmlFilesBySuffix(
                        ctx,
                        db,
                        Xml.FILE_NAME_ARCHIVIST_V5);

        int fileCount = 0;
        int fileTotal = archivistV5XmlFiles.size();

        for(File f : archivistV5XmlFiles){

            fileCount++;

            ArrayList<ArchivistXmlSource> v5Sources = new ArrayList<>();

            try {

                XmlImporter xi = Xml.getImporter(f);
                v5Sources = xi.getArchivistV5Sources();

            } catch (Exception e) {

                e.printStackTrace();
            }

            try {

                db.beginTransaction();

                int count = 0;
                int total = v5Sources.size();

                for(ArchivistXmlSource axs : v5Sources) {

                    count++;

                    publishProgress("File " +
                            fileCount + " of " + fileTotal +
                            " -> saving source " +
                            count + " of " + total +
                            " [source type: " + axs.getSourceType() + "]");

                    db.save(axs, db.getSqliteDatabase());
                }

                db.setTransactionSuccessful();


            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                db.endTransaction();
            }

        }

        for(File f : archivistV5XmlFiles){

            try {

                publishProgress("just testing, no files deleted");

            }catch (Exception ex){

                Log.e("ImportArchivistXml", Log.getStackTraceString(ex));

            }

            boolean successful = f.delete();

            if(!successful){

                publishProgress(("error deleting file: " +
                        f.getAbsolutePath()));
            }

        }
    }
}
