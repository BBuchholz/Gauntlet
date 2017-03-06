package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.MultiMapString;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.v4.PathTagLink;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaTagging;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Tag;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class AsyncOperationImportMnemosyneV4toV5 extends AsyncOperation {

    public AsyncOperationImportMnemosyneV4toV5(IStatusActivity statusActivity) {
        super(statusActivity, "Importing Mnemosyne V4");
    }

    @Override
    protected void runOperation() throws Exception {

        Context ctx = statusEnabledActivity.getAsActivity();
        NwdDb db = NwdDb.getInstance(ctx);
        db.open();


        // mirrors NWD Desktop:
        // Tapestry.NodeUI.MediaMasterDisplay.btnImportV4TagsToV5_Click()

        publishProgress("retrieving PathTagLinks...");

        ArrayList<PathTagLink> pathTagLinks = db.getV4PathTagLinksAsync();

        int pathTagLinksCount = 0;
        int pathTagLinksTotal = pathTagLinks.size();

        MultiMapString pathToTags = new MultiMapString();
        HashSet<String> allTags = new HashSet<>();

        for (PathTagLink link : pathTagLinks) {
            pathTagLinksCount++;

            publishProgress("Sorting links: processing link " +
                    pathTagLinksCount + " of " + pathTagLinksTotal);

            pathToTags.put(link.getPathValue(), link.getTagValue());
            allTags.add(link.getTagValue());
        }

        publishProgress("Ensuring media tags...");
        db.ensureMediaTags(allTags);

        publishProgress("Indexing media tags...");
        HashMap<String, Tag> tagsByTagValue = db.getAllMediaTags();

        HashMap<String, String> pathToHash =
                new HashMap<String, String>();

        int localMediaDeviceId =
                Configuration.getLocalMediaDevice(ctx, db).getMediaDeviceId();

        int pathCountTotal = pathToTags.keySet().size();
        int pathCountCurrent = 0;

        for (String path : pathToTags.keySet()) {
            pathCountCurrent++;

            publishProgress("hashing and storing path " + pathCountCurrent + " of " + pathCountTotal + ": " + path);
            String hash = Utils.computeSHA1(path);
            pathToHash.put(path, hash);
            db.storeHashForPath(localMediaDeviceId, path, hash);
        }

        publishProgress("indexing media by hash...");
        HashMap<String, Media> hashToMedia = db.getAllMedia();

        ArrayList<MediaTagging> taggings = new ArrayList<>();

        pathCountTotal = pathToTags.keySet().size();
        pathCountCurrent = 0;

        for (String path : pathToTags.keySet()) {
            pathCountCurrent++;

            publishProgress("processing tags for path " + pathCountCurrent + " of " + pathCountTotal + ": " + path);

            if (pathToHash.containsKey(path)) {
                String pathHash = pathToHash.get(path);

                if (hashToMedia.containsKey(pathHash)) {
                    int mediaId = hashToMedia.get(pathHash).getMediaId();

                    for (String tag : pathToTags.get(path)) {
                        if (tagsByTagValue.containsKey(tag)) {
                            int mediaTagId =
                                    tagsByTagValue.get(tag).getTagId();

                            MediaTagging mt = new MediaTagging();
                            mt.setMediaId(mediaId);
                            mt.setMediaTagId(mediaTagId);

                            taggings.add(mt);
                        }
                    }
                }

            }
        }

        publishProgress("ensuring media taggings in db...");

        db.ensureMediaTaggings(taggings);

        publishProgress("import logic in progress.");


    }
}
