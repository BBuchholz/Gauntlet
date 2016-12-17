package com.nineworldsdeep.gauntlet.synergy.v5;

/**
 * Created by brent on 11/4/16.
 */
public class SynergyV5ListItem {

    private String mItemValue;
    private int mItemId;
    private int mListItemId;
    private SynergyV5ToDo mToDo;

    public SynergyV5ToDo getToDo() {
        return mToDo;
    }

    public void setToDo(SynergyV5ToDo toDo) {
        this.mToDo = toDo;
    }

    public SynergyV5ListItem(String itemValue) {

        mItemValue = itemValue;
        mItemId = -1;
    }

    public boolean isCompleted() {
        return false;
    }

    public void markCompleted() {

    }

    public void markIncomplete() {

    }

    public String getCategory() {
        return null;
    }

    public String getItemValue() {

        return mItemValue;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        this.mItemId = itemId;
    }

    public int getListItemId() {
        return mListItemId;
    }

    public void setListItemId(int listItemId) {
        this.mListItemId = listItemId;
    }

    @Override
    public String toString(){

        return mItemValue;
    }

    public void merge(SynergyV5ListItem sli) {

        if(sli.getItemValue().equalsIgnoreCase(mItemValue)){

            if(sli.getItemId() > mItemId){

                mItemId = sli.getItemId();
            }

            if(sli.getListItemId() > mListItemId){

                mListItemId = sli.getListItemId();
            }

            if(mToDo == null){

                mToDo = sli.getToDo();

            }else if (sli.getToDo() != null){

                SynergyV5ToDo toDo = sli.getToDo();

                mToDo.setTimeStamps(toDo.getActivatedAt(),
                                    toDo.getCompletedAt(),
                                    toDo.getArchivedAt());
            }

        }
    }
}
