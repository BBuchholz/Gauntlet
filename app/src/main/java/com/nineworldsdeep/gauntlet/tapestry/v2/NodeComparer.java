package com.nineworldsdeep.gauntlet.tapestry.v2;

import com.nineworldsdeep.gauntlet.datamaps.DataMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 8/11/16.
 */
public class NodeComparer {

    public static List<NodeComparison> compare(DataMapper leftNode,
                                               DataMapper rightNode,
                                               NodeComparisonType nct) {

        List<NodeComparison> lst = new ArrayList<>();



        return generateMockup();
    }

    private static List<NodeComparison> generateMockup(){

        ArrayList<NodeComparison> lst =
                new ArrayList<>();

        //match (both same)
        lst.add(new NodeComparison("MATCH", "DETAILS HERE\n" +
                "MORE HERE\nAND HERE\nLETS SEE\nHOW MUCH"));
        //variation (both different but neither empty)
        lst.add(new NodeComparison("VARIATION", "DETAILS HERE\n" +
                "MORE HERE\nAND HERE\nLETS SEE\nHOW MUCH"));
        //file
        lst.add(new NodeComparison("ONLY: File", "DETAILS HERE\n" +
                "MORE HERE\nAND HERE\nLETS SEE\nHOW MUCH"));
        //only in db
        lst.add(new NodeComparison("ONLY: Db", "DETAILS HERE\n" +
                "MORE HERE\nAND HERE\nLETS SEE\nHOW MUCH"));

        return lst;
    }
}
