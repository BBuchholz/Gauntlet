package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.xml.Xml;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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

    public static ArrayList<MediaListItem> getMediaListItemsAudio(File dir) {

        ArrayList<MediaListItem> lst = new ArrayList<>();

        if(dir == null){

            lst.addAll(getAudioMediaListItemsFromPaths(getAudioTopFolders()));
        }
        else
        {
            lst.addAll(getMediaListItems(dir, audioExts));
        }

        return lst;
    }

//    public static ArrayList<MediaListItem> getMediaListItemsAudio(
//            HashMap<String, String> pathToTagString,
//            File dir) {
//
//        ArrayList<MediaListItem> lst = new ArrayList<>();
//
//        if(dir == null){
//
//            lst.addAll(getAudioMediaListItemsFromPaths(pathToTagString, getAudioTopFolders()));
//        }
//        else
//        {
//            lst.addAll(getMediaListItems(pathToTagString, dir, audioExts));
//        }
//
//        return lst;
//    }

//    public static ArrayList<MediaListItem> getMediaListItemsImage(
//            HashMap<String, String> pathToTagString,
//            File dir) {
//
//        ArrayList<MediaListItem> lst = new ArrayList<>();
//
//        if(dir == null){
//
//            lst.addAll(getImageMediaListItemsFromPaths(pathToTagString, getImageTopFolders()));
//        }
//        else
//        {
//            lst.addAll(getMediaListItems(pathToTagString, dir, imageExts));
//        }
//
//        return lst;
//    }


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

//    private static ArrayList<MediaListItem> getMediaListItems(
//            HashMap<String, String> pathToTagString,
//            File dir,
//            String[] exts) {
//
//        ArrayList<MediaListItem> lst = new ArrayList<>();
//
//        lst.addAll(getMediaListItemsFromPaths(
//                Utils.getAllDirectoryPaths(dir), pathToTagString));
//
//        lst.addAll(getMediaListItemsFromPaths(
//                Utils.getAllFilePathsWithExt(dir, exts), pathToTagString));
//
//        return lst;
//    }

//    private static ArrayList<MediaListItem> getMediaListItemsFromPaths(
//            ArrayList<String> pathList,
//            HashMap<String, String> pathToTagString) {
//
//        ArrayList<MediaListItem> newList = new ArrayList<>();
//
//        for(String filePath : pathList){
//
//            String tagString = pathToTagString.get(filePath);
//
//            newList.add(new MediaListItem(filePath, tagString));
//        }
//
//        return newList;
//    }

    private static ArrayList<MediaListItem> getMediaListItemsFromPaths(
            ArrayList<String> pathList) {

        ArrayList<MediaListItem> newList = new ArrayList<>();

        for(String filePath : pathList){

            newList.add(new MediaListItem(filePath));
        }

        return newList;
    }

    private static ArrayList<MediaListItem> getAudioMediaListItemsFromPaths(
            ArrayList<String> paths) {

        ArrayList<MediaListItem> newList = new ArrayList<>();

        for(String filePath : paths){

            File f = new File(filePath);

            if(isAudioFileFromPath(filePath) || f.isDirectory()) {

                MediaListItem mli =
                        new MediaListItem(filePath);

                newList.add(mli);
            }
        }

        return newList;
    }

//    private static ArrayList<MediaListItem> getAudioMediaListItemsFromPaths(
//            HashMap<String, String> pathToTagString,
//            ArrayList<String> paths) {
//
//        ArrayList<MediaListItem> newList = new ArrayList<>();
//
//        for(String filePath : paths){
//
//            File f = new File(filePath);
//
//            if(isAudioFileFromPath(filePath) || f.isDirectory()) {
//
//                String tagString = pathToTagString.get(filePath);
//
//                MediaListItem mli =
//                        new MediaListItem(filePath, tagString);
//
//                newList.add(mli);
//            }
//        }
//
//        return newList;
//    }

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

//    private static ArrayList<MediaListItem> getImageMediaListItemsFromPaths(
//            HashMap<String, String> pathToTagString,
//            ArrayList<String> paths) {
//
//        ArrayList<MediaListItem> newList = new ArrayList<>();
//
//        for(String filePath : paths){
//
//            File f = new File(filePath);
//
//            if(isImageFileFromPath(filePath) || f.isDirectory()) {
//
//                String tagString = pathToTagString.get(filePath);
//
//                MediaListItem mli =
//                        new MediaListItem(filePath, tagString);
//
//                newList.add(mli);
//            }
//        }
//
//        return newList;
//    }

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

    public static String tryMergeString(String s1, String s2) throws Exception {

        if(!Utils.stringIsNullOrWhitespace(s1) &&
            !Utils.stringIsNullOrWhitespace(s2) &&
            !s1.equals(s2))
        {
            throw new Exception("unable to merge MediaTagging, conflicting values set on an exclusive property");
        }

        if (!Utils.stringIsNullOrWhitespace(s1))
        {
            return s1;
        }

        return s2;
    }

    public static int tryMergeInt(int int1, int int2) throws Exception {

        if(int1 > 0 && int2 > 0)
        {
            throw new Exception("unable to merge MediaTagging, conflicting values set on an exclusive property");
        }

        if(int1 > 0)
        {
            return int1;
        }

        return int2;
    }

    public static void exportToXml(ArrayList<Media> mediaList, NwdDb db) throws Exception {

        Document doc = Xml.createDocument(Xml.TAG_NWD);
        Element mnemosyneSubsetEl = doc.createElement(Xml.TAG_MNEMOSYNE_SUBSET);
        doc.getDocumentElement().appendChild(mnemosyneSubsetEl);

        //get all media, just single table query, to get hash
        //then sync all with db.populateByHash(ArrayList<Media>)

        //adapted from export all code, leaving it as much the same as possible

        db.populateTaggingsAndDevicePaths(mediaList);

        for(Media media : mediaList) {

            Element mediaEl = doc.createElement(Xml.TAG_MEDIA);

            mediaEl.setAttribute(
                    Xml.ATTR_SHA1_HASH,
                    media.getMediaHash());

//
//                mediaEl.setAttribute(
//                        Xml.ATTR_FILE_NAME,
//                        media.getMediaFileName());
//
//                mediaEl.setAttribute(
//                        Xml.ATTR_DESCRIPTION,
//                        media.getMediaDescription());


            Xml.setAttributeIfNotNullOrWhitespace(
                    mediaEl,
                    Xml.ATTR_FILE_NAME,
                    media.getMediaFileName());

            Xml.setAttributeIfNotNullOrWhitespace(
                    mediaEl,
                    Xml.ATTR_DESCRIPTION,
                    media.getMediaDescription());

            mnemosyneSubsetEl.appendChild(mediaEl);

            for (MediaTagging mt : media.getMediaTaggings()) {

                Element tagEl = doc.createElement(Xml.TAG_TAG);

                tagEl.setAttribute(
                        Xml.ATTR_TAG_VALUE,
                        mt.getMediaTagValue());

                tagEl.setAttribute(
                        Xml.ATTR_TAGGED_AT,
                        TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                mt.getTaggedAt()));

                tagEl.setAttribute(
                        Xml.ATTR_UNTAGGED_AT,
                        TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                mt.getUntaggedAt()));

                mediaEl.appendChild(tagEl);
            }

            for (String deviceName : media.getDevicePaths().keySet()) {

                Element mediaDeviceEl =
                        doc.createElement(Xml.TAG_MEDIA_DEVICE);

                mediaDeviceEl.setAttribute(
                        Xml.ATTR_DESCRIPTION, deviceName);

                mediaEl.appendChild(mediaDeviceEl);

                for (DevicePath dp : media.getDevicePaths().get(deviceName)) {

                    Element pathEl = doc.createElement(Xml.TAG_PATH);

                    pathEl.setAttribute(
                            Xml.ATTR_VALUE,
                            dp.getPath());

                    pathEl.setAttribute(
                            Xml.ATTR_VERIFIED_PRESENT,
                            TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                    dp.getVerifiedPresent()));

                    pathEl.setAttribute(
                            Xml.ATTR_VERIFIED_MISSING,
                            TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                    dp.getVerifiedMissing()));

                    mediaDeviceEl.appendChild(pathEl);
                }
            }
        }

        File outputFile =
            Configuration.getOutgoingXmlFile_yyyyMMddHHmmss(
                    Xml.FILE_NAME_MNEMOSYNE_V5);

            Xml.write(outputFile, doc);
    }
}
