package com.nineworldsdeep.gauntlet.synergy.v3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 1/21/16.
 */
public class SynergyListFile {

    private String listName;

    public SynergyListFile(String listName) {
        throw new UnsupportedOperationException("prototype");
    }

    public String get(int position) {
        throw new UnsupportedOperationException("prototype");
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
        throw new UnsupportedOperationException();
    }

    public void add(SynergyListItem item) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    public void delete() {
        throw new UnsupportedOperationException();
    }
}
