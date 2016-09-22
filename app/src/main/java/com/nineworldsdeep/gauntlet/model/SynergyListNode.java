package com.nineworldsdeep.gauntlet.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

/**
 * Created by brent on 9/1/16.
 */
public class SynergyListNode {

    private String mName;
    private ArrayList<SynergyListItemNode> mItems =
            new ArrayList<>();

    public SynergyListNode(String name){
        mName = name;
    }

    public void add(SynergyListItemNode item){

        if(item != null && !mItems.contains(item)){

            mItems.add(item);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SynergyListNode that = (SynergyListNode) o;

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
