package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.mnemosyne.ImageGridItem;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5Utils;
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

    public static void copyFileNameToClipboard(Context context, MediaListItem mli){

        if(mli == null){
            Utils.toast(context, "Media List Item null");
            return;
        }

        String fileName = mli.getFile().getName();
        Utils.copyToClipboard(context, "media-file-name", fileName);

        Utils.toast(context, "copied");
    }

    public static void copyFileNameToClipboard(Context context, TagBrowserFileItem tagBrowserFileItem){

        if(tagBrowserFileItem == null){
            Utils.toast(context, "Media List Item null");
            return;
        }

        String fileName = tagBrowserFileItem.getFilename();
        Utils.copyToClipboard(context, "media-file-name", fileName);

        Utils.toast(context, "copied");
    }

    public static void copyHashToClipboard(Context context, MediaListItem mli){

        if(mli == null){
            Utils.toast(context, "Media List Item null");
            return;
        }

        if(mli.getHash() == null){

            try {

                mli.hashMedia();

            } catch (Exception e) {

                Utils.toast(context, "error hashing media: " + e.getMessage());
            }
        }

        String hash = mli.getHash();
        Utils.copyToClipboard(context, "media-hash", hash);

        Utils.toast(context, "copied");
    }

    public static String getMimeType(File file) {

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

    public static ArrayList<ImageGridItem> getImageGridItems(File dir) throws Exception {

        ArrayList<ImageGridItem> lst = new ArrayList<>();

        for(MediaListItem mli : getMediaListItemsImage(dir)){

            if(mli.getFile().isFile()) {

                lst.add(ImageGridItem.From(mli));
            }
        }

        return lst;
    }

    public static ArrayList<MediaListItem> getMediaListItemsAudio(File dir) throws Exception {

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
//            lst.addAll(getSporesAsMediaListItems(pathToTagString, dir, audioExts));
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
//            lst.addAll(getSporesAsMediaListItems(pathToTagString, dir, imageExts));
//        }
//
//        return lst;
//    }


    public static ArrayList<MediaListItem> getMediaListItemsImage(File dir) throws Exception {

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
            String[] exts) throws Exception {

        ArrayList<MediaListItem> lst = new ArrayList<>();

        lst.addAll(getMediaListItemsFromPaths(
                Utils.getAllDirectoryPaths(dir)));

        lst.addAll(getMediaListItemsFromPaths(
                Utils.getAllFilePathsWithExt(dir, exts)));

        return lst;
    }

//    private static ArrayList<MediaListItem> getSporesAsMediaListItems(
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
            ArrayList<String> pathList) throws Exception {

        ArrayList<MediaListItem> newList = new ArrayList<>();

        for(String filePath : pathList){

            MediaListItem mli;
            File f = new File(filePath);

            mli = MnemosyneRegistry.tryGetMediaListItem(f);

            newList.add(mli);
        }

        return newList;
    }

    private static ArrayList<MediaListItem> getAudioMediaListItemsFromPaths(
            ArrayList<String> paths) throws Exception {

        ArrayList<MediaListItem> newList = new ArrayList<>();

        for(String filePath : paths){

            File f = new File(filePath);

            if(isAudioFileFromPath(filePath) || f.isDirectory()) {

//                MediaListItem mli =
//                        new MediaListItem(filePath);

                MediaListItem mli = MnemosyneRegistry.tryGetMediaListItem(f);


//                MediaListItem mli;
//
//                if(f.isDirectory()){
//
//                    mli = new MediaListItem(filePath);
//
//                } else{
//
//                    mli = MnemosyneRegistry.tryGetMediaListItem(f);
//                }

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
            ArrayList<String> paths) throws Exception {

        ArrayList<MediaListItem> newList = new ArrayList<>();

        for(String filePath : paths){

            File f = new File(filePath);

            if(isImageFileFromPath(filePath) || f.isDirectory()) {

//                MediaListItem mli;
//
//                if(f.isDirectory()){
//
//                    mli = new MediaListItem(filePath);
//
//                } else{
//
//                    mli = MnemosyneRegistry.tryGetMediaListItem(f);
//                }


                MediaListItem mli = MnemosyneRegistry.tryGetMediaListItem(f);

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

        //KEEP THESE ALPHABETIZED

        lst.add(Configuration.getAudioDirectory().getAbsolutePath());
        lst.add(Configuration.getCanvasesDirectory().getAbsolutePath());
        lst.add(Configuration.getDownloadDirectory().getAbsolutePath());
        lst.add(Configuration.getPraxisAudioDirectory().getAbsolutePath());
        lst.add(Configuration.getProjectsAudioDirectory().getAbsolutePath());
        lst.add(Configuration.getRefTracksDirectory().getAbsolutePath());
        lst.add(Configuration.getStudyAudioDirectory().getAbsolutePath());
        lst.add(Configuration.getVoicememosDirectory().getAbsolutePath());

        //LEAVE THESE UN-ALPHABETIZED (currently not used in any devices, legacy code)

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

        //KEEP THESE ALPHABETIZED

        lst.add(Configuration.getCameraDirectory().getAbsolutePath());
        lst.add(Configuration.getDownloadDirectory().getAbsolutePath());
        lst.add(Configuration.getImagesDirectory().getAbsolutePath());
        lst.add(Configuration.getMemesDirectory().getAbsolutePath());
        lst.add(Configuration.getPraxisImagesDirectory().getAbsolutePath());
        lst.add(Configuration.getProjectsImagesDirectory().getAbsolutePath());

        File f = Configuration.getScreenshotDirectory();

        if(f.exists()){

            lst.add(f.getAbsolutePath());
        }

        lst.add(Configuration.getStudyImagesDirectory().getAbsolutePath());


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

    public static void exportToXml(ArrayList<Media> mediaList, NwdDb db)
            throws Exception {

        Document doc = getDocument(mediaList, db);

        File outputFile =
            Configuration.getOutgoingXmlFile_yyyyMMddHHmmss(
                    Xml.FILE_NAME_MNEMOSYNE_V5);

            Xml.write(outputFile, doc);
    }

    public static void hiveExportToXml(ArrayList<Media> mediaList,
                                       NwdDb db,
                                       Context context)
            throws Exception {

        Document doc = getDocument(mediaList, db);

        for(File outputFile :
            Configuration.getOutgoingHiveXmlFiles_yyyyMMddHHmmss(
                    context,
                    db,
                    Xml.FILE_NAME_MNEMOSYNE_V5)) {

            Xml.write(outputFile, doc);
        }

    }

    @NonNull
    private static Document getDocument(ArrayList<Media> mediaList, NwdDb db) throws Exception {
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
        return doc;
    }
}
