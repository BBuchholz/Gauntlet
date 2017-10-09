package com.nineworldsdeep.gauntlet.hive;

import com.nineworldsdeep.gauntlet.core.Configuration;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

/**
 * Created by brent on 9/24/17.
 */

public class ConfigHive {


    public static final String HIVE_XML_INCOMING_SUBFOLDER = "xml/incoming";
    public static final String HIVE_AUDIO_INCOMING_SUBFOLDER = "media/audio/incoming";
    public static final String HIVE_IMAGES_INCOMING_SUBFOLDER = "media/images/incoming";
    public static final String HIVE_PDFS_INCOMING_SUBFOLDER = "media/pdfs/incoming";
    public static final String STAGING_ROOT_NAME = "staging";

    private static String XML_SUB_FOLDER = "xml/incoming";
    private static String AUDIO_SUB_FOLDER = "media/audio/incoming";
    private static String PDFS_SUB_FOLDER = "media/pdfs/incoming";
    private static String IMAGE_SUB_FOLDER = "media/images/incoming";

    public static String[] IMAGE_EXTENSIONS =
            {"png", "jpg", "jpeg", "gif", "bmp", "tiff"};
    public static String[] AUDIO_EXTENSIONS = {"mp3", "wav"};

    public static File getHiveRootAudioFolderPath(HiveRoot hiveRoot) {

        return getHiveRootSubFolderPath(hiveRoot, AUDIO_SUB_FOLDER);
    }

    public static File getHiveRootXmlFolderPath(HiveRoot hiveRoot) {

        return getHiveRootSubFolderPath(hiveRoot, XML_SUB_FOLDER);
    }

    public static File getHiveRootImagesFolderPath(HiveRoot hiveRoot) {

        return getHiveRootSubFolderPath(hiveRoot, IMAGE_SUB_FOLDER);
    }

    public static File getHiveRootPdfsFolderPath(HiveRoot hiveRoot) {

        return getHiveRootSubFolderPath(hiveRoot, PDFS_SUB_FOLDER);
    }

    private static File getHiveRootSubFolderPath(HiveRoot hr, String subPath){

        File hiveFolder = new File(getSyncFolder(), "hive");
        File hiveRootFolder = new File(hiveFolder, hr.getHiveRootName());

        return new File(hiveRootFolder, subPath);
    }

    public static File getSyncFolder(){

        return Configuration.getSyncFolder();
    }

    public static boolean isAudioFile(File f){

        boolean isAudio = false;

        for(String ext : AUDIO_EXTENSIONS){

            if(FilenameUtils.getExtension(
                f.getAbsolutePath()).equalsIgnoreCase(ext)){

                isAudio = true;
            }
        }

        return isAudio;
    }

    public static boolean isImageFile(File f){

        boolean isImage = false;

        for(String ext : IMAGE_EXTENSIONS){

            if(FilenameUtils.getExtension(
                f.getAbsolutePath()).equalsIgnoreCase(ext)){

                isImage = true;
            }
        }

        return isImage;
    }

    public static File getHiveSubFolderForRootNameAndType(String hiveRootName,
                                                          HiveSporeType sporeType){

        File syncFolder = getSyncFolder();
        File hiveFolder = new File(syncFolder, "hive");
        File hiveRootFolder = new File(hiveFolder, hiveRootName);
        File subFolder = new File(hiveRootFolder,
                getInternalSubFolderPathForSporeType(sporeType));

        return subFolder;
    }

    private static String getInternalSubFolderPathForSporeType(HiveSporeType sporeType) {

        switch (sporeType){

            case audio:

                return HIVE_AUDIO_INCOMING_SUBFOLDER;

            case image:

                return HIVE_IMAGES_INCOMING_SUBFOLDER;

            case pdf:

                return HIVE_PDFS_INCOMING_SUBFOLDER;

            case xml:

                return HIVE_XML_INCOMING_SUBFOLDER;

            default:

                return null;
        }
    }

    public static boolean isStagingRoot(HiveRoot hiveRoot) {

        return hiveRoot.getHiveRootName().equalsIgnoreCase(STAGING_ROOT_NAME);
    }

    public static boolean isLocalRoot(HiveRoot hiveRoot) {

        return hiveRoot.getHiveRootName().equalsIgnoreCase(
                Configuration.getLocalDeviceName());
    }
}
