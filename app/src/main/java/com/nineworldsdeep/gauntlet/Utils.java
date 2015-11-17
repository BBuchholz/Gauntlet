package com.nineworldsdeep.gauntlet;

import android.content.Context;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.FileFilter;
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

        return now.get(Calendar.DATE) - listDate.get(Calendar.DATE) > 0;
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

    public static String processExtract(String originalLineItem, String key) {

        String openTag = key + "={";

        if(originalLineItem.contains(openTag)){

            int startIdx =
                    originalLineItem.indexOf(openTag)
                            + openTag.length();
            int endIdx = originalLineItem.indexOf("}", startIdx);

            String val = originalLineItem.substring(startIdx, endIdx);

            if(val.trim().length() > 0){

                return val;
            }
        }

        return null;
    }
}
