package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by brent on 1/21/16.
 */
public class SynergyListFile {

    private String listName;
    private File synergyFile;
    private ArrayList<SynergyListItem> items;

    public SynergyListFile(String listName) {

        if(Utils.stringIsNullOrWhitespace(listName)){
            listName = "utils-BLANK-LIST";
            Utils.log("blank listname converted to '" + listName + "'");
        }

        this.listName = listName;
        items = new ArrayList<>();
        synergyFile =
                new File(Configuration.getSynergyDirectory(), listName + ".txt");
    }

    public SynergyListItem get(int position) {
        return items.get(position);
    }

    public String getListName() {
        return listName;
    }

    public SynergyListItem replace(int pos, ArrayList<SynergyListItem> lst) {

        SynergyListItem removed = items.remove(pos);

        Stack<SynergyListItem> reversedStack = new Stack<>();

        for(SynergyListItem item : lst){

            reversedStack.push(item);
        }

        while(!reversedStack.empty()){

            items.add(pos, reversedStack.pop());
        }

        return removed;
    }

    public void save() {
        try{

            ArrayList<String> lst = new ArrayList<>();

            for(SynergyListItem item : items){
                lst.add(item.toLineItem());
            }

            FileUtils.writeLines(synergyFile, lst);

        }catch(IOException ex){

            Utils.log("save error: " + ex.getMessage());
        }
    }

    public int size() {
        return items.size();
    }

    public void move(int pos, int moveToPos) {
        throw new UnsupportedOperationException("prototype");
    }

    public boolean hasCategorizedItems() {
        throw new UnsupportedOperationException("prototype");
    }

    public int getFirstCategorizedItemPosition() {

        throw new UnsupportedOperationException("prototype");
    }

    public void queueToDailyToDo(int position) {
        throw new UnsupportedOperationException("prototype");
    }

    public void shelve(int position, String shelveToListName) {
        throw new UnsupportedOperationException("prototype");
    }

    public SynergyListItem remove(int position) {
        throw new UnsupportedOperationException();
    }

    public void add(int i, String itemText){
        add(i, new SynergyListItem(itemText));
    }

    public void add(int i, SynergyListItem item) {
        items.add(i, item);
    }

    public void add(SynergyListItem item) {
        items.add(item);
    }

    public void loadItems() {

        items = new ArrayList<>();

        try{

            for(String line : FileUtils.readLines(synergyFile)){
                items.add(new SynergyListItem(line));
            }

        }catch(IOException ex){

            Utils.log("loadItems() Exception: " + ex.getMessage());
        }
    }

    public List<SynergyListItem> getItems() {
        throw new UnsupportedOperationException();
    }

    public void archiveOne(SynergyListItem itemToArchive) {
        throw new UnsupportedOperationException();
    }

    public boolean exists() {
        return synergyFile.exists();
    }

    public void delete() {
        synergyFile.delete();
    }
}
