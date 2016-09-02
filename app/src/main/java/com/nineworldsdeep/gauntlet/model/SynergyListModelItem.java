package com.nineworldsdeep.gauntlet.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

/**
 * Created by brent on 9/1/16.
 */
public class SynergyListModelItem {

    private String mName;
    private ArrayList<SynergyListItemModelItem> mItems =
            new ArrayList<>();

    public SynergyListModelItem(String name){
        mName = name;
    }

    public void add(SynergyListModelItem item){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SynergyListModelItem that = (SynergyListModelItem) o;

        return new EqualsBuilder()
                .append(mName, that.mName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(mName)
                .toHashCode();
    }
}
