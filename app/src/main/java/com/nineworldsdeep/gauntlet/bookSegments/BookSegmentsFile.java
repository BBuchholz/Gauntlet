package com.nineworldsdeep.gauntlet.bookSegments;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.synergy.v2.LineItemListFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 11/8/15.
 */
public class BookSegmentsFile extends LineItemListFile {


    public BookSegmentsFile(String bookAlias) {
        super(bookAlias, Configuration.getBookSegmentsDirectory());
    }


    public List<BookSegment> getBookSegments() {

        ArrayList<BookSegment> lst =
                new ArrayList<>();

        for(String item : getItems()){
            lst.add(new BookSegment(item));
        }

        return lst;
    }

    public void addBookSegment(String strAlias,
                               String strPageRange,
                               String strSegment,
                               String strKeywords,
                               String strConferre,
                               String strNotes) {

        String entry = "alias={" + strAlias + "} ";
        entry += "pR={" + strPageRange + "} ";
        entry += "seg={" + strSegment + "} ";
        entry += "kW={" + strKeywords + "} ";
        entry += "cf={" + strConferre + "} ";
        entry += "notes={" + strNotes + "} ";

        add(entry);
    }
}
