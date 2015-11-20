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

        if(!Utils.stringIsNullOrWhitespace(get("kW"))) {

            output += get("kW") + "; ";
        }

        if(!Utils.stringIsNullOrWhitespace(get("cf"))) {

            output += get("cf") + "; ";
        }

        return output;
    }

    public String getAlias() {
        return get("alias");
    }

    public String getPageRange(){
        return get("pR");
    }

    public String getSegment(){
        return get("seg");
    }

    public String getKeyWords(){
        return get("kW");
    }

    public String getConferre(){
        return get("cf");
    }

    public String getNotes(){
        return get("notes");
    }
}
