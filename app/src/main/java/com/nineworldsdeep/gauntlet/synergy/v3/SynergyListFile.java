package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by brent on 1/21/16.
 */
public class SynergyListFile {

    private String listName;
    private File synergyFile;
    private ArrayList<SynergyListItem> items;

    protected SynergyListFile(String listName, File synergyFile) {

        if(Utils.stringIsNullOrWhitespace(listName)){
            listName = "utils-BLANK-LIST";
            Utils.log("blank listname converted to '" + listName + "'");
        }

        this.listName = listName;
        items = new ArrayList<>();
        this.synergyFile = synergyFile;
    }

    public SynergyListFile(String listName){
        this(listName,
                new File(Configuration.getSynergyDirectory(), listName + ".txt"));
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

    public void move(int moveFromPos, int moveToPos) {
        items.add(moveToPos, items.remove(moveFromPos));
    }

    public boolean hasCategorizedItems() {
        return getFirstCategorizedItemPosition() > -1;
    }

    public int getFirstCategorizedItemPosition() {

        for(int i = 0; i < size(); i++){

            if(get(i).isCategorizedItem()){

                return i;
            }
        }

        return -1;
    }

    public void queueToDailyToDo(int position) {

        queue(position, false, "DailyToDo");
    }

    private void queue(int position, boolean keepOriginal, String queueToName) {

        SynergyListFile queueToFile =
                new SynergyListFile(
                        SynergyUtils.getTimeStampedListName(queueToName));

        queueToFile.loadItems();

        SynergyListItem categorizedItem;

        if(keepOriginal){

            categorizedItem = new SynergyListItem(getListName(), get(position));

        }else{

            categorizedItem = new SynergyListItem(getListName(), remove(position));
        }

        queueToFile.add(0, categorizedItem);

        queueToFile.save();
        save();
    }

    public void shelve(int position, String shelveToListName) {

        SynergyListFile shelveToFile = new SynergyListFile(shelveToListName);
        shelveToFile.loadItems();

        SynergyListItem itemToShelve = remove(position);

        if(itemToShelve.isCategorizedItem()){
            itemToShelve.trimCategory();
        }

        //add to top of list
        shelveToFile.add(0, itemToShelve);

        shelveToFile.save();
        save();
    }

    public SynergyListItem remove(int position) {
        return items.remove(position);
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

            if(exists()) {

                for (String line : FileUtils.readLines(synergyFile)) {
                    items.add(new SynergyListItem(line));
                }
            }

        }catch(IOException ex){

            Utils.log("loadItems() Exception: " + ex.getMessage());
        }
    }

    public List<SynergyListItem> getItems() {

        //TODO: this is a hack
        // it would be cool to encapsulate this somehow
        // to setup some sort of constructor that would
        // instantiate different subclasses based on
        // list name, for now this is a hack
        if(listName.equalsIgnoreCase("Fragments")){

            Collections.shuffle(items);
            return items;
        }

        return items;
    }

    /**
     * saves the specified item to the archive file
     * for this list. Only removes from this list if
     * the removeItem parameter is set to true
     * @param itemToArchive
     * @return archived item (Note, it is unchanged in this process,
     * this is a pass-through convenience process)
     */
    public SynergyListItem archiveOne(SynergyListItem itemToArchive, boolean removeItem) {
        SynergyArchiveFile saf = new SynergyArchiveFile(getListName());
        saf.loadItems();
        saf.add(itemToArchive);
        saf.save();

        if(removeItem && getItems().contains(itemToArchive)){
            remove(itemToArchive);
        }

        return itemToArchive;
    }

    public void remove(SynergyListItem itm) {

        int position = -1;

        for(int i = 0; i < items.size(); i++){

            if(get(i).equals(itm)){
                position = i;
            }
        }

        if(position > -1){
            remove(position);
        }

    }

    public boolean exists() {
        return synergyFile.exists();
    }

    public void delete() {
        synergyFile.delete();
    }

    public SynergyListItem getByDeCategorizedItemText(String itemText) {

        for(SynergyListItem sli : items){

            if(SynergyUtils.trimCategory(sli.getText()).equalsIgnoreCase(itemText)){

                return sli;
            }
        }

        return null;
    }

    public boolean containsByDeCategorizedItemText(String itemText) {

        return getByDeCategorizedItemText(itemText) != null;
    }

}
