package com.nineworldsdeep.gauntlet.bookSegments;

import com.nineworldsdeep.gauntlet.Fragment;
import com.nineworldsdeep.gauntlet.Utils;

/**
 * Created by brent on 11/8/15.
 */
public class BookSegment extends Fragment {

    public BookSegment(String lineItem) {
        super(lineItem);

        processExtract("alias");
        processExtract("pR");
        processExtract("seg");
        processExtract("kW");
        processExtract("cf");
        processExtract("notes");

        setDisplayKey("seg");
    }

    @Override
    public String toString(){

        return getSingleLineDisplay();
    }

    private String getSingleLineDisplay() {

        String output = "";

        output += "(" + get("pR") + ") ";

        if(!Utils.stringIsNullOrWhitespace(get("seg"))){

            output += get("seg") + "; ";
        }

        output += get("kW");

        return output;
    }
}
