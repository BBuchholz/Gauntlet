package com.nineworldsdeep.gauntlet.muse;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

/**
 * Created by brent on 11/28/15.
 */
public class MuseUtils {
    public static String toNoteString(boolean[] noteArray) {

        boolean firstNote = true;
        String output = "noteArray={";

        for(int i = 0; i < noteArray.length; i++){

            if(noteArray[i]){

                if(firstNote){
                   firstNote = false;
                }else{
                   output += ", ";

                }
                output += parseNoteVal(i);
            }
        }

        output += "}";

        return output;
    }

    private static String parseNoteVal(int noteVal) {

        NoteHelper nh = new NoteHelper();

        return nh.toNoteName(noteVal);
    }

    public static String getCurrentSessionName() {

        String currentTimestamp = SynergyUtils.getCurrentTimeStamp_yyyyMMdd();
        String sessionName = "Session-" + currentTimestamp;

        return sessionName;
    }
}
