package com.nineworldsdeep.gauntlet;

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

    private String listName;
    private ArrayList<String> items;

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
}
