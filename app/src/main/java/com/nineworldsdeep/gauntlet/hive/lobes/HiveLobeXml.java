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

public class HiveLobeXml extends HiveLobe {


    public HiveLobeXml(HiveRoot hr) {
        super("xml", hr);
    }

    @Override
    public void collect() {

        File hiveRootXmlSubFolderPath =
                ConfigHive.getHiveRootXmlFolderPath(getHiveRoot());

        for(File filePath :
                FileUtils.listFiles(
                        hiveRootXmlSubFolderPath,
                        new String[]{ "xml" },
                        true)){

            add(new HiveSporeFilePath(filePath));
        }
    }
}
