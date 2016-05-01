package com.nineworldsdeep.gauntlet.synergy.v2;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListItem;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public static List<ListEntry> getAllListEntries(){

        List<ListEntry> lst = new ArrayList<>();

        for(String listName : getAllListNames()){

            ListEntry le = new ListEntry();
            le.setListName(listName);
            le.setItemCount(getSynergyListItemCount(listName));
            lst.add(le);
        }

        return lst;
    }

    private static int getSynergyListItemCount(String listName) {

        int count = 0;

        File f = getSynergyListFile(listName);

        try{

            count = FileUtils.readLines(f).size();

        }catch(Exception ex){

            //silently ignore
        }

        return count;
    }

    private static File getSynergyListFile(String listName) {
        return new File(Configuration.getSynergyDirectory(), listName + ".txt");
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

    public static String getCurrentTimeStamp_yyyyMMddHHmmss()
    {
        return getCurrentTimeStamp_yyyyMMddHHmmss(false);
    }

    public static String getCurrentTimeStamp_yyyyMMddHHmmss(boolean asTimeStampKeyVal) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

        String output = sdf.format(new Date());;

        if(asTimeStampKeyVal){

            output = "timeStamp={" + output + "}";
        }

        return output;
    }

    public static String getTimeStampedListName(String templateName) {

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
////        return templateName + "-" + sdf.format(new Date());
//        return sdf.format(new Date()) + "-" + templateName;

        return Utils.getCurrentTimeStamp_yyyyMMdd() + "-" + templateName;
    }

    public static String push(String listName){

        if(Utils.containsTimeStamp(listName)){

            String pushToName =
                    Utils.incrementTimeStampInString_yyyyMMdd(listName);

            SynergyListFile currentFile =
                    new SynergyListFile(listName);
            currentFile.loadItems();

            //shelve any categorized items (related to github issue #48)
            //see comment to v2.SynergyUtils.archive()
            SynergyUtils.shelveAllCategorized(currentFile);

            //reload after shelve
            currentFile.loadItems();

            SynergyListFile pushToFile =
                    new SynergyListFile(pushToName);
            pushToFile.loadItems();

            for(String itm : currentFile.getItems()){
                pushToFile.addItem(itm);
            }

            pushToFile.save();
            archive(currentFile.getListName(), true);

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

    /**
     * archives a single list item, for the given list name.
     * any associated Synergy file is left alone, and if the
     * desire is to remove the item from another list, that
     * must be handled seperately.
     * @param listName
     * @param item
     */
    public static String archiveOne(String listName, String item){

        SynergyArchiveFile saf = new SynergyArchiveFile(listName);
        saf.loadItems();
        saf.add(item);
        saf.save();

        return item;
    }

    public static void archive(String listName, boolean listIsExpired) {

        SynergyListFile slf = new SynergyListFile(listName);
        slf.loadItems();

        SynergyArchiveFile saf = new SynergyArchiveFile(listName);
        saf.loadItems();

        List<String> toBeRemoved = new ArrayList<>();

        for(String itm : slf.getItems()){
            if(listIsExpired || SynergyUtils.listItemIsCompleted(itm)){
                //this prevents an archive bug, github issue #48
                //will simply not remove anything that is incomplete & categorized
                //ignores completed items, even if categorized, but
                //since v3 is underway, this is a negligible for now
                //a bit hackish but can be fixed once v3 is fully implemented
                //for now, user should use "shelve all categorized items" from menu
                if(!isCategorizedItem(itm)){
                    toBeRemoved.add(itm);
                }
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

    public static void queue(LineItemListFile lf,
                             int position,
                             boolean keepOriginal,
                             String queueToName) {

        SynergyListFile currentDailyToDo =
                new SynergyListFile(getTimeStampedListName(queueToName));

        currentDailyToDo.loadItems();

//        String categorizedItem =
//                "::" + lf.getListName() + ":: - ";

        String categorizedItem;

        if(keepOriginal){

            categorizedItem = lf.get(position);

        }else{

            categorizedItem =
                    "::" + lf.getListName() + ":: - " + lf.remove(position);
        }

        currentDailyToDo.add(0, categorizedItem);

        currentDailyToDo.save();
        lf.save();
    }

    public static void queueToDailyToDo(SynergyListFile slf, int position){

        queue(slf, position, false, "DailyToDo");
    }

    public static void queueFromTemplate(SynergyTemplateFile stf, int position){

        queue(stf, position, true, stf.getListName());
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

    public static void shelveAllCategorized(SynergyListFile slf){

        while(hasCategorizedItems(slf)){

            int pos =
                    SynergyUtils.getFirstCategorizedItemPosition(slf);

            String category = parseCategory(slf.get(pos));

            shelve(slf, pos, category);
        }
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

    public static boolean hasCategorizedItems(SynergyListFile slf) {

        return getFirstCategorizedItemPosition(slf) > -1;
    }

    public static int getFirstCategorizedItemPosition(SynergyListFile slf) {

        for(int i = 0; i < slf.size(); i++){

            if(isCategorizedItem(slf.get(i))){

                return i;
            }
        }

        return -1;
    }

    public static void move(SynergyListFile slf, int pos, String moveToListName) {

        SynergyListFile moveToFile = new SynergyListFile(moveToListName);

        moveToFile.loadItems();

        archiveOne(slf.getListName(), slf.get(pos));
        moveToFile.add(0, slf.remove(pos));

        moveToFile.save();
        slf.save();
    }

    public static boolean listItemIsCompleted(SynergyListItem synergyListItem) {
        throw new UnsupportedOperationException("prototype");
    }

}
