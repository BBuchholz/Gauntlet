package com.nineworldsdeep.gauntlet.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.hive.HiveRoot;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaDevice;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyUtils;
import com.nineworldsdeep.gauntlet.tapestry.v1.TapestryUtils;

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
    private static boolean _shuffleV3Fragments = false;
    private static String _mostRecentMoveToList = "Fragments"; //default
    private static MediaDevice localMediaDevice;

    public static boolean isInTestMode() {
        return false;
    }

    public static void setTestMode(boolean testMode) {
        _testMode = testMode;
    }

    public static boolean isInDeleteDatabaseForDevelopmentMode(){

        return false;
    }

    public static void setDeleteDatabaseForDevelopment(boolean deleteDbForDev){

        //TODO: disable for release version
        _deleteDatabaseForDevelopment = deleteDbForDev;
    }

    public static void setMostRecentMoveToList(String listName){
        _mostRecentMoveToList = listName;
    }

    public static String getMostRecentMoveToList(){
        return _mostRecentMoveToList;
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

    public static File getMidiDirectory(){

        return getDirectoryStoragePath("/NWD-AUX/midi");
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

    public static File getCausticSongExportsDirectory(){

        return getDirectoryStoragePath("/caustic/songs/export");
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

    public static File getMemesDirectory() {

        return getDirectoryStoragePath("/NWD-MEDIA/images/memes");
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

    public static File getSyncFolder(){

        return getDirectoryStoragePath("/NWD-SYNC");
    }

    public static File getIncomingXmlFile_yyyyMMddHHmmss(String suffix) {

        String fileName = SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss()
                        + "-" + suffix + ".xml";

        if(isInTestMode()){

            fileName = "test-" + fileName;
        }

        return new File(getIncomingXmlDirectory(), fileName);
    }

    public static File getOutgoingXmlFile_yyyyMMddHHmmss(String suffix) {

        String fileName = SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss()
                        + "-" + suffix + ".xml";

        if(isInTestMode()){

            fileName = "test-" + fileName;
        }

        return new File(getOutgoingXmlDirectory(), fileName);
    }

    public static List<File> getIncomingXmlFilesBySuffix(String suffix){

        String[] exts = {"xml"};
        List<File> lst = new ArrayList<>();

        for(File f : FileUtils.listFiles(getIncomingXmlDirectory(), exts, false)){

            if(FilenameUtils.removeExtension(f.getName()).endsWith(suffix)){

                lst.add(f);
            }
        }

        return lst;
    }

    public static File getXmlDirectory() {

        return getDirectoryStoragePath("/NWD/xml");
    }

    public static File getIncomingXmlDirectory() {

        return getDirectoryStoragePath("/NWD/xml/incoming");
    }

    public static File getOutgoingXmlDirectory() {

        return getDirectoryStoragePath("/NWD/xml/outgoing");
    }

    public static boolean shuffleV3Fragments() {

        return _shuffleV3Fragments;
    }

    public static void toggleV3ShuffleFragments() {

        _shuffleV3Fragments = !_shuffleV3Fragments;
    }

    public static MediaDevice getLocalMediaDevice(Context c, NwdDb db) {

        //this will eventually get from the db
        //MediaDevice localMediaDevice = new MediaDevice();

        if(localMediaDevice == null){

            localMediaDevice = db.getLocalMediaDevice(c);
        }

        return localMediaDevice;
    }

    public static boolean localMediaDeviceIsSet(){

        return localMediaDevice != null;
    }

    public static String getLocalDeviceName(){

        if(localMediaDeviceIsSet()){

            return localMediaDevice.getMediaDeviceDescription();
        }

        //loads from config file
        return TapestryUtils.getCurrentDeviceName();
    }

    /**
     * does not check if set, use localMediaDeviceIsSet()
     * before using this method
     * @return
     */
    public static MediaDevice getLocalMediaDevice(){

        return localMediaDevice;
    }

    public static MediaDevice getLocalMediaDevice(NwdDb db) {

        if(localMediaDevice == null){

            localMediaDevice = db.getLocalMediaDevice();
        }

        return localMediaDevice;
    }

    public static void ensureLocalMediaDevice(
            Context c, NwdDb db, String localMediaDeviceName) {

        db.ensureLocalMediaDevice(c, localMediaDeviceName);
    }

    public static ArrayList<File> getHiveRootFolderPaths(HiveRoot hr) {

        ArrayList<File> folders = new ArrayList<>();

        for(String subFolderPath : getHiveRootSubFolderPaths()){

            File syncFolder = getSyncFolder();
            File hiveFolder = new File(syncFolder, "hive");
            File hiveRootFolder = new File(hiveFolder, hr.getHiveRootName());
            File subFolder = new File(hiveRootFolder, subFolderPath);

            folders.add(subFolder);
        }

        return folders;
    }

    private static ArrayList<String> getHiveRootSubFolderPaths() {

        ArrayList<String> lst = new ArrayList<>();

         lst.add("xml/incoming");
         lst.add("media/audio/incoming");
         lst.add("media/images/incoming");
         lst.add("media/pdfs/incoming");

        return lst;
    }

    public static ArrayList<File> getOutgoingHiveXmlFiles_yyyyMMddHHmmss(
            Context ctx,
            NwdDb db,
            String suffix) {

        String fileName = SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss()
                        + "-" + suffix + ".xml";

        ArrayList<File> files = new ArrayList<>();

        for(HiveRoot hr : db.getActiveHiveRoots(ctx)){

            for(File folder : Configuration.getHiveRootFolderPaths(hr)){

                if(folder.exists()){

                    files.add(new File(folder, fileName));
                }
            }
        }

        return files;
    }
}
