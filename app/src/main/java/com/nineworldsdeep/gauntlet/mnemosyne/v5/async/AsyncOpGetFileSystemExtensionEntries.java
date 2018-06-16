package com.nineworldsdeep.gauntlet.mnemosyne.v5.async;

import com.nineworldsdeep.gauntlet.MultiMapString;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.AsyncMultiMapStringOperation;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Collection;

/**
 * Created by brent on 2/6/17.
 */

public class AsyncOpGetFileSystemExtensionEntries
        extends AsyncMultiMapStringOperation {

    private File mediaRoot;

    public AsyncOpGetFileSystemExtensionEntries(IStatusResponsive statusActivity,
                                                File mediaRootDir) {
        super(statusActivity, "Retrieving Extension Entries");

        mediaRoot = mediaRootDir;
    }

    @Override
    protected MultiMapString runOperation() {

        MultiMapString extensionsToPaths = new MultiMapString();

        publishProgress("scanning...");

        Collection<File> allFiles =
                FileUtils.listFiles(mediaRoot, null, true);
        int count = 0;
        int total = allFiles.size();

        for(File f : allFiles){

            count++;

            if(count % 1 == 0){ //adjust to change update rate

                String msg = "processing " + count + " of " +
                        total + ": " + f.getAbsolutePath();

                publishProgress(msg);
            }

            String ext = FilenameUtils.getExtension(f.getName());

            if(Utils.stringIsNullOrWhitespace(ext)){

                ext = "[NULL]";
            }

            extensionsToPaths.put(ext, f.getAbsolutePath());
        }

        publishProgress("finished scanning " + total + " files.");

        return extensionsToPaths;
    }
}
