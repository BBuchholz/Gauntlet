package com.nineworldsdeep.gauntlet;

import android.os.Environment;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by brent on 10/6/15.
 */
public class SynergyTemplateFile extends SynergyListFile {

    public SynergyTemplateFile(String listName){
        super(listName);
    }

    public static File getSynergyTemplateFile(String listName){

        File filesDir = getSynergyTemplateDirectory();
        return new File(filesDir, listName + ".txt");
    }

    public static File getSynergyTemplateDirectory(){

        File root = Environment.getExternalStorageDirectory();
        File synergyDir = new File(root.getAbsolutePath() + "/NWD/synergy/templates");
        if(!synergyDir.exists()){
            synergyDir.mkdirs();
        }
        return synergyDir;
    }

    public void loadItems(){

        File toDoFile = getSynergyTemplateFile(listName);
        try{
            items = new ArrayList<String>(FileUtils.readLines(toDoFile));
        }catch(IOException ex){
            items = new ArrayList<>();
        }
    }

    public void save(){

        File toDoFile = getSynergyTemplateFile(listName);
        try{
            FileUtils.writeLines(toDoFile, items);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static List<String> getAllTemplateNames() {

        String[] exts = {"txt"};
        List<String> lst = new ArrayList<>();

        for (File f : FileUtils.listFiles(getSynergyTemplateDirectory(),
                exts, false)){

            lst.add(FilenameUtils.removeExtension(f.getName()));
        }

        Collections.sort(lst);

        return lst;
    }
}
