package com.nineworldsdeep.gauntlet;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Brent on 9/28/2015.
 */
public class Utils {

    public static void toast(Context c, String msg){
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public static Date extractTimeStamp_yyyyMMdd(String s){

        Pattern p = Pattern.compile("\\d{8}");
        Matcher m = p.matcher(s);

        //validate by parsing date?
        if(m.find()){

            String dateStr = m.group();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            try {
                Date d = sdf.parse(dateStr);
                return d;
            }
            catch(ParseException pe) {

                return null;
            }
        }

        return null;
    }

    public static boolean containsTimeStamp(String s) {

        return extractTimeStamp_yyyyMMdd(s) != null;
    }

    public static String incrementTimeStampInString_yyyyMMdd(String listName) {

        Date pushFrom = Utils.extractTimeStamp_yyyyMMdd(listName);

        Calendar cal = Calendar.getInstance();
        cal.setTime(pushFrom);
        cal.add(Calendar.DATE, 1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

        String newTimeStamp = sdf.format(cal.getTime());
        String oldTimeStamp = sdf2.format(pushFrom);

        return listName.replace(oldTimeStamp, newTimeStamp);
    }
}
