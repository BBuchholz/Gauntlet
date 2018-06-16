package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Context;
import android.util.Log;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;

public class AsyncOperationArchivistPurgeSource extends AsyncOperation {

    ArchivistSource source;

    public AsyncOperationArchivistPurgeSource(IStatusResponsive statusActivity, ArchivistSource source) {
        super(statusActivity, "Purging Source");

        this.source = source;
    }

    @Override
    protected void runOperation() {

        Context ctx = statusEnabledActivity.getAsActivity();
        NwdDb db = NwdDb.getInstance(ctx);
        db.open();


        publishProgress("retrieving source excerpts...");


        ArrayList<ArchivistSourceExcerpt> sourceExcerpts =
                db.getArchivistSourceExcerptsForSourceId(ctx, source.getSourceId());

        try {

            db.beginTransaction();

            int excerptCount = 0;
            int excerptTotal = sourceExcerpts.size();

            String purgeMsg;
            String msg;

            for(ArchivistSourceExcerpt ase : sourceExcerpts) {

                excerptCount++;

                purgeMsg = "processing source excerpt " + excerptCount + " of " + excerptTotal;

                msg = purgeMsg + ": purging annotations...";
                publishProgress(msg);
                db.deleteArchivistSourceExcerptAnnotationsByExcerptId(ase.getExcerptId());

                msg = purgeMsg + ": purging tags...";
                publishProgress(msg);
                db.deleteArchivistSourceExcerptTaggingsByExcerptId(ase.getExcerptId());
            }

            publishProgress("purging source excerpts...");
            db.deleteArchivistSourceExcerptsBySourceId(source.getSourceId());


            publishProgress("purging source location entries...");
            db.deleteArchivistSourceLocationSubsetEntriesBySourceId(source.getSourceId());


            publishProgress("purging source...");
            db.deleteArchivistSourceBySourceId(source.getSourceId());

            db.setTransactionSuccessful();

        } catch (Exception ex) {

            Log.e("ArchivistPurgeSource", Log.getStackTraceString(ex));

        } finally {

            db.endTransaction();
        }
    }
}
