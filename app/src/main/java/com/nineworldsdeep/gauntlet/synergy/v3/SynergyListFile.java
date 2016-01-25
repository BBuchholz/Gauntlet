package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    public SynergyListItem replace(int pos, ArrayList<String> lst) {
        throw new UnsupportedOperationException("prototype");
    }

    public void save() {
        throw new UnsupportedOperationException("prototype");
    }

    public int size() {
        throw new UnsupportedOperationException("prototype");
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

        throw new UnsupportedOperationException();
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
