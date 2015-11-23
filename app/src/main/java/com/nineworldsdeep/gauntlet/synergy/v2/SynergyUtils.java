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

        return getAllTextFileNamesWithoutExt(
                Configuration.getSynergyDirectory());
    }

    public static List<String> getAllArchiveNames() {

        return getAllTextFileNamesWithoutExt(
                Configuration.getArchiveDirectory());
    }

    public static List<String> getAllTemplateNames() {
        return getAllTextFileNamesWithoutExt(
                Configuration.getTemplateDirectory());
    }

    private static List<String> getAllTextFileNamesWithoutExt(File directory){

        String[] exts = {"txt"};
        return Utils.getAllFileNamesMinusExt(directory, exts);
    }

    public static SynergyListFile generateFromTemplate(String templateName,
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
//        return templateName + "-" + sdf.format(new Date());
        return sdf.format(new Date()) + "-" + templateName;
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

    public static void queue(SynergyListFile slf, int position) {

        SynergyListFile currentDailyToDo =
                new SynergyListFile(getTimeStampedListName("DailyToDo"));

        currentDailyToDo.loadItems();

        String categorizedItem =
                "::" + slf.getListName() + ":: - " + slf.remove(position);

        currentDailyToDo.add(0, categorizedItem);

        currentDailyToDo.save();
        slf.save();
    }

    public static String trimCategory(String synergyListItem){

        if(isCategorizedItem(synergyListItem)){

            int startIdx = synergyListItem.indexOf(":: - ") + 5;

            return synergyListItem.substring(startIdx);

        }else{

            return synergyListItem;
        }
    }

    public static String parseCategory(String synergyListItem) {

        if(isCategorizedItem(synergyListItem)){

            int endIdx = synergyListItem.indexOf(":: - ");

            return synergyListItem.substring(2, endIdx);

        }else{

            return null;
        }
    }

    public static boolean isCategorizedItem(String synergyListItem){

        return synergyListItem.startsWith("::") &&
                synergyListItem.contains(":: - ");
    }

    public static String markCompleted(String item){

        return "completed={" + item + "}";
    }

    public static void completeCategorizedItem(String item){

        String category = parseCategory(item);

        SynergyListFile slf = new SynergyListFile(category);
        slf.loadItems();
        slf.add(markCompleted(trimCategory(item)));
        slf.save();
    }

    public static void shelve(SynergyListFile shelveFromFile,
                              int position,
                              String category) {

        SynergyListFile shelveToFile = new SynergyListFile(category);
        shelveToFile.loadItems();

        //remove category if exists
        String itemToShelve = shelveFromFile.remove(position);

        if(isCategorizedItem(itemToShelve)){

            itemToShelve = trimCategory(itemToShelve);
        }

        //add to top of list
        shelveToFile.add(0, itemToShelve);

        shelveToFile.save();
        shelveFromFile.save();
    }
}
