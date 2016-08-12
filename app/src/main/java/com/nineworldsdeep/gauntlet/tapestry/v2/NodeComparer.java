package com.nineworldsdeep.gauntlet.tapestry.v2;

import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 8/11/16.
 */
public class NodeComparer {

    public static List<RelationComparison> compare(TrueableNode leftNode, TrueableNode rightNode) {

        List<RelationComparison> lst = new ArrayList<>();



        return generateMockup();
    }

    private static List<RelationComparison> generateMockup(){

        ArrayList<RelationComparison> lst =
                new ArrayList<>();

        //match (both same)
        lst.add(new RelationComparison("MATCH", "DETAILS HERE\n" +
                "MORE HERE\nAND HERE\nLETS SEE\nHOW MUCH"));
        //variation (both different but neither empty)
        lst.add(new RelationComparison("VARIATION", "DETAILS HERE\n" +
                "MORE HERE\nAND HERE\nLETS SEE\nHOW MUCH"));
        //file
        lst.add(new RelationComparison("ONLY: File", "DETAILS HERE\n" +
                "MORE HERE\nAND HERE\nLETS SEE\nHOW MUCH"));
        //only in db
        lst.add(new RelationComparison("ONLY: Db", "DETAILS HERE\n" +
                "MORE HERE\nAND HERE\nLETS SEE\nHOW MUCH"));

        return lst;
    }
}
