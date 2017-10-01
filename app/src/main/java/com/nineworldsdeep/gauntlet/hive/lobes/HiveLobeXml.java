package com.nineworldsdeep.gauntlet.hive.lobes;

import com.nineworldsdeep.gauntlet.hive.ConfigHive;
import com.nineworldsdeep.gauntlet.hive.HiveLobe;
import com.nineworldsdeep.gauntlet.hive.HiveRoot;
import com.nineworldsdeep.gauntlet.hive.spores.HiveSporeFilePath;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by brent on 9/24/17.
 */

public class HiveLobeXml extends HiveLobe {

    public static final String[] FILE_EXTENSIONS = new String[]{"xml"};

    public HiveLobeXml(HiveRoot hr) {
        super("xml", hr);
    }

    @Override
    public void collect() {

        for(File filePath :
                filterDirectoryFiles(
                        getAssociatedDirectory(),
                        FILE_EXTENSIONS)){

            add(new HiveSporeFilePath(filePath));
        }
    }

    @Override
    public File getAssociatedDirectory() {

        return ConfigHive.getHiveRootXmlFolderPath(getHiveRoot());
    }

    @Override
    public Iterable<File> siftFiles(HiveRoot hiveRoot) {

        return siftFiles(hiveRoot, FILE_EXTENSIONS);
    }

}
