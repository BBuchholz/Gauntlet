package com.nineworldsdeep.gauntlet;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Brent on 9/28/2015.
 */
public class Utils {

    public static void toast(Context c, String msg){
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean containsTimeStamp(String s) {
        Pattern p = Pattern.compile("\\d{8}");
        Matcher m = p.matcher(s);
        return m.find();
    }
}
