package com.nineworldsdeep.gauntlet.hive;

/**
 * Created by brent on 9/24/17.
 */

public abstract class HiveSpore {

    protected String name;

    public HiveSpore(String name){

        this.name = name;
    }

    protected HiveSpore(){


    }

    public String getName(){

        return name;
    }

    public abstract HiveSporeType getHiveSporeType();
}


