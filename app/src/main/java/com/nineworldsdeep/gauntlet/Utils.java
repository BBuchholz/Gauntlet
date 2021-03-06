package com.nineworldsdeep.gauntlet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.synergy.v2.LineItemListFile;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyListFile;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Brent on 9/28/2015.
 */
public class Utils {

    /**
     * appends string msg to synergy list log file "yyyyMMdd-utils-log"
     * and raises a toast informing the user
     * @param msg
     */
    public static void log(Context c, String msg){
        String logName = SynergyUtils.getTimeStampedListName("utils-log");
        SynergyListFile slf = new SynergyListFile(logName);
        slf.loadItems();
        slf.add(msg);
        slf.save();

        if(c != null){

            toast(c, "error logged in: " + logName);
        }
    }

    public static void copyToClipboard(Context ctx,
                                       String stringLabel,
                                       String stringToCopy) {

        ClipboardManager clipboard =
                (ClipboardManager) ctx.getSystemService(ctx.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(stringLabel, stringToCopy);
        clipboard.setPrimaryClip(clip);
    }

    /**
     *
     * @param msg
     * @deprecated use log(Context,String)
     */
    @Deprecated
    public static void log(String msg){

        log(null, msg);
    }

    public static void toast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static List<String> getAllFileNamesWithExt(File dir, String[] exts) {
        List<String> lst = new ArrayList<>();;

        for (File f : FileUtils.listFiles(dir, exts, false)){

            lst.add(f.getName());
        }

        Collections.sort(lst);

        return lst;
    }

    public static List<String> getToBeRemovedPlaylists(){

        String[] exts = {"m3u8"};

        List<String> playlists =
                getAllFileNamesWithExt(Configuration.getPlaylistsDirectory(), exts);

        List<String> toBeRemoved = new ArrayList<>();

        for(String playlistName : playlists){

            if(playlistName.startsWith("to be removed")){

                toBeRemoved.add(playlistName);
            }
        }

        return toBeRemoved;
    }

    public static ArrayList<String> getAllFilePathsWithExt(File dir, String[] exts) {

        ArrayList<String> lst = new ArrayList<>();;

        for (File f : FileUtils.listFiles(dir, exts, false)){

            lst.add(f.getAbsolutePath());
        }

        Collections.sort(lst);

        return lst;
    }

    public static List<String> getAllDirectoryNames(File dir) {

        List<String> lst = new ArrayList<>();;

        for (File d : dir.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)){

            lst.add(d.getName());
        }

        Collections.sort(lst);

        return lst;
    }

    public static ArrayList<String> getAllDirectoryPaths(File dir) {

        ArrayList<String> lst = new ArrayList<>();

        for (File d : dir.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)){

            lst.add(d.getAbsolutePath());
        }

        Collections.sort(lst);

        return lst;
    }

    public static List<String> getAllFileNamesMinusExt(File dir, String[] exts){
        List<String> lst = new ArrayList<>();;

        for (File f : FileUtils.listFiles(dir, exts, false)){

            lst.add(FilenameUtils.removeExtension(f.getName()));
        }

        Collections.sort(lst);

        return lst;
    }

    public static Date extractTimeStamp_yyyyMMdd(String s){

        Pattern p = Pattern.compile("\\d{8}");
        Matcher m = p.matcher(s);

        //validate by parsing date?
        if(m.find()){

            String dateStr = m.group();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            try {
                Date d = sdf.parse(dateStr);
                return d;
            }
            catch(ParseException pe) {

                log("Utils.extractTimeStamp_yyyyMMdd() error: " + pe.getMessage());

                return null;
            }
            catch(Exception e) {

                log("Utils.extractTimeStamp_yyyyMMdd() error: " + e.getMessage());

                return null;
            }
        }

        return null;
    }

    public static boolean containsTimeStamp(String s) {

        return extractTimeStamp_yyyyMMdd(s) != null;
    }

    public static String incrementTimeStampInString_yyyyMMdd(String listName) {

        Date pushFrom = Utils.extractTimeStamp_yyyyMMdd(listName);

        Calendar cal = Calendar.getInstance();
        cal.setTime(pushFrom);
        cal.add(Calendar.DATE, 1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

        String newTimeStamp = sdf.format(cal.getTime());
        String oldTimeStamp = sdf2.format(pushFrom);

        return listName.replace(oldTimeStamp, newTimeStamp);
    }

    public static boolean isTimeStampExpired_yyyyMMdd(String listName) {

        if(!containsTimeStamp(listName)){
            return false;
        }

        Calendar now = Calendar.getInstance();
        Calendar listDate = Calendar.getInstance();
        listDate.setTime(Utils.extractTimeStamp_yyyyMMdd(listName));

        if(now.get(Calendar.YEAR) > listDate.get(Calendar.YEAR)){

            return true;

        }

        if(now.get(Calendar.YEAR) == listDate.get(Calendar.YEAR) &&
                now.get(Calendar.MONTH) > listDate.get(Calendar.MONTH)){

            return true;
        }

        if(now.get(Calendar.YEAR) == listDate.get(Calendar.YEAR) &&
                now.get(Calendar.MONTH) == listDate.get(Calendar.MONTH) &&
                now.get(Calendar.DATE) > listDate.get(Calendar.DATE)){

            return true;
        }

        return false;
    }

    public static boolean stringIsNullOrWhitespace(String s) {

        if(s == null){
            return true;
        }

        if(s.trim().isEmpty()){
            return true;
        }

        return false;
    }

    public static String processExtract(String input, String key){
        Parser p = new Parser();

        return p.extract(key, input);
    }

    public static String computeSha1HashForText(String text) throws Exception{

        MessageDigest md = MessageDigest.getInstance("SHA1");

        md.update(text.getBytes("iso-8859-1"), 0, text.length());

        byte[] mdBytes = md.digest();

        //convert the byte to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdBytes.length; i++) {
            sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static String computeSHA1(String path) throws Exception {

        File f = new File(path);
        if(!f.exists()){

            return "";
        }

        //from: http://www.mkyong.com/java/how-to-generate-a-file-checksum-value-in-java/

        MessageDigest md = MessageDigest.getInstance("SHA1");
        FileInputStream fis = new FileInputStream(path);
        byte[] dataBytes = new byte[1024];

        int nread = 0;

        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        };

        byte[] mdbytes = md.digest();

        //convert the byte to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static String toPascalCase(String s) {
        return WordUtils.capitalizeFully(s).replace(" ", "");
    }

    public static String removeHardReturns(String rawText) {

        //remove all hard returns, replace with spaces
        rawText = rawText.replace(System.getProperty("line.separator"), " ");

        //reduce all consecutive spaces with a single space
        while(rawText.contains("  ")){

            rawText = rawText.replace("  ", " ");
        }

        return rawText;
    }

    public static String incrementTimeStampInStringToCurrent_yyyyMMdd(String listName) {

        Date pushFrom = Utils.extractTimeStamp_yyyyMMdd(listName);

        //defaults to current date and time
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

        String newTimeStamp = sdf.format(cal.getTime());
        String oldTimeStamp = sdf2.format(pushFrom);

        return listName.replace(oldTimeStamp, newTimeStamp);
    }

    public static String processName(String input) {

        input = Utils.removeHardReturns(input);
        input = input.trim();

        // if trimmed name contains spaces, convert
        // to PascalCase-RespectingDashes
        if(input.contains(" ") || Utils.stringIsAllLower(input)){

            //respect dashes
            input = input.replace("-", " - ");

            //remove double spaces
            while(input.contains("  ")){

                input = input.replace("  ", " ");
            }

            input = toPascalCase(input);
        }

        //if a name doesn't contain spaces,
        //we assume the user inteded any capitalization
        //within it (eg. we don't want ThisListExample to
        //become Thislistexample)

        return input;
    }

    private static boolean stringIsAllLower(String input) {

        //remove all whitespace prior to testing
        String s = input.replace(" ", "");

        for (int i=0; i<s.length(); i++)
        {
            if (Character.isUpperCase(s.charAt(i)))
            {
                return false;
            }
        }

        return true;
    }

    public static HashMap<String, String> fromLineItemConfigFile(String fileNameWithoutExtension) {

        LineItemListFile lilf =
                new LineItemListFile(fileNameWithoutExtension,
                Configuration.getConfigDirectory());

        //ensure without overwriting
        if(!lilf.exists()){
            lilf.save();
        }

        lilf.loadItems();

        return lilf.toHashMap();
    }

    public static String getCurrentTimeStamp_yyyyMMdd() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        return sdf.format(new Date());
    }

    public static String trimTimeStamp_yyyyMMdd(String listName) {

        if(Utils.containsTimeStamp(listName)){

            Date foundTimeStamp = Utils.extractTimeStamp_yyyyMMdd(listName);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            String timeStampPrefix = sdf.format(foundTimeStamp) + "-";

            listName = listName.replace(timeStampPrefix, "");
        }

        return listName;
    }


//    @Deprecated
//    public static String processExtractOld(String originalLineItem, String key) {
//
//        String openTag = key + "={";
//
//        if(originalLineItem.contains(openTag)){
//
//            int startIdx =
//                    originalLineItem.indexOf(openTag)
//                            + openTag.length();
//            int endIdx = originalLineItem.indexOf("}", startIdx);
//
//            String val = originalLineItem.substring(startIdx, endIdx);
//
//            if(val.trim().length() > 0){
//
//                return val;
//            }
//        }
//
//        return null;
//    }

}
