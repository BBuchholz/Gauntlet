package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.Fragment;
import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

        if(!v3ItemText() && p.validate(itemText)){
            convertItemTextToV3();
        }
    }

    public SynergyListItem(String categoryName, SynergyListItem sli){

        this(sli.itemText);

        String categorizedText = "::" + categoryName + ":: - " + getText();

        this.itemText = p.setFirst("item", categorizedText, this.itemText);
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

    public String getVal(String key) {
        return p.extract(key, itemText);
    }

    public String toLineItem(){
        return itemText;
    }

    public boolean isCategorizedItem() {

        return getText().startsWith("::") &&
                getText().contains(":: - ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SynergyListItem that = (SynergyListItem) o;

        return new EqualsBuilder()
                .append(itemText, that.itemText)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(itemText)
                .toHashCode();
    }
}
