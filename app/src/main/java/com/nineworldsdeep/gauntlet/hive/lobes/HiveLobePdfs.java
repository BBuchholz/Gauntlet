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

public class HiveLobePdfs extends HiveLobe {

    public static final String[] FILE_EXTENSIONS = new String[]{"pdf"};

    public HiveLobePdfs(HiveRoot hr) {
        super("pdfs", hr);
    }

    @Override
    public void collect() {

//        File hiveRootPdfsSubFolderPath =
//                getAssociatedDirectory();
//
//        for(File filePath :
//                FileUtils.listFiles(
//                        hiveRootPdfsSubFolderPath,
//                        new String[]{"pdf"},
//                        true)){
//
//            add(new HiveSporeFilePath(filePath));
//        }

        for(File filePath :
                filterDirectoryFiles(
                        getAssociatedDirectory(),
                        FILE_EXTENSIONS)){

            add(new HiveSporeFilePath(filePath));
        }
    }

    @Override
    public File getAssociatedDirectory() {

        return ConfigHive.getHiveRootPdfsFolderPath(getHiveRoot());
    }

    @Override
    public Iterable<File> siftFiles(HiveRoot hiveRoot) {

        return siftFiles(hiveRoot, FILE_EXTENSIONS);
    }
}
