package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.MultiMapString;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusEnabledActivity;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brent on 10/5/16.
 */
public class AsyncOperationImportHashTagIndex extends AsyncOperation {

    public AsyncOperationImportHashTagIndex(IStatusEnabledActivity statusActivity) {
        super(statusActivity, "Importing Hash Tag Indexes");
    }

    @Override
    protected void runOperation() {

        NwdDb db = NwdDb.getInstance(statusEnabledActivity.getAsActivity());
        db.open();

        // get all paths, recursive, in audio, images, screenshots,
        // downloads, and voicememos
        Collection<File> audioFolderFiles =
                FileUtils.listFiles(Configuration.getAudioDirectory(),
                        TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

        Collection<File> imageFolderFiles =
                FileUtils.listFiles(Configuration.getImagesDirectory(),
                        TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

        Collection<File> downloadFolderFiles =
                FileUtils.listFiles(Configuration.getDownloadDirectory(),
                        TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

        Collection<File> screenshotFolderFiles =
                FileUtils.listFiles(Configuration.getScreenshotDirectory(),
                        TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

        Collection<File> voicememosFolderFiles =
                FileUtils.listFiles(Configuration.getVoicememosDirectory(),
                        TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

        ArrayList<File> allCurrentFiles = new ArrayList<>();

        allCurrentFiles.addAll(audioFolderFiles);
        allCurrentFiles.addAll(imageFolderFiles);
        allCurrentFiles.addAll(downloadFolderFiles);
        allCurrentFiles.addAll(screenshotFolderFiles);
        allCurrentFiles.addAll(voicememosFolderFiles);

        FileHashIndexFile fhi = new FileHashIndexFile();
        fhi.loadItems();

        HashMap<String, String> pathToHash = fhi.getPathToHashMap();

        // for each path in the above list, if a hash does not already
        // exist, hash the path and add to loaded map
        for(File f : allCurrentFiles){

            if(!pathToHash.containsKey(f.getAbsolutePath())){

                try{
                    String hash =
                            Utils.computeSHA1(f.getAbsolutePath()).toLowerCase();

                    pathToHash.put(f.getAbsolutePath(), hash);

                }catch (Exception ex){

                    //skip
                }
            }
        }

        MultiMapString hashToTags = new MultiMapString();
        ArrayList<File> toBeConsumed = new ArrayList<>();

        for(File f : FileUtils.listFiles(
                Configuration.getConfigDirectory(),
                TrueFileFilter.INSTANCE,
                TrueFileFilter.INSTANCE)){

            String baseName = FilenameUtils.getBaseName(f.getAbsolutePath());

            if(baseName.endsWith("HashToTagsIndex")){

                if(baseName.endsWith("-HashToTagsIndex")){

                    //its a timestamped file, mark for consumption
                    toBeConsumed.add(f);
                }

                HashToTagsIndexFile htf = new HashToTagsIndexFile(baseName);
                htf.loadItems();

                List<HashToTagStringFragment> frags =
                    htf.getHashToTagStringFragments();

                for(HashToTagStringFragment frg : frags){

                    String thisHash = frg.getHash().toLowerCase();
                    String thisTagString = frg.getTagString();

                    hashToTags.putCommaStringValues(thisHash, thisTagString);
                }
            }
        }

        MultiMapString pathToTags = new MultiMapString();

        for(String path : pathToHash.keySet()){

            String hash = pathToHash.get(path).toLowerCase();

            if(hashToTags.containsKey(hash)){

                pathToTags.put(path, hashToTags.get(hash));
            }
        }

        db.linkTagsToFile(pathToTags);

        //consume imported
        for(File f : toBeConsumed){

            f.delete();
        }
    }
}
