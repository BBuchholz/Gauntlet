package com.nineworldsdeep.gauntlet.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by brent on 9/15/16.
 */
public class TimeStamp {

    public static String yyyy_MM_dd_hh_mm_ss() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.US);

        String output = sdf.format(new Date());;

        return output;
    }
}
