package com.nineworldsdeep.gauntlet.hive.lobes;

import com.nineworldsdeep.gauntlet.hive.ConfigHive;
import com.nineworldsdeep.gauntlet.hive.HiveLobe;
import com.nineworldsdeep.gauntlet.hive.HiveRoot;
import com.nineworldsdeep.gauntlet.hive.spores.HiveSporeFilePath;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by brent on 9/24/17.
 */

public class HiveLobeImages extends HiveLobe {


    public HiveLobeImages(HiveRoot hr) {
        super("images", hr);
    }

    @Override
    public void collect() {

//        File hiveRootImagesSubFolderPath =
//                getAssociatedDirectory();
//
//        for(File filePath :
//                FileUtils.listFiles(
//                        hiveRootImagesSubFolderPath,
//                        ConfigHive.IMAGE_EXTENSIONS,
//                        true)){
//
//            add(new HiveSporeFilePath(filePath));
//        }

        for(File filePath :
                filterDirectoryFiles(
                        getAssociatedDirectory(),
                        ConfigHive.IMAGE_EXTENSIONS)){

            add(new HiveSporeFilePath(filePath));
        }
    }

    @Override
    public File getAssociatedDirectory() {

        return ConfigHive.getHiveRootImagesFolderPath(getHiveRoot());
    }

    @Override
    public Iterable<File> siftFiles(HiveRoot hiveRoot) {

        return siftFiles(hiveRoot, ConfigHive.IMAGE_EXTENSIONS);
    }
}


