package com.nineworldsdeep.gauntlet.synergy.v1;

import android.os.Environment;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static List<String> getAllArchiveNames() {

        String[] exts = {"txt"};
        List<String> lst = new ArrayList<>();

        for (File f : FileUtils.listFiles(getSynergyArchiveDirectory(),
                                          exts, false)){

            lst.add(FilenameUtils.removeExtension(f.getName()));
        }

        Collections.sort(lst);

        return lst;
    }
}
