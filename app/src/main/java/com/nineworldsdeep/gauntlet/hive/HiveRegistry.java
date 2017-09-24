package com.nineworldsdeep.gauntlet.hive;

import java.util.HashMap;

/**
 * Created by brent on 9/24/17.
 */

class HiveRegistry {

    private static HashMap<String, HiveRoot> hiveRoots;
    private static HashMap<String, HiveLobe> hiveLobes;

    static {

        hiveLobes = new HashMap<>();
        hiveRoots = new HashMap<>();
    }

    /**
     * key will be hiveRootName, converted to lowercase
     * @param root
     */
    public static void registerRoot(HiveRoot root) {

        hiveRoots.put(root.getHiveRootName().toLowerCase(), root);
    }

    /**
     * key will be in format "hiveRootName/lobeName" (e.g. "test-root/xml")
     * converted to lowercase
     * @param lobe
     */
    public static void registerLobe(HiveLobe lobe){

        hiveLobes.put(getLobeKey(lobe), lobe);
    }

    public static boolean hasRootRegistered(String rootName){

        return hiveRoots.containsKey(rootName.toLowerCase());
    }

    public static boolean hasLobeRegistered(String lobeKey){

        return hiveLobes.containsKey(lobeKey);
    }

    public static String getLobeKey(HiveLobe lobe){

        return lobe.getHiveRoot().getHiveRootName() + "/" +
                lobe.getHiveLobeName();
    }

    public static HiveRoot getHiveRoot(String hiveRootKey){

        return hiveRoots.get(hiveRootKey);
    }

    public static HiveLobe getHiveLobe(String hiveLobeKey){

        return hiveLobes.get(hiveLobeKey);
    }
}
