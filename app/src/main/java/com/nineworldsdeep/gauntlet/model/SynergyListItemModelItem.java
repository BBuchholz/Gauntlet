package com.nineworldsdeep.gauntlet.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

/**
 * Created by brent on 9/1/16.
 */
public class SynergyListItemModelItem {

    private String mItemText;
    private ArrayList<SynergyListModelItem> mParentLists =
            new ArrayList<>();

    public SynergyListItemModelItem(SynergyListModelItem list, String itemText){

        mItemText = itemText;
        mParentLists.add(list);
    }

    public String getItemText() {
        return mItemText;
    }

    public ArrayList<SynergyListModelItem> getParentLists() {
        return mParentLists;
    }

    /**
     * will add list if it doesn't already exist in the list of parents
     * @param list
     */
    public void addParentList(SynergyListModelItem list){

        if(list != null && !mParentLists.contains(list)){

            mParentLists.add(list);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SynergyListItemModelItem that = (SynergyListItemModelItem) o;

        return new EqualsBuilder()
                .append(mItemText, that.mItemText)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(mItemText)
                .toHashCode();
    }
}
