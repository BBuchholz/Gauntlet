package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 11/16/15.
 */
public class MnemoSyneUtils {

    private static String[] imageExts = {"png", "jpg", "gif"}; //TODO: add more formats here
    private static String[] audioExts = {"mp3", "wav"};

    public static List<String> getImages(File dir) {

        List<String> lst = new ArrayList<>();

        lst.addAll(Utils.getAllDirectoryNames(dir));

        lst.addAll(Utils.getAllFileNamesWithExt(dir, imageExts));

        return lst;
    }

    public static List<FileListItem> getImageListItems(File dir){

        return getFileListItems(dir, imageExts);
    }

    public static List<FileListItem> getAudioListItems(File dir) {

        return getFileListItems(dir, audioExts);
    }

    public static List<FileListItem> getImageListItems(File dir,
                                                       boolean includeSyncFolders){

        List<FileListItem> lst = new ArrayList<>();

        if(includeSyncFolders){

            lst.addAll(getFileListItemsFromPaths(getImageSyncFolders()));
        }

        lst.addAll(getFileListItems(dir, imageExts));

        return lst;
    }

    public static List<FileListItem> getAudioListItems(File dir,
                                                       boolean includeTopFolders){

        List<FileListItem> lst = new ArrayList<>();

        if(includeTopFolders){

            lst.addAll(getFileListItemsFromPaths(getAudioTopFolders()));
        }

        lst.addAll(getFileListItems(dir, audioExts));

        return lst;
    }

    public static List<String> getAudioTopFolders() {

        List<String> lst = new ArrayList<>();

        lst.add(Configuration.getVoicememosDirectory().getAbsolutePath());

        File externalMusic = Configuration.getSdCardMediaMusicDirectory();

        if(externalMusic != null) {

            lst.add(externalMusic.getAbsolutePath());
        }

        return lst;
    }

    public static List<String> getImageSyncFolders() {

        List<String> lst = new ArrayList<>();

        lst.add(Configuration.getCameraDirectory().getAbsolutePath());
        lst.add(Configuration.getScreenshotDirectory().getAbsolutePath());
        lst.add(Configuration.getSkitchDirectory().getAbsolutePath());

        return lst;
    }

    private static List<FileListItem> getFileListItems(File dir, String[] exts){

        List<FileListItem> lst = new ArrayList<>();

        lst.addAll(getFileListItemsFromPaths(Utils.getAllDirectoryPaths(dir)));

        lst.addAll(getFileListItemsFromPaths(Utils.getAllFilePathsWithExt(dir, exts)));

        return lst;
    }

    private static List<FileListItem> getFileListItemsFromPaths(List<String> lst) {

        List<FileListItem> newList = new ArrayList<>();

        for(String filePath : lst){
            newList.add(new FileListItem(filePath));
        }

        return newList;
    }

}
