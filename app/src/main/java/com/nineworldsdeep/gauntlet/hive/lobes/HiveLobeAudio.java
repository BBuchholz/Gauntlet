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

        File hiveRootAudioSubFolderPath =
                ConfigHive.getHiveRootAudioFolderPath(getHiveRoot());

        for(File filePath :
                FileUtils.listFiles(
                        hiveRootAudioSubFolderPath,
                        ConfigHive.AUDIO_EXTENSIONS,
                        true)){

            add(new HiveSporeFilePath(filePath));
        }
    }
}
