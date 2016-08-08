package com.nineworldsdeep.gauntlet.tapestry.v2;

/**
 * Created by brent on 8/8/16.
 */
public class RelationComparison {

    private String comparisonResult;
    private String multilineDetails;

    public RelationComparison(String comparisonResult, String multilineDetails){

        this.comparisonResult = comparisonResult;
        this.multilineDetails = multilineDetails;
    }

    public String getComparisonResult() {

        return comparisonResult;
    }

    public String getMultilineDetails() {

        return multilineDetails;
    }
}
