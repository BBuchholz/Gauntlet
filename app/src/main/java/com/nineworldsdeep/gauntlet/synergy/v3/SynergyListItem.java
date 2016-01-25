package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.Fragment;
import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

import java.security.InvalidParameterException;

/**
 * Created by brent on 1/21/16.
 */
public class SynergyListItem {

    private String itemText;
    private Parser p;

    public SynergyListItem(String itemText) {
        p = new Parser();
        this.itemText = itemText;
    }

    public boolean listItemIsCompleted() {

        return v2Completed() || v3Completed();
    }

    private boolean v3Completed(){

        return !Utils.stringIsNullOrWhitespace(
                p.extract("completedAt", itemText));
    }

    private boolean v2Completed(){

        return itemText.startsWith("completed={")
                        && itemText.endsWith("}");
    }

    private boolean v3ItemText(){

        return !Utils.stringIsNullOrWhitespace(
                p.extract("item", itemText)
        );
    }

    public String getText() {

        if(v3ItemText()){
            return p.extract("item", itemText);
        }

        return itemText;
    }

    public void append(String key, String value){

        itemText += " " + key + "={" + value + "}";
    }

    public void markCompleted() {
        //make sure we don't mess up something already completed
        if(!v2Completed() && !v3Completed()){

            if(!v3ItemText()){
                if(p.validate(itemText)){

                    convertItemTextToV3();
                    append("completedAt",
                            SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss());

                }else{

                    //use v2 completion
                    itemText = "completed={" + itemText + "}";
                }
            }else{
                //is v3 itemText
                append("completedAt",
                        SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss());
            }
        }
    }

    private void convertItemTextToV3() {
        if(!p.validate(itemText)){
            throw new IllegalArgumentException("cannot convert invalid string '" +
                    itemText + "' to v3");
        }

        itemText = "item={" + itemText + "}";
    }
}
