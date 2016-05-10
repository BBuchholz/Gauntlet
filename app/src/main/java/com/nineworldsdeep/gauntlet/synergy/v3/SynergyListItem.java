package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by brent on 1/21/16.
 */
public class SynergyListItem {

    private String itemText;
    private Parser p;
    private String displayKey;

    public SynergyListItem(String itemText) {

        p = new Parser();
        this.itemText = itemText;
        this.displayKey = "item";

        if(!v3ItemText() && p.validate(itemText)){
            convertItemTextToV3();
        }
    }

    public SynergyListItem(String categoryName, SynergyListItem sli){

        this(sli.itemText);

        String categorizedText = "::" + categoryName + ":: - " + getItem();

        this.itemText = p.setFirst("item", categorizedText, this.itemText);
    }

    public String getDisplayKey() {
        return displayKey;
    }

    public void setDisplayKey(String displayKey) {
        this.displayKey = displayKey;
    }

    public boolean isCompleted() {

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

    /**
     * gets the value for the keyVal "item"
     * @return
     */
    public String getItem() {

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

    public void markArchived() {

        //not implemented for less than v3
        if(!p.validate(itemText)){

            throw new IllegalArgumentException("archival failed, fragment[" +
                    itemText + "] is not valid");
        }

        //avoid overwriting anything with a timestamp already attached
        if(Utils.stringIsNullOrWhitespace(p.extract("archivedAt", itemText))){

            append("archivedAt",
                    SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss());
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

    /**
     * returns raw lineItem
     * (eg. "item={some text} tags={testing, examples} registeredAt={20160327190400}")
     * @return
     */
    public String toLineItem(){
        return itemText;
    }

    public boolean isCategorizedItem() {

        return getItem().startsWith("::") &&
                getItem().contains(":: - ");
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

    /**
     * trims category from internal representation of item if exists.
     * @return the category trimmed from the item, or null if item was not
     * categorized to begin with
     */
    public String trimCategory() {

        String category = null;

        if(isCategorizedItem()){

            category = SynergyUtils.parseCategory(getItem());

            int startIdx = getItem().indexOf(":: - ") + 5;

            String trimmedValue = getItem().substring(startIdx);

            this.itemText = p.setFirst("item", trimmedValue, itemText);

        }

        return category;
    }

    public String getCategory() {

        if(isCategorizedItem()){

            int endIdx = getItem().indexOf(":: - ");

            return getItem().substring(2, endIdx);

        }else{

            return null;
        }
    }

    public void markIncomplete() {

        if(v2Completed()){

            int beginIndex = itemText.indexOf("{") + 1;
            int endIndex = itemText.lastIndexOf("}");
            itemText = itemText.substring(beginIndex, endIndex);
        }

        if(v3Completed()){

            itemText = p.trimLastKeyVal("completedAt", itemText);
        }
    }

    @Override
    public String toString(){

        String displayText = p.extract(getDisplayKey(), itemText);

        if(Utils.stringIsNullOrWhitespace(displayText)){

            displayText = "[[INVALID DISPLAY KEY]::[" +
                    itemText + "]]";

        }else{

            if(isCompleted()){

                String completedPrefix = "(completed) ";

                if(v3Completed()){

                    completedPrefix = "completedAt={" +
                            p.extract("completedAt", itemText)
                            + "} ";
                }

                displayText =  completedPrefix + displayText;
            }
        }

        return displayText;
    }

}
