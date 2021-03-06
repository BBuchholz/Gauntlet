package com.nineworldsdeep.gauntlet.synergy.v1;

import android.os.Environment;

import com.nineworldsdeep.gauntlet.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Brent on 9/27/2015.
 */
@Deprecated
public class SynergyListFile {

    protected String listName;
    protected ArrayList<String> items;

    public SynergyListFile(String name){
        listName = name;
    }

    public static File getSynergyDirectory(){

        File root = Environment.getExternalStorageDirectory();
        File synergyDir = new File(root.getAbsolutePath() + "/NWD/synergy");
        if(!synergyDir.exists()){
            synergyDir.mkdirs();
        }
        return synergyDir;
    }

    public static List<String> getAllListNames(){

        String[] exts = {"txt"};
        List<String> lst = new ArrayList<String>();

        for (File f : FileUtils.listFiles(getSynergyDirectory(), exts, false)){
            lst.add(FilenameUtils.removeExtension(f.getName()));
        }

        Collections.sort(lst);

        return lst;
    }

    public void loadItems(){

        File toDoFile = getSynergyFile();
        try{
            items = new ArrayList<String>(FileUtils.readLines(toDoFile));
        }catch(IOException ex){
            items = new ArrayList<>();
        }
    }

    public File getSynergyFile(){

        File filesDir = getSynergyDirectory();
        return new File(filesDir, listName + ".txt");
    }

    public static File getSynergyFile(String listName){

        File filesDir = getSynergyDirectory();
        return new File(filesDir, listName + ".txt");
    }

    public static void generateFromTemplate(String templateName, String timeStampedListName){

        SynergyListFile slf = new SynergyListFile(timeStampedListName);
        slf.loadItems();

        SynergyTemplateFile stf = new SynergyTemplateFile(templateName);
        stf.loadItems();

        for(String itm : stf.items){
            slf.items.add(itm);
        }

        slf.save();
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

            for(String itm : currentFile.items){
                pushToFile.items.add(itm);
            }

            pushToFile.save();
            currentFile.delete();

            return pushToName;
        }

        return null;
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

        for(String itm : slf.items){
            if(expired || ListItem.isCompleted(itm)){
                toBeRemoved.add(itm);
            }
        }

        for(String itm : toBeRemoved){
            slf.items.remove(itm);
            saf.items.add(itm);
        }

        saf.save();

        if(slf.items.size() > 0){
            slf.save();
        }else{
            slf.save(); //this can be removed once delete is implemented
            slf.delete();
        }
    }

    public void delete(){
        getSynergyFile().delete();
    }

    public void save(){

        File toDoFile = getSynergyFile();
        try{
            FileUtils.writeLines(toDoFile, items);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

}
