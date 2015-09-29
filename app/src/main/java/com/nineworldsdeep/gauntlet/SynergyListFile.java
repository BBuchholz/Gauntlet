package com.nineworldsdeep.gauntlet;

import android.content.Context;
import android.os.Environment;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brent on 9/27/2015.
 */
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

    public static void archive(String listName) {

        SynergyListFile slf = new SynergyListFile(listName);
        slf.loadItems();

        SynergyArchiveFile saf = new SynergyArchiveFile(listName);
        saf.loadItems();

        List<String> toBeRemoved = new ArrayList<>();

        for(String itm : slf.items){
            if(ListItem.isCompleted(itm)){
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
