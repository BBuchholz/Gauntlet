package com.nineworldsdeep.gauntlet.bookSegments;

import com.nineworldsdeep.gauntlet.Fragment;

/**
 * Created by brent on 11/8/15.
 */
public class Alias extends Fragment {

    public Alias(String lineItem) {
        super(lineItem);

        processExtract("alias");
        processExtract("author");
        processExtract("title");
        processExtract("year");

        setDisplayKey("title");
    }

    @Override
    public String toString(){

        return getSingleLineDisplay();
    }

    private String getSingleLineDisplay() {

        String output =
                get("title") +
                        " by " + get("author") + " " +
                        "(" + get("year") + ") " +
                        "[" + get("alias") + "]";

        return output;
    }
}
