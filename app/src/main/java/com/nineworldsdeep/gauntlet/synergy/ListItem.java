package com.nineworldsdeep.gauntlet.synergy;

/**
 * Created by Brent on 9/28/2015.
 */
public class ListItem {

    public static boolean isCompleted(String itm) {

        return itm.startsWith("completed={")
                && itm.endsWith("}");
    }
}
