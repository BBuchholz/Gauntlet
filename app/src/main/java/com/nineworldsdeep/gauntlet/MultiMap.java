package com.nineworldsdeep.gauntlet;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by brent on 6/12/16.
 */
public class MultiMap<K, V> {

    private HashMap<K, ArrayList<V>> keyToValueList =
            new HashMap<>();


    public void put(K key, Collection<V> values){

        for(V val : values){

            put(key, val);
        }
    }

    public ArrayList<V> get(K key){

        return keyToValueList.get(key);
    }

    public void put(K key, V value){

        if(key == null || value == null){

            return;
        }

        if(!keyToValueList.containsKey(key)){

            keyToValueList.put(key, new ArrayList<V>());
        }

        keyToValueList.get(key).add(value);
    }

    public Set<K> keySet(){

        return keyToValueList.keySet();
    }

    public boolean containsKey(K key){

        return keyToValueList.containsKey(key);
    }
}
