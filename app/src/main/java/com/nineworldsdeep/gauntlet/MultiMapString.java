package com.nineworldsdeep.gauntlet;

import android.text.TextUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by brent on 6/12/16.
 */
public class MultiMapString {

    private HashMap<String, HashSet<String>> stringToStringList =
            new HashMap<>();

    /**
     * will split commaStringValues using a comma as a separator
     * and will put each individual value into the supplied key
     * @param key
     * @param commaStringValues
     */
    public void putCommaStringValues(String key,
                                     String commaStringValues){

        for(String val : commaStringValues.split(",")){

            put(key, val);
        }
    }

    public void put(String key, Collection<String> values){

        for(String val : values){

            put(key, val);
        }
    }

    public Set<String> get(String key){

        return stringToStringList.get(key);
    }

    public String getAsCommaString(String key){

        return TextUtils.join(", ", get(key));
    }

    public void put(String key, String value){

        //always trim
        key = key.trim();
        value = value.trim();

        //just ignore empty values
        if(Utils.stringIsNullOrWhitespace(key) ||
                Utils.stringIsNullOrWhitespace(value)){

            return;
        }

        if(!stringToStringList.containsKey(key)){

            stringToStringList.put(key, new HashSet<String>());
        }

        stringToStringList.get(key).add(value);
    }

    public Set<String> keySet(){

        return stringToStringList.keySet();
    }

    public boolean containsKey(String key){

        return stringToStringList.containsKey(key);
    }
}
