package com.nineworldsdeep.gauntlet.synergy.v5;

import android.content.Context;

import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by brent on 11/4/16.
 */
public class SynergyV5List {

    private String mListName;
    private int mListId;
    private Date mActivatedAt;
    private Date mShelvedAt;
    private ArrayList<SynergyV5ListItem> mItems;

    public SynergyV5List(String listName) {

        mListName = listName;
        mListId = -1;
        mItems = new ArrayList<>();
    }

    public void save(Context context, NwdDb db) {

        db.save(context, this);
    }

    public boolean exists() {
        return true;
    }

    public SynergyV5ListItem get(int position) {
        return mItems.get(position);
    }

    public String getListName() {
        return mListName;
    }

    public SynergyV5ListItem replace(int pos,
                                     ArrayList<SynergyV5ListItem> sliList) {

        return null;
    }

    public void archiveOne(SynergyV5ListItem replace, boolean b) {

    }

    public int size() {
        return 0;
    }

    public void move(int currentPosition, int moveToPosition) {

    }

    public boolean hasCategorizedItems() {
        return false;
    }

    public int getFirstCategorizedItemPosition() {

        return 0;
    }

    public void queueToActive(int position) {

    }

    public void shelve(int position, String category) {

    }

    public void load(Context context, NwdDb db) {

        db.load(context, this);
    }

    public List<SynergyV5ListItem> getItems() {

        return mItems;
    }

    public void add(int position, SynergyV5ListItem sli) {

        if(isNotDuplicate(sli)){

            mItems.add(position, sli);
        }
    }

    public void add(SynergyV5ListItem v5ListItem){

        int position = mItems.size();
        add(position, v5ListItem);
    }

    private boolean isNotDuplicate(SynergyV5ListItem sli) {

        boolean exists = false;

        for(SynergyV5ListItem existingListItem : mItems){

            String existingValue = existingListItem.getItemValue();

            if(existingValue.equalsIgnoreCase(sli.getItemValue())){

                exists = true;
            }
        }

        return !exists;
    }

    public int getListId() {
        return mListId;
    }

    public void setListId(int listId) {
        this.mListId = listId;
    }

    public Date getActivatedAt(){
        return mActivatedAt;
    }

    public Date getShelvedAt(){
        return mShelvedAt;
    }

    /**
     * will resolve conflicts, newest date will always take precedence
     * passing null values allowed as well to just set one or the other
     * null values always resolve to the non-null value (unless both null)
     * @param activatedAt
     * @param shelvedAt
     */
    public void setTimeStamps(Date activatedAt, Date shelvedAt){

        if(activatedAt != null){

            if(mActivatedAt == null || mActivatedAt.compareTo(activatedAt) < 0){
                //mActivated is older or null
                mActivatedAt = activatedAt;
            }
        }

        if(shelvedAt != null){

            if(mShelvedAt == null || mShelvedAt.compareTo(shelvedAt) < 0){
                //mShelved at is older
                mShelvedAt = shelvedAt;
            }
        }

    }
}
