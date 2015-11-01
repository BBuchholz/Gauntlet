package com.nineworldsdeep.gauntlet.synergy.v2;

import com.nineworldsdeep.gauntlet.Configuration;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 10/30/15.
 */
public class LineItemListFile {

    private String listName;
    private File synergyFile;
    private ArrayList<String> items;

    public LineItemListFile(String name, File filesDir){

        listName = name;
        synergyFile = new File(filesDir, listName + ".txt");
    }

    public File getSynergyFile(){

        return synergyFile;
    }

    public int size(){
        return items.size();
    }

    public List<String> getItems(){
        return items;
    }

    public void addItem(String item){
        items.add(item);
    }

    public void removeItem(String item){
        items.remove(item);
    }

    public void loadItems(){

        File toDoFile = getSynergyFile();
        try{
            items = new ArrayList<String>(FileUtils.readLines(toDoFile));
        }catch(IOException ex){
            items = new ArrayList<>();
        }
    }

    public void loadItems(List<String> itemsList){

        if(items == null){
            items = new ArrayList<>();
        }

        for(String item : itemsList){
            items.add(item);
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
