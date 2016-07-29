package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.LineItemListFile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brent on 4/30/16.
 */
public class ConfigFile extends HashMap<String, String> {

    private File mFile = null;

    public ConfigFile() {

        LineItemListFile lilf =
                new LineItemListFile("ConfigFile",
                        Configuration.getConfigDirectory());

        mFile = lilf.getSynergyFile();

        //ensure without overwriting
        if(!lilf.exists()){
            lilf.save();
        }

        lilf.loadItems();

        HashMap<String, String> temp =
                lilf.toHashMap();

        for(String key : temp.keySet()){

            put(key, temp.get(key));
        }
    }

    public boolean hasDeviceName(){

        return containsKey("deviceName") &&
                !Utils.stringIsNullOrWhitespace(get("deviceName"));
    }

    public String getDeviceName(){

        String name = null;

        if(hasDeviceName()){
            name = get("deviceName");
        }

        return name;
    }

    public void setDeviceName(String name){

        put("deviceName", name);
    }

    public void save(){

        ArrayList<String> items = new ArrayList<>();

        for(String key : keySet()){
            items.add(key + "={" + get(key) + "} ");
        }

        try{
            FileUtils.writeLines(mFile, items);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
