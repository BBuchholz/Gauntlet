package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by brent on 11/16/15.
 */
public class MnemoSyneUtils {
    public static List<String> getImages(File dir) {

        List<String> lst = new ArrayList<>();

        lst.addAll(Utils.getAllDirectoryNames(dir));

        String[] exts = {"png"}; //TODO: add more formats here
        lst.addAll(Utils.getAllFileNamesWithExt(dir, exts));

        return lst;
    }

    public static List<ImageListItem> getImageListItems(File dir){

        List<ImageListItem> lst = new ArrayList<>();

        lst.addAll(getImageListItemsFromPaths(Utils.getAllDirectoryPaths(dir)));

        String[] exts = {"png"}; //TODO: add more formats here
        lst.addAll(getImageListItemsFromPaths(Utils.getAllFilePathsWithExt(dir, exts)));

        return lst;
    }

    private static List<ImageListItem> getImageListItemsFromPaths(List<String> lst) {

        List<ImageListItem> newList = new ArrayList<>();

        for(String filePath : lst){
            newList.add(new ImageListItem(filePath));
        }

        return newList;
    }
}
