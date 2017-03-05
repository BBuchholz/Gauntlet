package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.MultiMapString;
import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.v4.PathTagLink;
import com.nineworldsdeep.gauntlet.model.FileNode;
import com.nineworldsdeep.gauntlet.model.LocalConfigNode;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.xml.Xml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class AsyncOperationImportMnemosyneV4toV5 extends AsyncOperation {

    public AsyncOperationImportMnemosyneV4toV5(IStatusActivity statusActivity) {
        super(statusActivity, "Importing Mnemosyne V4");
    }

    @Override
    protected void runOperation() {

        Context ctx = statusEnabledActivity.getAsActivity();
        NwdDb db = NwdDb.getInstance(ctx);
        db.open();

        try {

            // mirrors NWD Desktop:
            // Tapestry.NodeUI.MediaMasterDisplay.btnImportV4TagsToV5_Click()

            publishProgress("retrieving PathTagLinks...");

            ArrayList<PathTagLink> pathTagLinks = db.getV4PathTagLinks();

            int pathTagLinksCount = 0;
            int pathTagLinksTotal = pathTagLinks.size();

            MultiMapString pathToTags = new MultiMapString();
            HashSet<String> allTags = new HashSet<>();

            for(PathTagLink link : pathTagLinks)
            {
                pathTagLinksCount++;

                publishProgress("Sorting links: processing link " +
                    pathTagLinksCount + " of " + pathTagLinksTotal);

                pathToTags.put(link.getPathValue(), link.getTagValue());
                allTags.add(link.getTagValue());
            }

            publishProgress("Ensuring media tags...");
            db.ensureMediaTags(allTags);
//
//                StatusDetailUpdate("Indexing media tags...");
//                Dictionary<string, Tag> tagsByTagValue = db.GetAllMediaTags();
//
//                Dictionary<string, string> pathToHash =
//                    new Dictionary<string, string>();
//
//                int localMediaDeviceId = Configuration.DB.MediaSubset.LocalDeviceId;
//
//                int pathCountTotal = pathToTags.Keys.Count();
//                int pathCountCurrent = 0;
//
//                foreach(var path in pathToTags.Keys)
//                {
//                    pathCountCurrent++;
//
//                    StatusDetailUpdate("hashing and storing path " + pathCountCurrent + " of " + pathCountTotal + ": " + path);
//                    string hash = Hashes.Sha1ForFilePath(path);
//                    pathToHash.Add(path, hash);
//                    db.StoreHashForPath(localMediaDeviceId, path, hash);
//                }
//
//                StatusDetailUpdate("indexing media by hash...");
//                Dictionary<string, Media> hashToMedia =
//                    db.GetAllMedia();
//
//                List<MediaTagging> taggings = new List<MediaTagging>();
//
//                pathCountTotal = pathToTags.Keys.Count();
//                pathCountCurrent = 0;
//
//                foreach(string path in pathToTags.Keys)
//                {
//                    pathCountCurrent++;
//
//                    StatusDetailUpdate("processing tags for path " + pathCountCurrent + " of " + pathCountTotal + ": " + path);
//
//                    if (pathToHash.ContainsKey(path))
//                    {
//                        string pathHash = pathToHash[path];
//
//                        if (hashToMedia.ContainsKey(pathHash))
//                        {
//                            int mediaId = hashToMedia[pathHash].MediaId;
//
//                            foreach (string tag in pathToTags[path])
//                            {
//                                if (tagsByTagValue.ContainsKey(tag))
//                                {
//                                    int mediaTagId = tagsByTagValue[tag].TagId;
//
//                                    taggings.Add(new MediaTagging
//                                    {
//                                        MediaId = mediaId,
//                                        MediaTagId = mediaTagId
//                                    });
//                                }
//                            }
//                        }
//
//                    }
//                }
//
//                StatusDetailUpdate("ensuring media taggings in db...");
//
//                db.EnsureMediaTaggings(taggings);

            publishProgress("import logic in progress.");

        } catch(Exception ex) {

            publishProgress("Error importing Mnemosyne V4 to V5: " +
                    ex.getMessage());
        }
    }
}
