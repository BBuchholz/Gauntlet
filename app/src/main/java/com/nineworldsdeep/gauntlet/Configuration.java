package com.nineworldsdeep.gauntlet;

import android.os.Environment;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brent on 10/30/15.
 */
public class Configuration {

    private static boolean _testMode = false;

    public static boolean isInTestMode() {
        return _testMode;
    }

    public static void setTestMode(boolean testMode) {
        _testMode = testMode;
    }

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

    public static File getVoicememosDirectory(){

        return getDirectoryStoragePath("/NWD-AUX/voicememos");
    }

    public static File getSdCardMediaMusicDirectory(){

        return getSdDirectoryStoragePath("/NWD-MEDIA");
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

    private static File getSdDirectoryStoragePath(String path){

        if(_testMode){
            path = "/NWD-SNDBX" + path;
        }

        File root = new File("/storage/external_SD");
        File dir = new File(root.getAbsolutePath() + path);
        if(!dir.exists()){

            try{

                dir.mkdirs();

            }catch(Exception ex){

                return null;
            }
        }

        if(dir.exists()){

            return dir;

        }else{

            return null;
        }
    }

    private static File getDirectoryStoragePath(String path){

        if(_testMode){
            path = "/NWD-SNDBX" + path;
        }

        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    public static File getCameraDirectory() {

        return getDirectoryStoragePath("/DCIM/Camera");
    }

    public static File getScreenshotDirectory() {

        HashMap<String, String> dynamicVals =
                Utils.fromLineItemConfigFile("ConfigFile");

        String partialPath = "/Pictures/Screenshots";

        if(dynamicVals.containsKey("screenShotFolder")){

            partialPath = dynamicVals.get("screenShotFolder");
        }

        return getDirectoryStoragePath(partialPath);

//        return getDirectoryStoragePath("/Pictures/Screenshots");
    }

    public static File getSkitchDirectory() {

        return getDirectoryStoragePath("/Pictures/Skitch");
    }

    public static File getPlaylistsDirectory() {

        return getDirectoryStoragePath("/Playlists");
    }

    public static File getPlaylistFile(String playlistNameWithExt) {

        File f = new File(getPlaylistsDirectory() + "/" + playlistNameWithExt);
        return f;
    }

    public static File getDownloadDirectory() {

        return getDirectoryStoragePath("/Download");
    }

    public static File getTapestryDirectory() {

        return getDirectoryStoragePath("/NWD/tapestry");
    }

    public static File getSqliteDb(String name) {

        File nwdSqliteFolder = getDirectoryStoragePath("/NWD/sqlite");

        String dbFilePath = nwdSqliteFolder.getAbsolutePath() + File.separator + name;

        if (!dbFilePath.endsWith(".db"))
        {
            dbFilePath += ".db" ;
        }

        return new File(dbFilePath);
    }
}
