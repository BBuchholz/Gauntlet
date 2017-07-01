package com.nineworldsdeep.gauntlet.hive;

/**
 * Created by brent on 7/1/17.
 */

public class HiveRoot {

    int hiveRootId;
    String hiveRootName;

    public HiveRoot(){

    }

    public HiveRoot(int id, String name){
        setHiveRootId(id);
        setHiveRootName(name);
    }

    public int getHiveRootId() {
        return hiveRootId;
    }

    public void setHiveRootId(int hiveRootId) {
        this.hiveRootId = hiveRootId;
    }

    public String getHiveRootName() {
        return hiveRootName;
    }

    public void setHiveRootName(String hiveRootName) {
        this.hiveRootName = hiveRootName;
    }
}
