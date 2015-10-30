package com.nineworldsdeep.gauntlet;

import android.os.Environment;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by brent on 10/30/15.
 */
public class Configuration {

    public File getSynergyDirectory(){

        File root = Environment.getExternalStorageDirectory();
        File synergyDir = new File(root.getAbsolutePath() + "/NWD/synergy");
        if(!synergyDir.exists()){
            synergyDir.mkdirs();
        }
        return synergyDir;
    }

    public List<String> getAllSynergyListNames(){

        String[] exts = {"txt"};
        List<String> lst = new ArrayList<String>();

        for (File f : FileUtils.listFiles(getSynergyDirectory(), exts, false)){
            lst.add(FilenameUtils.removeExtension(f.getName()));
        }

        Collections.sort(lst);

        return lst;
    }
}
