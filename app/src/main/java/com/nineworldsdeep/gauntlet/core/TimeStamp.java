package com.nineworldsdeep.gauntlet.core;

import com.nineworldsdeep.gauntlet.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by brent on 9/15/16.
 */
public class TimeStamp {

    public static String yyyy_MM_dd_hh_mm_ss() {

        return toYyyy_MM_dd_hh_mm_ss(new Date());
    }

    public static String toYyyy_MM_dd_hh_mm_ss(Date date){

        if(date == null){
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.US);

        String output = sdf.format(date);

        return output;
    }

    public static String to_UTC_Yyyy_MM_dd_hh_mm_ss(Date date){

        if(date == null){
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format(date);
    }

    public static Date yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
            String yyyy_MM_dd_hh_mm_ss_UTC) throws ParseException {

        if(Utils.stringIsNullOrWhitespace(yyyy_MM_dd_hh_mm_ss_UTC)){
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.parse(yyyy_MM_dd_hh_mm_ss_UTC);
    }

    public static Date now(){

        return new Date();
    }
}
