package com.nineworldsdeep.gauntlet.tapestry.v2;

import com.nineworldsdeep.gauntlet.datamaps.DataMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 8/11/16.
 */
public class NodeComparer {

    // conventions for comparison
    // primary colors (Blue, Red, Yellow) and their combinations

    // pattern follows color rules, in a circle, with red at top and
    // green at bottom defining the equal/inequal axis with regards
    // to Blue on the left and Yellow on the right:

    // Neither Supercedes (both need upsert on Truing) = Red

    // Blue needs upsert on Truing,
    // but Yellow supercedes = Purple (Blue towards Red)

    // Yellow needs upsert on Truing,
    // but Blue supercedes = Orange (Yellow towards Red)

    // Neither needs upsert on Truing (both "supercede" the other) = Green

    public static List<NodeComparison> compare(DataMapper leftNode, //blue
                                               DataMapper rightNode, //yellow
                                               NodeComparisonType nct) {

        List<NodeComparison> lst = new ArrayList<>();



        return generateMockup();
    }

    private static List<NodeComparison> generateMockup(){

        ArrayList<NodeComparison> lst =
                new ArrayList<>();

        //match (both same)
        lst.add(new NodeComparison(NodeComparisonResult.GreenUpsertNeither,
                "Green HERE\n" +
                "MORE HERE\nAND HERE\nLETS SEE\nHOW MUCH"));
        //variation (both different but neither empty)
        lst.add(new NodeComparison(NodeComparisonResult.RedUpsertBoth,
                "Red HERE\n" +
                "MORE HERE\nAND HERE\nLETS SEE\nHOW MUCH"));
        //file
        lst.add(new NodeComparison(NodeComparisonResult.PurpleUpsertBlueLeft,
                "Purple HERE\n" +
                "MORE HERE\nAND HERE\nLETS SEE\nHOW MUCH"));
        //only in db
        lst.add(new NodeComparison(NodeComparisonResult.OrangeUpsertYellowRight,
                "Orange HERE\n" +
                "MORE HERE\nAND HERE\nLETS SEE\nHOW MUCH"));

        return lst;
    }
}
