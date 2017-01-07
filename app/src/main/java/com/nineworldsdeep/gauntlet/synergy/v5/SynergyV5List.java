package com.nineworldsdeep.gauntlet.synergy.v5;

import android.content.Context;

import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

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
    //private HashMap<Integer, SynergyV5ListItem> mPositionToActiveItem;

    public SynergyV5List(String listName) {

        mListName = listName;
        mListId = -1;
        mItems = new ArrayList<>();

        //mPositionToActiveItem = new HashMap<>();
    }

    public void sync(Context context, NwdDb db) {

        //check for timestamped list
        //if isTimeStamped
        //  first, shelve list, and sync (with timestamped name)
        //
        //  clear list id, change name, and activate list
        //  sync again

        if(SynergyV5Utils.isTimeStampedList(this)){

            //load if not
            db.sync(context, this);

            shelve();

            db.sync(context, this);

            mListId = -1;
            mListName = SynergyV5Utils.stripTimeStamp(mListName);

            for(SynergyV5ListItem sli : getAllItems()){

                sli.clearIds();
            }

            activate();
        }

        db.sync(context, this);
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

    //this stays private, doesn't account for item status
    private void move(int currentPosition, int moveToPosition) {

        mItems.add(moveToPosition, mItems.remove(currentPosition));
    }

    public void moveToTop(String itemValue){

        int idx = getItemPositionByItemValue(itemValue);

        if(idx > -1){

            move(idx, 0);
        }
    }

    public void moveToBottom(String itemValue){

        int idx = getItemPositionByItemValue(itemValue);

        if(idx > -1){

            move(idx, mItems.size() - 1);
        }
    }

    public void moveDown(String itemValue){

        int idx = getItemPositionByItemValue(itemValue);

        int nextIdx = mItems.size() - 1;

        if(idx > -1){

            SynergyV5ListItem found = mItems.get(idx);

            for(int i = mItems.size() - 1; i > -1; i--){

                SynergyV5ListItem itm = mItems.get(i);

                if(itm.getItemValue().equalsIgnoreCase(found.getItemValue())){

                    //reached item to move down
                    break;
                }

                if(i >= idx &&
                   itm.isCompleted() == found.isCompleted() &&
                   itm.isActive() == found.isActive()){

                    nextIdx = i;
                }
            }
        }

        move(idx, nextIdx);
    }

    public void moveUp(String itemValue){

        int idx = getItemPositionByItemValue(itemValue);

        int prevIdx = 0;

        if(idx > -1){

            SynergyV5ListItem found = mItems.get(idx);

            for(int i = 0; i < mItems.size(); i++){

                SynergyV5ListItem itm = mItems.get(i);

                if(itm.getItemValue().equalsIgnoreCase(found.getItemValue())){

                    //reached item to move up
                    break;
                }

                if(i <= idx &&
                   itm.isCompleted() == found.isCompleted() &&
                   itm.isActive() == found.isActive()){

                    prevIdx = i;
                }
            }
        }

        move(idx, prevIdx);
    }

    public boolean hasCategorizedItems() {
        return false;
    }

    public int getFirstCategorizedItemPosition() {

        return 0;
    }

    public void queueToActive(int position) {

    }

    /**
     * shelves list
     */
    public void shelve() {

        mShelvedAt = TimeStamp.now();
    }

    /**
     * activates list
     */
    public void activate() {

        mActivatedAt = TimeStamp.now();
    }



    public List<SynergyV5ListItem> getAllItems() {

        return mItems;
    }

    private ArrayList<SynergyV5ListItem> getItems(boolean activeNotArchived){

       ArrayList<SynergyV5ListItem> filteredItems =
            new ArrayList<>();

        int currentIndex = -1;

        for(SynergyV5ListItem sli : getAllItems()){

            if(sli.isActive() == activeNotArchived){

                //if completed, put to bottom
                if(sli.isCompleted()){

                    filteredItems.add(filteredItems.size(), sli);

                }else {

                    currentIndex++;
                    filteredItems.add(currentIndex, sli);

                }
            }
        }

        return filteredItems;
    }

    public ArrayList<SynergyV5ListItem> getArchivedItems() {

        return getItems(false);
    }

    public ArrayList<SynergyV5ListItem> getActiveItems() {

        return getItems(true);

//        ArrayList<SynergyV5ListItem> filteredItems =
//                new ArrayList<>();
//
//        int currentIndex = -1;
//
//        for(SynergyV5ListItem sli : getAllItems()){
//
//            if(sli.isActive()){
//
//                //if completed, put to bottom
//                if(sli.isCompleted()){
//
//                    filteredItems.add(filteredItems.size(), sli);
//
//                }else {
//
//                    currentIndex++;
//                    filteredItems.add(currentIndex, sli);
//
//                }
//            }
//        }
//
//        return filteredItems;
    }

    public SynergyV5ListItem getCopyForItemValue(String itemValue){

        SynergyV5ListItem item = getByItemValue(itemValue);

        if(item != null){

            item = item.getCopy();
        }

        return item;
    }

    public SynergyV5ListItem getByItemValue(String itemValue){

//        for(SynergyV5ListItem existingListItem : mItems){
//
//            String existingValue = existingListItem.getItemValue();
//
//            if(existingValue.equalsIgnoreCase(itemValue)){
//
//                return existingListItem;
//            }
//        }

        int idx = getItemPositionByItemValue(itemValue);

        if(idx > -1){

            return mItems.get(idx);
        }

        return null;
    }

    private int getItemPositionByItemValue(String itemValue){

        for(int i = 0; i < mItems.size(); i++){

            String existingValue = mItems.get(i).getItemValue();

            if(existingValue.equalsIgnoreCase(itemValue)){

                return i;
            }
        }

        return -1;
    }

//    public SynergyV5ListItem getCopyForActivePosition(int position){
//
//        return mPositionToActiveItem.get(position).getCopy();
//    }

    public void add(int position, SynergyV5ListItem sli) {

        SynergyV5ListItem existingItem = getByItemValue(sli.getItemValue());

        if(existingItem != null){

            existingItem.merge(sli);

        }else{

            mItems.add(position, sli);
        }

//        for(SynergyV5ListItem existingListItem : mItems){
//
//            String existingValue = existingListItem.getItemValue();
//
//            if(existingValue.equalsIgnoreCase(sli.getItemValue())){
//
//                exists = true;
//                existingListItem.merge(sli);
//            }
//        }
//
//        if(!exists){
//
//            mItems.add(position, sli);
//
//        }
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
