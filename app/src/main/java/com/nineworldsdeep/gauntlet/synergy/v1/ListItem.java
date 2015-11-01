package com.nineworldsdeep.gauntlet.synergy.v1;

/**
 * Created by Brent on 9/28/2015.
 */
@Deprecated
public class ListItem {

    public static boolean isCompleted(String itm) {

        return itm.startsWith("completed={")
                && itm.endsWith("}");
    }
}
