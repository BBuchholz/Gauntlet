package com.nineworldsdeep.gauntlet.hive.experimental.dummy;

import com.nineworldsdeep.gauntlet.hive.HiveRoot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContentHiveRoots {

    private static List<HiveRoot> items = new ArrayList<>();
    private static Map<String, HiveRoot> itemMap = new HashMap<>();

    static {

        items.add(new HiveRoot(1, "test"));
        items.add(new HiveRoot(2, "test2"));

        for(HiveRoot hr : getItems()){

            itemMap.put(Integer.toString(hr.getHiveRootId()), hr);
        }
    }

    public static List<HiveRoot> getItems(){

        return items;
    }

    public static Map<String, HiveRoot> getItemMap(){

        return itemMap;
    }

}
