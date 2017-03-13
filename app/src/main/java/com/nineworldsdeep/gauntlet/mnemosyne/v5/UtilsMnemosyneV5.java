package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brent on 2/22/17.
 */

public class UtilsMnemosyneV5 {

    //TODO: add more image formats
    private static String[] imageExts = {"png", "jpg", "gif"};
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

    private static ArrayList<MediaListItem> getAudioMediaListItemsFromPaths(
            HashMap<String, String> pathToTagString,
            ArrayList<String> lst) {

        ArrayList<MediaListItem> newList = new ArrayList<>();

        for(String filePath : lst){

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
}
