package com.nineworldsdeep.gauntlet;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Brent on 9/28/2015.
 */
public class Utils {

    public static void toast(Context c, String msg){
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }
}
