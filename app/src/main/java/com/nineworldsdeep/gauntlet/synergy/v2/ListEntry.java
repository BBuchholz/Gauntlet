package com.nineworldsdeep.gauntlet.synergy.v2;

/**
 * Created by brent on 3/21/16.
 */
public class ListEntry {

    private String listName;
    private int itemCount;

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    @Override
    public String toString(){

       return getListName() + " - " + getItemCount();
    }
}
