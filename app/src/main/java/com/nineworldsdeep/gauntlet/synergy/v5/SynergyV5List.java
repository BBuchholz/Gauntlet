package com.nineworldsdeep.gauntlet.synergy.v5;

import android.content.Context;

import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    private HashMap<Integer, SynergyV5ListItem> mPositionToActiveItem;

    public SynergyV5List(String listName) {

        mListName = listName;
        mListId = -1;
        mItems = new ArrayList<>();
        mPositionToActiveItem = new HashMap<>();
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

        return mItems.size();
    }

    public void move(int currentPosition, int moveToPosition) {

        mItems.add(moveToPosition, mItems.remove(currentPosition));
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

    public List<SynergyV5ListItem> getAllItems() {

        return mItems;
    }

    public ArrayList<SynergyV5ListItem> getActiveItems() {

        ArrayList<SynergyV5ListItem> filteredItems =
                new ArrayList<>();

        mPositionToActiveItem.clear();

        int currentIndex = -1;

        for(SynergyV5ListItem sli : getAllItems()){

            if(sli.isActive()){

                currentIndex++;

                filteredItems.add(currentIndex, sli);

                mPositionToActiveItem.put(currentIndex, sli);
            }
        }

        return filteredItems;
    }

    public SynergyV5ListItem getCopyForActivePosition(int position){

        return mPositionToActiveItem.get(position).getCopy();
    }

    public void add(int position, SynergyV5ListItem sli) {

        boolean exists = false;

        for(SynergyV5ListItem existingListItem : mItems){

            String existingValue = existingListItem.getItemValue();

            if(existingValue.equalsIgnoreCase(sli.getItemValue())){

                exists = true;
                existingListItem.merge(sli);
            }
        }

        if(!exists){

            mItems.add(position, sli);

        }
    }

    public void add(SynergyV5ListItem v5ListItem){

        int position = mItems.size();
        add(position, v5ListItem);
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
                //mShelvedAt at is older
                mShelvedAt = shelvedAt;
            }
        }

    }

    public SynergyV5ListItem archive(SynergyV5ListItem sliToArchive) {

        //create new item so archiving original will not affect it
        SynergyV5ListItem sli = new SynergyV5ListItem(sliToArchive.getItemValue());
        sli.setItemId(sliToArchive.getItemId());

        SynergyV5ToDo toDo = sliToArchive.getToDo();

        if(toDo != null){

            sli.setToDo(toDo.getCopy());
        }

        sliToArchive.archive();

        return sli;
    }

    public SynergyV5ListItem getItemByItemValue(String itemValue) {

        SynergyV5ListItem outputItem = null;

        for(SynergyV5ListItem item : mItems){

            if(item.getItemValue().equalsIgnoreCase(itemValue)){

                outputItem = item;
            }
        }

        return outputItem;
    }

//    public SynergyV5ListItem archive(int position) {
//
//        SynergyV5ListItem currentSli = mItems.get(position);
//
//        //create new item so archiving original will not affect it
//        SynergyV5ListItem sli = new SynergyV5ListItem(currentSli.getItemValue());
//        sli.setItemId(currentSli.getItemId());
//
//        SynergyV5ToDo toDo = currentSli.getToDo();
//
//        if(toDo != null){
//
//            sli.setToDo(toDo.getCopy());
//        }
//
//        currentSli.archive();
//
//        return sli;
//    }
}
