package com.nineworldsdeep.gauntlet.hive;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

/**
 * Created by brent on 7/5/17.
 */

public abstract class HiveLobe {

//    protected int hiveRootId = -1;
//    protected String hiveRootName;
    //protected HiveLobeType hiveLobeType;

    protected String hiveLobeName;
    protected HiveRoot hiveRoot;

    protected ArrayList<HiveSpore> sporesInternal;

    public HiveLobe(String name, HiveRoot hr){

        hiveLobeName = name;
        hiveRoot = hr;
        sporesInternal = new ArrayList<>();
    }

    public Iterable<HiveSpore> getSpores(){

        return sporesInternal;
    }

    public HiveRoot getHiveRoot(){ return hiveRoot; }

    public String getHiveLobeName(){ return hiveLobeName; }

    public void add(HiveSpore spore){

        if(!sporesInternal.contains(spore)){

            sporesInternal.add(spore);
        }
    }

    public abstract void collect();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HiveLobe that = (HiveLobe) o;

        return new EqualsBuilder()
                .append(HiveRegistry.getLobeKey(this),
                        HiveRegistry.getLobeKey(that))
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(HiveRegistry.getLobeKey(this))
                .toHashCode();
    }

//
//    public int getHiveRootId() {
//        return hiveRoot.getHiveRootId();
//    }

//    public void setHiveRootId(int hiveRootId) {
//        this.hiveRootId = hiveRootId;
//    }

//    public String getHiveRootName() {
//        return hiveRootName;
//    }

//    public void setHiveRootName(String hiveRootName) {
//        this.hiveRootName = hiveRootName;
//    }

//    public HiveLobeType getHiveLobeType() {
//        return hiveLobeType;
//    }
//
//    public void setHiveLobeType(HiveLobeType hiveLobeType) {
//        this.hiveLobeType = hiveLobeType;
//    }
}