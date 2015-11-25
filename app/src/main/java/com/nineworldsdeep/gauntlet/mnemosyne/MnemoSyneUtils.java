package com.nineworldsdeep.gauntlet.mnemosyne;

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

    public static List<FileListItem> getAudioListItems(File dir) {

        return getFileListItems(dir, audioExts);
    }
}
