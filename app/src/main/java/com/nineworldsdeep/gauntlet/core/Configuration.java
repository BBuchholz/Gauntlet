package com.nineworldsdeep.gauntlet.core;

import android.os.Environment;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyUtils;

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
    private static boolean _deleteDatabaseForDevelopment = false;
    private static boolean _shuffleFragments = false;

    public static boolean isInTestMode() {
        return _testMode;
    }

    public static void setTestMode(boolean testMode) {
        _testMode = testMode;
    }

    public static boolean isInDeleteDatabaseForDevelopmentMode(){

        //TODO: disable for release version
        return _deleteDatabaseForDevelopment;
        //return false;
    }

    public static void setDeleteDatabaseForDevelopment(boolean deleteDbForDev){

        //TODO: disable for release version
        _deleteDatabaseForDevelopment = deleteDbForDev;
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

        //test mode deactivated as we need config variables for other tests
        return getDirectoryStoragePath("/NWD/config", false);
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

        //TODO: this need to become /NWD-MEDIA/music, but we need
        //logic to migrate all metadata before we change
        //the configuration
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

        return getDirectoryStoragePath(path, true);
    }

    private static File getDirectoryStoragePath(String path,
                                                boolean testModeSupported){

        //TODO: replace with getDirectoryStoragePath(_testMode, path)
        // when we have time to run the debugger and test it

        if(_testMode && testModeSupported){
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

    /**
     * returns the absolute path for an external
     * sqlite database, with the supplied name.
     * The name can be given with or without
     * the ".db" suffix.
     * @param name
     * @return
     */
    public static File getSqliteDb(String name) {

        File nwdSqliteFolder = getDirectoryStoragePath("/NWD/sqlite");

        String dbFilePath = nwdSqliteFolder.getAbsolutePath() + File.separator + name;

        if (!dbFilePath.endsWith(".db"))
        {
            dbFilePath += ".db" ;
        }

        return new File(dbFilePath);
    }

    private static File getDirectoryStoragePath(boolean sandbox, String path){

        if(!path.startsWith("/")){

            path = "/" + path;
        }

        if(sandbox){
            path = "/NWD-SNDBX" + path;
        }

        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    public static File getSandboxSubFolder(String folderName) {

        return getDirectoryStoragePath(true, folderName);
    }

    /**
     * returns a sandbox file object in the specified
     * sandbox folder. If the fileName includes a suffix
     * it will be preserved. If not, a .txt file will
     * be assumed and returned.
     * @param folderName
     * @param fileName
     * @return
     */
    public static File getSandboxFile(String folderName, String fileName) {

        if(Utils.stringIsNullOrWhitespace(
                FilenameUtils.getExtension(fileName))){

            fileName = fileName + ".txt";
        }

        File subFolder = getSandboxSubFolder(folderName);

        return new File(subFolder,fileName);
    }

    public static File getSdCardMediaPdfDirectory() {

        return getSdDirectoryStoragePath("/NWD-MEDIA/pdf");
    }

    public static File getPdfDirectory() {

        return getDirectoryStoragePath("/NWD-MEDIA/pdfs");
    }

    public static File getXmlFile_yyyyMMddHHmmss(String suffix) {

        String fileName = SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss()
                        + "-" + suffix + ".xml";

        if(isInTestMode()){

            fileName = "test-" + fileName;
        }

        return new File(getXmlDirectory(), fileName);
    }

    public static File getXmlDirectory() {

        return getDirectoryStoragePath("/NWD/xml");
    }

    public static boolean shuffleFragments() {

        return _shuffleFragments;
    }

    public static void toggleShuffleFragments() {

        _shuffleFragments = !_shuffleFragments;
    }
}
