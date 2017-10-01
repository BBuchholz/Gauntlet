package com.nineworldsdeep.gauntlet.hive;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.hive.lobes.HiveLobeAudio;
import com.nineworldsdeep.gauntlet.hive.lobes.HiveLobeImages;
import com.nineworldsdeep.gauntlet.hive.lobes.HiveLobePdfs;
import com.nineworldsdeep.gauntlet.hive.lobes.HiveLobeXml;
import com.nineworldsdeep.gauntlet.hive.spores.HiveSporeFilePath;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaListItem;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by brent on 7/5/17.
 */

public class UtilsHive {

    public static ArrayList<MediaListItem> getSporesAsMediaListItems(HiveLobe hl) {

        ArrayList<MediaListItem> lst = new ArrayList<>();

        for(HiveSpore hs : hl.getSpores()){

            if(hs instanceof HiveSporeFilePath){

                HiveSporeFilePath hsfp = (HiveSporeFilePath)hs;

                lst.add(new MediaListItem(hsfp.getFile().getAbsolutePath()));
            }
        }

        return lst;
    }

    /**
     * returns null if doesn't exist in the database
     * @param ctx
     * @param db
     * @return
     */
    public static HiveRoot getLocalHiveRoot(Context ctx, NwdDb db){

        String deviceName = Configuration.getLocalDeviceName();

        for(HiveRoot hr : db.getActiveHiveRoots(ctx)){

            if(hr.getHiveRootName().equalsIgnoreCase(deviceName)){

                return hr;
            }
        }

        return null;
    }

    public static boolean checkAndRegisterNewRootFolders(NwdDb db, Context ctx) {

        ArrayList<String> rootNamesFromFolderNames = new ArrayList<>();
        ArrayList<String> unregisteredFolderNames = new ArrayList<>();
        ArrayList<HiveRoot> allRoots = db.getAllHiveRoots(ctx);

        //get immediate sub folders of SyncRoot (NWD-SYNC/hive/)
        for(File hiveRootFolder :
                Configuration.getFileSystemTopLevelHiveRootFolders()){

            boolean found = false;

            for(HiveRoot hr : allRoots){

                if(hr.getHiveRootName().equalsIgnoreCase(
                        hiveRootFolder.getName())){

                    found = true;
                }
            }

            if(!found){

                unregisteredFolderNames.add(hiveRootFolder.getName());
            }
        }

        //register them all
        for(String unregisteredRootName : unregisteredFolderNames){

            HiveRoot newHr = new HiveRoot(-1, unregisteredRootName);
            db.syncByName(ctx, newHr);
            newHr.deactivate();
            db.syncByName(ctx, newHr);
        }

        return unregisteredFolderNames.size() > 0;
    }

    public static HiveSporeType getSporeTypeFromFile(File fileForSpore) {

        if(FilenameUtils.getExtension(
                fileForSpore.getAbsolutePath()).equalsIgnoreCase("xml")){

            return HiveSporeType.xml;
        }

        if(FilenameUtils.getExtension(
                fileForSpore.getAbsolutePath()).equalsIgnoreCase("pdf")){

            return HiveSporeType.pdf;
        }

        if(ConfigHive.isImageFile(fileForSpore)){

            return HiveSporeType.image;
        }

        if(ConfigHive.isAudioFile(fileForSpore)){

            return HiveSporeType.audio;
        }

        return HiveSporeType.unknown;
    }

    public static Iterable<File> siftFilesForSporeType(HiveSporeType sporeType,
                                                       Iterable<File> files){

        ArrayList<File> lst = new ArrayList<>();

        for(File f : files){

            if(getSporeTypeFromFile(f) == sporeType){

                lst.add(f);
            }
        }

        return lst;
    }

    public static void refreshLobes(HiveRoot hr) {

        hr.add(new HiveLobeXml(hr));
        hr.add(new HiveLobeImages(hr));
        hr.add(new HiveLobeAudio(hr));
        hr.add(new HiveLobePdfs(hr));
    }

    public static void refreshSpores(HiveLobe hl){

        //just simple for now, it gets its own method here
        //because we will eventually be pulling associated
        //data from the db as well

        hl.sporesInternal.clear();
        hl.collect();
    }

    public static void getFromStaging(Context context,
                                      HiveLobe lobe,
                                      FileMovementType fileMovementType) {

        String msg = "";

        HiveRoot stagingRoot =
                HiveRegistry.getHiveRoot(ConfigHive.STAGING_ROOT_NAME);

        refreshLobes(stagingRoot);

        for(File fileToGet : lobe.siftFiles(stagingRoot)) {

            if (fileToGet.exists()) {

                try {

                    File destinationFile =
                            new File(lobe.getAssociatedDirectory(),
                                    FilenameUtils.getName(
                                            fileToGet.getAbsolutePath()));

                    if(destinationFile.exists()){

                        if(fileHashesAreEqual(context, fileToGet, destinationFile)) {

                            if (fileMovementType == FileMovementType.MoveTo) {

                                fileToGet.delete();
                                msg = "files moved.";

                            }else{

                                msg = "files copied";
                            }


                        }

                    }else {

                        switch (fileMovementType) {

                            case MoveTo:

                                FileUtils.moveFile(fileToGet, destinationFile);
                                msg = "files moved.";
                                break;

                            case CopyTo:

                                FileUtils.copyFile(fileToGet, destinationFile);
                                msg = "files copied";
                                break;
                        }
                    }

                } catch (Exception ex) {

                    msg = "Error moving files: " + ex.getMessage();
                }

            } else {

                msg = "non-existent path: " + fileToGet.getAbsolutePath();
            }

        }

        if (!Utils.stringIsNullOrWhitespace(msg)) {

            Utils.toast(context, msg);
        }
    }

    public static void addToStaging(Context context,
                                    File fileToAdd,
                                    FileMovementType fileMovementType) {

        String msg = "";

        if(fileToAdd.exists()){

            try{

                File destinationFile =
                        new File(getStagingDirectoryForFileType(fileToAdd),
                                FilenameUtils.getName(fileToAdd.getAbsolutePath()));

                if(destinationFile.exists()){

                    if(fileHashesAreEqual(context, fileToAdd, destinationFile)
                            && fileMovementType == FileMovementType.MoveTo){

                        fileToAdd.delete();
                    }

                }else {

                    switch (fileMovementType) {

                        case MoveTo:

                            FileUtils.moveFile(fileToAdd, destinationFile);
                            msg = "file moved.";
                            break;

                        case CopyTo:

                            FileUtils.copyFile(fileToAdd, destinationFile);
                            msg = "file copied";
                            break;
                    }
                }

            }catch (Exception ex){

                msg = "Error moving file: " + ex.getMessage();
            }

        }else{

            msg = "non existant path: " + fileToAdd.getAbsolutePath();
        }

        if(!Utils.stringIsNullOrWhitespace(msg)){

            Utils.toast(context, msg);
        }
    }

    /**
     *
     * @param fileToAdd
     * @return
     */
    private static File getStagingDirectoryForFileType(File fileToAdd) {

        HiveSporeType sporeType = getSporeTypeFromFile(fileToAdd);

        return ConfigHive.getHiveSubFolderForRootNameAndType(
                ConfigHive.STAGING_ROOT_NAME, sporeType);
    }

    public static void moveToStaging(Context context, File fileToMove) {

        addToStaging(context, fileToMove, FileMovementType.MoveTo);
    }

    public static void copyToStaging(Context context, File fileToCopy) {

        addToStaging(context, fileToCopy, FileMovementType.CopyTo);
    }

    public static void copyAllFromStagingTo(Context context, HiveLobe lobe) {

        HiveRoot stagingRoot =
                HiveRegistry.getHiveRoot(ConfigHive.STAGING_ROOT_NAME);

        if(stagingRoot != null){

            getFromStaging(context, lobe, FileMovementType.CopyTo);

        }else{

            Utils.toast(context, "staging root not registered");
        }
    }

    public static void moveAllFromStagingTo(Context context, HiveLobe lobe) {

        HiveRoot stagingRoot =
                HiveRegistry.getHiveRoot(ConfigHive.STAGING_ROOT_NAME);

        if(stagingRoot != null){

            getFromStaging(context, lobe, FileMovementType.MoveTo);

        }else{

            Utils.toast(context, "staging root not registered");
        }
    }

    /**
     * supports images, audio, and pdfs, as defined by nwd-hive internally
     * @param intakeFiles
     */
    public static void intake(Context context,
                              Iterable<File> intakeFiles) throws Exception {

        //images
        for(File imageFile :
                siftFilesForSporeType(HiveSporeType.image, intakeFiles)){

            moveFile(context, imageFile, Configuration.getScreenshotDirectory());
        }

        //audio
        for(File audioFile :
                siftFilesForSporeType(HiveSporeType.audio, intakeFiles)){

            moveFile(context, audioFile, Configuration.getVoicememosDirectory());
        }

        //pdfs
        for(File pdfFile :
                siftFilesForSporeType(HiveSporeType.pdf, intakeFiles)){

            moveFile(context, pdfFile, Configuration.getPdfDirectory());
        }

    }

    public static void moveFile(Context context,
                                File sourceFile,
                                File destinationDirectory) throws Exception {

        if(sourceFile.exists() && sourceFile.isFile()){

            File destinationFile =
                    new File(destinationDirectory,
                            FilenameUtils.getName(
                                    sourceFile.getAbsolutePath()));

            if(destinationFile.exists()){

                if(fileHashesAreEqual(context, sourceFile, destinationFile)){

                    sourceFile.delete();
                }

            }else {

                FileUtils.moveFile(sourceFile, destinationFile);
            }
        }
    }

    private static boolean fileHashesAreEqual(Context context, File sourceFile, File destinationFile) throws Exception {

        String sourceHash =
                Utils.computeSHA1(sourceFile.getAbsolutePath());
        String destHash =
                Utils.computeSHA1(destinationFile.getAbsolutePath());

        if(!sourceHash.equalsIgnoreCase(destHash)){

            Utils.toast(context, "Destination file [" +
                    destinationFile.getName() +
                    "] already exists with a different hash value, " +
                    "file move aborted.");

            return false;
        }

        return true;
    }

    public static boolean isLocalRoot(Context context,
                                      NwdDb db,
                                      HiveRoot hiveRoot) {

        return getLocalHiveRoot(context, db).equals(hiveRoot);
    }

    public enum FileMovementType {

        MoveTo,
        CopyTo
    }
}
