package com.nineworldsdeep.gauntlet.synergy.v5;

/**
 * Created by brent on 11/4/16.
 */
public class SynergyV5ListItem {

    private String mItemValue;
    private int mItemId;

    public SynergyV5ListItem(String itemValue) {

        mItemValue = itemValue;
        mItemId = -1;
    }

    public boolean isCompleted() {
        return false;
    }

    public void markCompleted() {

    }

    public void markIncomplete() {

    }

    public String getCategory() {
        return null;
    }

    public String getItemValue() {

        return mItemValue;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        this.mItemId = itemId;
    }

    @Override
    public String toString(){

        return mItemValue;
    }
}
