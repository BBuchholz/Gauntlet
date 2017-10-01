package com.nineworldsdeep.gauntlet.hive;

import android.content.Context;
import android.graphics.Bitmap;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.hive.lobes.HiveLobeAudio;
import com.nineworldsdeep.gauntlet.hive.lobes.HiveLobeImages;
import com.nineworldsdeep.gauntlet.hive.lobes.HiveLobePdfs;
import com.nineworldsdeep.gauntlet.hive.lobes.HiveLobeXml;
import com.nineworldsdeep.gauntlet.hive.spores.HiveSporeFilePath;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.AudioListV5Activity;
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

    public static final String STAGING_ROOT_NAME = "staging";

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

        hl.collect();
    }

    public static void addToStaging(Context context,
                                    File fileToAdd,
                                    StagingMovementType stagingMovementType) {

        String msg = "";

        if(fileToAdd.exists()){

            try{

                File destinationFile =
                        new File(getStagingDirectoryForFileType(fileToAdd),
                                FilenameUtils.getName(fileToAdd.getAbsolutePath()));

                switch (stagingMovementType) {

                    case MoveToStaging:

                        FileUtils.moveFile(fileToAdd, destinationFile);
                        msg = "file moved.";
                        break;

                    case CopyToStaging:

                        FileUtils.copyFile(fileToAdd, destinationFile);
                        msg = "file copied";
                        break;
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
                UtilsHive.STAGING_ROOT_NAME, sporeType);
    }

    public static void moveToStaging(Context context, File fileToMove) {

        addToStaging(context, fileToMove, StagingMovementType.MoveToStaging);
    }

    public static void copyToStaging(Context context, File fileToCopy) {

        addToStaging(context, fileToCopy, StagingMovementType.CopyToStaging);
    }

    public enum StagingMovementType{

        MoveToStaging,
        CopyToStaging
    }
}
