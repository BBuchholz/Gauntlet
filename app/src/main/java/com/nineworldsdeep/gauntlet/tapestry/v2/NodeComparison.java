package com.nineworldsdeep.gauntlet.tapestry.v2;

/**
 * Created by brent on 8/8/16.
 */
public class NodeComparison {

    private NodeComparisonResult mComparisonResult;
    private String mMultilineDetails;

    public NodeComparison(NodeComparisonResult comparisonResult, String multilineDetails){

        this.mComparisonResult = comparisonResult;
        this.mMultilineDetails = multilineDetails;
    }

    public NodeComparisonResult getComparisonResult() {

        return mComparisonResult;
    }

    public String getMultilineDetails() {

        return mMultilineDetails;
    }
}
