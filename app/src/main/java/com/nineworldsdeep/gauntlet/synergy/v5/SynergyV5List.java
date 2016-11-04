package com.nineworldsdeep.gauntlet.synergy.v5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 11/4/16.
 */
public class SynergyV5List {
    public SynergyV5List(String listName) {

    }

    public void save() {
    }

    public boolean exists() {
        return true;
    }

    public SynergyV5ListItem get(int position) {
        return null;
    }

    public String getListName() {
        return null;
    }

    public SynergyV5ListItem replace(int pos, ArrayList<SynergyV5ListItem> sliList) {

        return null;
    }

    public void archiveOne(SynergyV5ListItem replace, boolean b) {

    }

    public int size() {
        return 0;
    }

    public void move(int currentPosition, int moveToPosition) {

    }

    public boolean hasCategorizedItems() {
        return false;
    }

    public int getFirstCategorizedItemPosition() {

        return 0;
    }

    public void queueToActive(int position) {

    }

    public void shelve(int position, String category) {

    }

    public void loadItems() {

    }

    public List<SynergyV5ListItem> getItems() {
        return new ArrayList<>();
    }

    public void add(int addItemIndex, String itemText) {

    }
}
