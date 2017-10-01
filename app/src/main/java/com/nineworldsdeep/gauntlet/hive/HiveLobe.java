package com.nineworldsdeep.gauntlet.hive;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by brent on 7/5/17.
 */

public abstract class HiveLobe {

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
    public abstract File getAssociatedDirectory();

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

    protected Iterable<File> filterDirectoryFiles(File directory,
                                                  String[] fileExtensions){

        return FileUtils.listFiles(
                directory,
                fileExtensions,
                true);
    }

    public abstract Iterable<File> siftFiles(HiveRoot hiveRoot);

    protected Iterable<File> siftFiles(HiveRoot hiveRoot, String[] fileExtensions) {

        HiveLobe lobeToSift = hiveRoot.getLobeByName(this.getHiveLobeName());

        if(lobeToSift != null) {

            return filterDirectoryFiles(
                    lobeToSift.getAssociatedDirectory(),
                    fileExtensions);

        }else{

            return new ArrayList<>();
        }

    }
}
