package com.nineworldsdeep.gauntlet.synergy.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by brent on 10/30/15.
 */
public class SynergyUtils {

    public static List<String> getAllListNames(){

        String[] exts = {"txt"};
        List<String> lst = new ArrayList<String>();
        File synergyDir = Configuration.getSynergyDirectory();

        for (File f : FileUtils.listFiles(synergyDir, exts, false)){
            lst.add(FilenameUtils.removeExtension(f.getName()));
        }

        Collections.sort(lst);

        return lst;
    }

    public static List<String> getAllArchiveNames() {

        String[] exts = {"txt"};
        List<String> lst = new ArrayList<>();
        File archiveDir = Configuration.getArchiveDirectory();

        for (File f : FileUtils.listFiles(archiveDir, exts, false)){

            lst.add(FilenameUtils.removeExtension(f.getName()));
        }

        Collections.sort(lst);

        return lst;
    }

    public SynergyListFile generateFromTemplate(String templateName,
                                                String timeStampedListName){

        SynergyListFile slf = new SynergyListFile(timeStampedListName);
        slf.loadItems();

        SynergyTemplateFile stf = new SynergyTemplateFile(templateName);
        stf.loadItems();

        for(String itm : stf.getItems()){
            slf.addItem(itm);
        }

        return slf;
    }

    public static String getTimeStampedListName(String templateName) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        return templateName + "-" + sdf.format(new Date());
    }

    public static String push(String listName){

        if(Utils.containsTimeStamp(listName)){

            String pushToName =
                    Utils.incrementTimeStampInString_yyyyMMdd(listName);

            SynergyListFile currentFile =
                    new SynergyListFile(listName);
            currentFile.loadItems();

            SynergyListFile pushToFile =
                    new SynergyListFile(pushToName);
            pushToFile.loadItems();

            for(String itm : currentFile.getItems()){
                pushToFile.addItem(itm);
            }

            pushToFile.save();
            currentFile.delete();

            return pushToName;
        }

        return null;
    }

    public static boolean listItemIsCompleted(String itm) {

        return itm.startsWith("completed={")
                && itm.endsWith("}");
    }

    public static void archive(String listName) {
        archive(listName, false);
    }

    public static void archive(String listName, boolean expired) {

        SynergyListFile slf = new SynergyListFile(listName);
        slf.loadItems();

        SynergyArchiveFile saf = new SynergyArchiveFile(listName);
        saf.loadItems();

        List<String> toBeRemoved = new ArrayList<>();

        for(String itm : slf.getItems()){
            if(expired || SynergyUtils.listItemIsCompleted(itm)){
                toBeRemoved.add(itm);
            }
        }

        for(String itm : toBeRemoved){
            slf.removeItem(itm);
            saf.addItem(itm);
        }

        saf.save();

        if(slf.size() > 0){
            slf.save();
        }else{
            slf.delete();
        }
    }
}
