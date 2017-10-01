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

public class HiveLobeAudio extends HiveLobe {


    public HiveLobeAudio(HiveRoot hr) {
        super("audio", hr);
    }

    @Override
    public void collect() {

//        File hiveRootAudioSubFolderPath =
//                getAssociatedDirectory();
//
//        for(File filePath :
//                FileUtils.listFiles(
//                        hiveRootAudioSubFolderPath,
//                        ConfigHive.AUDIO_EXTENSIONS,
//                        true)){
//
//            add(new HiveSporeFilePath(filePath));
//        }

        for(File filePath :
                filterDirectoryFiles(
                        getAssociatedDirectory(),
                        ConfigHive.AUDIO_EXTENSIONS)){

            add(new HiveSporeFilePath(filePath));
        }
    }

    @Override
    public File getAssociatedDirectory() {

        return ConfigHive.getHiveRootAudioFolderPath(getHiveRoot());
    }

    @Override
    public Iterable<File> siftFiles(HiveRoot hiveRoot) {

        return siftFiles(hiveRoot, ConfigHive.AUDIO_EXTENSIONS);
    }
}
