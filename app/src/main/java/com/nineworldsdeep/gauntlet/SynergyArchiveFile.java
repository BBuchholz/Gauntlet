package com.nineworldsdeep.gauntlet;

import android.content.Context;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Brent on 9/28/2015.
 */
public class SynergyArchiveFile extends SynergyListFile {

    public SynergyArchiveFile(String listName){
        super(listName);
    }

    public static File getSynergyArchiveFile(String listName){

        File filesDir = getSynergyArchiveDirectory();
        return new File(filesDir, listName + ".txt");
    }

    public static File getSynergyArchiveDirectory(){

        File root = Environment.getExternalStorageDirectory();
        File synergyDir = new File(root.getAbsolutePath() + "/NWD/synergy/archived");
        if(!synergyDir.exists()){
            synergyDir.mkdirs();
        }
        return synergyDir;
    }

    public void loadItems(){

        File toDoFile = getSynergyArchiveFile(listName);
        try{
            items = new ArrayList<String>(FileUtils.readLines(toDoFile));
        }catch(IOException ex){
            items = new ArrayList<>();
        }
    }

    public void save(){

        File toDoFile = getSynergyArchiveFile(listName);
        try{
            FileUtils.writeLines(toDoFile, items);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
