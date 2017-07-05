package com.nineworldsdeep.gauntlet.hive;

/**
 * Created by brent on 7/5/17.
 */

class Lobe {

    int hiveRootId = -1;
    String hiveRootName;
    LobeType lobeType;

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

    public LobeType getLobeType() {
        return lobeType;
    }

    public void setLobeType(LobeType lobeType) {
        this.lobeType = lobeType;
    }
}
