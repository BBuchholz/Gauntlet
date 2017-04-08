package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brent on 2/22/17.
 */

public class UtilsMnemosyneV5 {

    //TODO: add more image formats
    private static String[] imageExts = {"png", "jpg", "jpeg", "gif"};
    private static String[] audioExts = {"mp3", "wav"};
    private static String[] documentExts = {"pdf"};

    static String getMimeType(File file) {

        String type = null;

//        final String url = file.toString();
//        final String extension = MimeTypeMap.getFileExtensionFromUrl(url);

        if (file == null) {

            type = "*/*"; // fallback type

        }else {

            final String extension =
                    FilenameUtils.getExtension(file.getName());

            if (extension != null &&
                    !Utils.stringIsNullOrWhitespace(extension)) {

                type = MimeTypeMap
                        .getSingleton()
                        .getMimeTypeFromExtension(extension.toLowerCase());
            }

            if (type == null) {

                type = "*/*"; // fallback type
            }
        }

        return type;
    }

    public static ArrayList<MediaListItem> getMediaListItemsAudio(
            HashMap<String, String> pathToTagString,
            File dir) {

        ArrayList<MediaListItem> lst = new ArrayList<>();

        if(dir == null){

            lst.addAll(getAudioMediaListItemsFromPaths(pathToTagString, getAudioTopFolders()));
        }
        else
        {
            lst.addAll(getMediaListItems(pathToTagString, dir, audioExts));
        }

        return lst;
    }

    public static ArrayList<MediaListItem> getMediaListItemsImage(
            HashMap<String, String> pathToTagString,
            File dir) {

        ArrayList<MediaListItem> lst = new ArrayList<>();

        if(dir == null){

            lst.addAll(getImageMediaListItemsFromPaths(pathToTagString, getImageTopFolders()));
        }
        else
        {
            lst.addAll(getMediaListItems(pathToTagString, dir, imageExts));
        }

        return lst;
    }


    public static ArrayList<MediaListItem> getMediaListItemsImage(File dir) {

        ArrayList<MediaListItem> lst = new ArrayList<>();

        if(dir == null){

            lst.addAll(getImageMediaListItemsFromPaths(getImageTopFolders()));
        }
        else
        {
            lst.addAll(getMediaListItems(dir, imageExts));
        }

        return lst;
    }

    private static ArrayList<MediaListItem> getMediaListItems(
            File dir,
            String[] exts) {

        ArrayList<MediaListItem> lst = new ArrayList<>();

        lst.addAll(getMediaListItemsFromPaths(
                Utils.getAllDirectoryPaths(dir)));

        lst.addAll(getMediaListItemsFromPaths(
                Utils.getAllFilePathsWithExt(dir, exts)));

        return lst;
    }

    private static ArrayList<MediaListItem> getMediaListItems(
            HashMap<String, String> pathToTagString,
            File dir,
            String[] exts) {

        ArrayList<MediaListItem> lst = new ArrayList<>();

        lst.addAll(getMediaListItemsFromPaths(
                Utils.getAllDirectoryPaths(dir), pathToTagString));

        lst.addAll(getMediaListItemsFromPaths(
                Utils.getAllFilePathsWithExt(dir, exts), pathToTagString));

        return lst;
    }

    private static ArrayList<MediaListItem> getMediaListItemsFromPaths(
            ArrayList<String> pathList,
            HashMap<String, String> pathToTagString) {

        ArrayList<MediaListItem> newList = new ArrayList<>();

        for(String filePath : pathList){

            String tagString = pathToTagString.get(filePath);

            newList.add(new MediaListItem(filePath, tagString));
        }

        return newList;
    }

    private static ArrayList<MediaListItem> getMediaListItemsFromPaths(
            ArrayList<String> pathList) {

        ArrayList<MediaListItem> newList = new ArrayList<>();

        for(String filePath : pathList){

            newList.add(new MediaListItem(filePath));
        }

        return newList;
    }

    private static ArrayList<MediaListItem> getAudioMediaListItemsFromPaths(
            HashMap<String, String> pathToTagString,
            ArrayList<String> paths) {

        ArrayList<MediaListItem> newList = new ArrayList<>();

        for(String filePath : paths){

            File f = new File(filePath);

            if(isAudioFileFromPath(filePath) || f.isDirectory()) {

                String tagString = pathToTagString.get(filePath);

                MediaListItem mli =
                        new MediaListItem(filePath, tagString);

                newList.add(mli);
            }
        }

        return newList;
    }

    private static ArrayList<MediaListItem> getImageMediaListItemsFromPaths(
            ArrayList<String> paths) {

        ArrayList<MediaListItem> newList = new ArrayList<>();

        for(String filePath : paths){

            File f = new File(filePath);

            if(isImageFileFromPath(filePath) || f.isDirectory()) {

                MediaListItem mli =
                        new MediaListItem(filePath);

                newList.add(mli);
            }
        }

        return newList;
    }

    private static ArrayList<MediaListItem> getImageMediaListItemsFromPaths(
            HashMap<String, String> pathToTagString,
            ArrayList<String> paths) {

        ArrayList<MediaListItem> newList = new ArrayList<>();

        for(String filePath : paths){

            File f = new File(filePath);

            if(isImageFileFromPath(filePath) || f.isDirectory()) {

                String tagString = pathToTagString.get(filePath);

                MediaListItem mli =
                        new MediaListItem(filePath, tagString);

                newList.add(mli);
            }
        }

        return newList;
    }

    private static boolean isImageFileFromPath(String path) {

        boolean isImageFile = false;

        for(String ext : imageExts){

            if(path.toLowerCase().endsWith("." + ext.toLowerCase())){

                isImageFile = true;
            }
        }

        return isImageFile;
    }

    private static boolean isAudioFileFromPath(String path) {

        boolean isAudioFile = false;

        for(String ext : audioExts){

            if(path.toLowerCase().endsWith("." + ext.toLowerCase())){

                isAudioFile = true;
            }
        }

        return isAudioFile;
    }

    public static ArrayList<String> getAudioTopFolders() {

        ArrayList<String> lst = new ArrayList<>();

        lst.add(Configuration.getDownloadDirectory().getAbsolutePath());
        lst.add(Configuration.getAudioDirectory().getAbsolutePath());
        lst.add(Configuration.getVoicememosDirectory().getAbsolutePath());

        File externalMusic = Configuration.getSdCardMediaMusicDirectory();
        File causticSongExports = Configuration.getCausticSongExportsDirectory();

        if(externalMusic != null) {

            lst.add(externalMusic.getAbsolutePath());
        }

        if(causticSongExports != null){

            lst.add(causticSongExports.getAbsolutePath());
        }

        return lst;
    }

    public static ArrayList<String> getImageTopFolders() {

        ArrayList<String> lst = new ArrayList<>();

        lst.add(Configuration.getDownloadDirectory().getAbsolutePath());
        lst.add(Configuration.getImagesDirectory().getAbsolutePath());
        lst.add(Configuration.getCameraDirectory().getAbsolutePath());
        lst.add(Configuration.getMemesDirectory().getAbsolutePath());

        File f = Configuration.getScreenshotDirectory();

        if(f.exists()){

            lst.add(f.getAbsolutePath());
        }

        return lst;
    }
}
