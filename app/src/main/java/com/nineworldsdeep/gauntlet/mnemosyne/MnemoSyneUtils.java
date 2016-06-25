package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.DisplayNameDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brent on 11/16/15.
 */
public class MnemoSyneUtils {

    private static String[] imageExts = {"png", "jpg", "gif"}; //TODO: add more formats here
    private static String[] audioExts = {"mp3", "wav"};
    private static String[] documentExts = {"pdf"};

    public static List<String> getImages(File dir) {

        List<String> lst = new ArrayList<>();

        lst.addAll(Utils.getAllDirectoryNames(dir));

        lst.addAll(Utils.getAllFileNamesWithExt(dir, imageExts));

        return lst;
    }

    public static List<FileListItem> getImageListItems(
            HashMap<String,String> pathToTagString, File dir){

        List<FileListItem> lst = new ArrayList<>();

        if(dir == null){

            lst.addAll(getImageListItemsFromPaths(pathToTagString, getTopImageFolders()));

        }else{

            lst.addAll(getFileListItems(pathToTagString, dir, imageExts));
        }

        return lst;
    }

    public static List<FileListItem> getImageListItems(NwdDb db, File dir){

        List<FileListItem> lst = new ArrayList<>();

        if(dir == null){

            lst.addAll(getImageListItemsFromPaths(db, getTopImageFolders()));

        }else{

            lst.addAll(getFileListItems(db, dir, imageExts));
        }

        return lst;
    }

    public static List<FileListItem> getAudioListItems(
            HashMap<String,String> pathToTagString, File dir){

        List<FileListItem> lst = new ArrayList<>();

        if(dir == null){

            lst.addAll(getAudioListItemsFromPaths(pathToTagString, getAudioTopFolders()));
        }
        else
        {
            lst.addAll(getFileListItems(pathToTagString, dir, audioExts));
        }

        return lst;
    }

    public static List<FileListItem> getAudioListItems(NwdDb db, File dir){

        List<FileListItem> lst = new ArrayList<>();

        if(dir == null){

            lst.addAll(getAudioListItemsFromPaths(db, getAudioTopFolders()));
        }
        else
        {
            lst.addAll(getFileListItems(db, dir, audioExts));
        }

        return lst;
    }

    public static List<FileListItem> getDocumentListItems(NwdDb db, File dir){

        List<FileListItem> lst = new ArrayList<>();

        if(dir == null){

            lst.addAll(getDocumentListItemsFromPaths(db, getDocumentTopFolders()));
        }
        else
        {
            lst.addAll(getFileListItems(db, dir, documentExts));
        }

        return lst;
    }

    private static List<FileListItem> getDocumentListItemsFromPaths(
            NwdDb db,
            List<String> pathList) {

        List<FileListItem> newList = new ArrayList<>();

        for(String filePath : pathList){

            File f = new File(filePath);

            if(isPdfFileFromPath(filePath) || f.isDirectory()) {

                newList.add(new FileListItem(filePath, db));
            }
        }

        return newList;

    }

    private static boolean isPdfFileFromPath(String filePath) {

        return filePath.toLowerCase().endsWith(".pdf");
    }

    private static List<String> getDocumentTopFolders() {

        List<String> lst = new ArrayList<>();

        lst.add(Configuration.getDownloadDirectory().getAbsolutePath());
        lst.add(Configuration.getPdfDirectory().getAbsolutePath());

        File externalPdf = Configuration.getSdCardMediaPdfDirectory();

        if(externalPdf != null) {

            lst.add(externalPdf.getAbsolutePath());
        }

        return lst;
    }

    public static List<String> getAudioTopFolders() {

        List<String> lst = new ArrayList<>();

        lst.add(Configuration.getDownloadDirectory().getAbsolutePath());
        lst.add(Configuration.getAudioDirectory().getAbsolutePath());
        lst.add(Configuration.getVoicememosDirectory().getAbsolutePath());

        File externalMusic = Configuration.getSdCardMediaMusicDirectory();

        if(externalMusic != null) {

            lst.add(externalMusic.getAbsolutePath());
        }

        return lst;
    }

    public static List<String> getTopImageFolders() {

        List<String> lst = new ArrayList<>();

        lst.add(Configuration.getDownloadDirectory().getAbsolutePath());
        lst.add(Configuration.getImagesDirectory().getAbsolutePath());
        lst.add(Configuration.getCameraDirectory().getAbsolutePath());
        lst.add(Configuration.getSkitchDirectory().getAbsolutePath());

        File f = Configuration.getScreenshotDirectory();

        if(f.exists()){

            lst.add(f.getAbsolutePath());
        }

        return lst;
    }

    private static List<FileListItem> getFileListItems(
            HashMap<String,String> pathToTagString,
            File dir,
            String[] exts){

        List<FileListItem> lst = new ArrayList<>();

        lst.addAll(getFileListItemsFromPaths(
                Utils.getAllDirectoryPaths(dir), pathToTagString));

        lst.addAll(getFileListItemsFromPaths(
                Utils.getAllFilePathsWithExt(dir, exts), pathToTagString));

        return lst;
    }

    private static List<FileListItem> getFileListItems(
            NwdDb db,
            File dir,
            String[] exts){

        List<FileListItem> lst = new ArrayList<>();

        lst.addAll(getFileListItemsFromPaths(
                Utils.getAllDirectoryPaths(dir), db));

        lst.addAll(getFileListItemsFromPaths(
                Utils.getAllFilePathsWithExt(dir, exts), db));

        return lst;
    }

    private static List<FileListItem> getFileListItemsFromPaths(
            List<String> lst,
            HashMap<String,String> pathToTagString) {

        List<FileListItem> newList = new ArrayList<>();

        for(String filePath : lst){

            newList.add(new FileListItem(filePath, pathToTagString));
        }

        return newList;
    }

    private static List<FileListItem> getFileListItemsFromPaths(
            List<String> lst,
            NwdDb db) {

        List<FileListItem> newList = new ArrayList<>();

        for(String filePath : lst){

            newList.add(new FileListItem(filePath, db));
        }

        return newList;
    }

    private static List<FileListItem> getImageListItemsFromPaths(
            HashMap<String,String> pathToTagString,
            List<String> lst) {

        List<FileListItem> newList = new ArrayList<>();

        for(String filePath : lst){

            File f = new File(filePath);

            if(isImageFileFromPath(filePath) || f.isDirectory()) {

                newList.add(new FileListItem(filePath, pathToTagString));
            }
        }

        return newList;
    }

    private static List<FileListItem> getImageListItemsFromPaths(
            NwdDb db,
            List<String> lst) {

        List<FileListItem> newList = new ArrayList<>();

        for(String filePath : lst){

            File f = new File(filePath);

            if(isImageFileFromPath(filePath) || f.isDirectory()) {

                newList.add(new FileListItem(filePath, db));
            }
        }

        return newList;
    }

    private static List<FileListItem> getAudioListItemsFromPaths(NwdDb db, List<String> lst) {

        List<FileListItem> newList = new ArrayList<>();

        for(String filePath : lst){

            File f = new File(filePath);

            if(isAudioFileFromPath(filePath) || f.isDirectory()) {

                newList.add(new FileListItem(filePath, db));
            }
        }

        return newList;
    }


    private static List<FileListItem> getAudioListItemsFromPaths(HashMap<String,String> pathToTagString, List<String> lst) {

        List<FileListItem> newList = new ArrayList<>();

        for(String filePath : lst){

            File f = new File(filePath);

            if(isAudioFileFromPath(filePath) || f.isDirectory()) {

                newList.add(new FileListItem(filePath, pathToTagString));
            }
        }

        return newList;
    }

    private static boolean isAudioFileFromPath(String filePath){
        return filePath.toLowerCase().endsWith(".wav") ||
                filePath.toLowerCase().endsWith(".mp3");
    }

    private static boolean isImageFileFromPath(String filePath){

        return filePath.toLowerCase().endsWith(".jpg") ||
                filePath.toLowerCase().endsWith(".png") ||
                filePath.toLowerCase().endsWith(".bmp") ||
                filePath.toLowerCase().endsWith(".gif");
    }

    public static void copyTags(String sourcePath,
                                String destinationPath,
                                NwdDb db)
            throws Exception {

        HashMap<String,String> dbPathToTagsMap = //storing for testing
                TagDbIndex.importExportPathToTagStringMap(db);

        FileListItem fliSrc = new FileListItem(sourcePath, db);
        FileListItem fliDest = new FileListItem(destinationPath, db);

//        MultiMapString pathToTags = new MultiMapString();
//
//        pathToTags.putCommaStringValues(fliDest.getFile().getAbsolutePath(),
//                fliSrc.getTags());
//
//        db.linkTagsToFile(pathToTags);
        //fliDest.setAndSaveTagString(fliSrc.getTags(), db);
        db.linkTagStringToFile(fliDest.getFile().getAbsolutePath(),
                fliSrc.getTags());
    }

    public static void copyDisplayName(String sourcePath,
                                       String destinationPath,
                                       NwdDb db)
            throws Exception {

        HashMap<String,String> dbPathToNameMap =
                DisplayNameDbIndex.importExportPathToNameMap(db);

        FileListItem fliSrc = new FileListItem(sourcePath, db);
        FileListItem fliDest = new FileListItem(destinationPath, db);

        if(!fliSrc.getDisplayName()
                .equalsIgnoreCase(fliSrc.getFile().getName())){

            db.linkFileToDisplayName(fliDest.getFile().getAbsolutePath(), fliSrc.getDisplayName());
            //fliDest.setAndSaveDisplayName(fliSrc.getDisplayName(), dbPathToNameMap);
        }
    }
}
