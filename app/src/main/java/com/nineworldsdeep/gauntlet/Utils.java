package com.nineworldsdeep.gauntlet;

import android.content.Context;
import android.widget.Toast;

import com.nineworldsdeep.gauntlet.synergy.v2.SynergyListFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Brent on 9/28/2015.
 */
public class Utils {

    public static void toast(Context c, String msg){
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public static List<String> getAllFileNamesWithExt(File dir, String[] exts) {
        List<String> lst = new ArrayList<>();;

        for (File f : FileUtils.listFiles(dir, exts, false)){

            lst.add(f.getName());
        }

        Collections.sort(lst);

        return lst;
    }

    public static List<String> getAllFilePathsWithExt(File dir, String[] exts) {

        List<String> lst = new ArrayList<>();;

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

    public static List<String> getAllDirectoryPaths(File dir) {

        List<String> lst = new ArrayList<>();;

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

                //TODO: implement this as one option for NWD integrated logging
                SynergyListFile slf = new SynergyListFile("Utils-Log");
                slf.add(pe.getMessage());
                slf.save();

                return null;
            }
            catch(Exception e) {

                //TODO: implement this as one option for NWD integrated logging
                SynergyListFile slf = new SynergyListFile("Utils-Log");
                slf.add(e.getMessage());
                slf.save();

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

    public static String computeSHA1(String path) throws Exception {

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
