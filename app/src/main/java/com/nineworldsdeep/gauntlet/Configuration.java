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

    public static File getSynergyDirectory(){

        return getDirectoryStoragePath("/NWD/synergy");
    }

    public static File getArchiveDirectory() {

        return getDirectoryStoragePath("/NWD/synergy/archived");
    }

    public static File getTemplateDirectory() {

        return getDirectoryStoragePath("/NWD/synergy/templates");
    }

    public static File getMuseSessionNotesDirectory() {

        return getDirectoryStoragePath("/NWD/muse/sessions");
    }

    public static File getBookSegmentsDirectory() {

        return getDirectoryStoragePath("/NWD/bookSegments");
    }

    public static File getConfigDirectory() {

        return getDirectoryStoragePath("/NWD/config");
    }

    public static File getImagesDirectory() {

        return getDirectoryStoragePath("/NWD-MEDIA/images");
    }

    public static File getAudioDirectory() {

        return getDirectoryStoragePath("/NWD-MEDIA/audio");
    }

    public static List<String> getAllSynergyListNames(){

        File dir = getSynergyDirectory();
        return getAllListNamesForDirectory(dir);
    }

    public static List<String> getAllTemplateListNames(){

        File dir = getTemplateDirectory();
        return getAllListNamesForDirectory(dir);
    }

    public static List<String> getAllArchiveListNames(){

        File dir = getArchiveDirectory();
        return getAllListNamesForDirectory(dir);
    }

    private static List<String> getAllListNamesForDirectory(File dir){

        String[] exts = {"txt"};
        List<String> lst = new ArrayList<String>();

        for (File f : FileUtils.listFiles(dir, exts, false)){
            lst.add(FilenameUtils.removeExtension(f.getName()));
        }

        Collections.sort(lst);

        return lst;
    }

    private static File getDirectoryStoragePath(String path){

        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

}
