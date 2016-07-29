package com.nineworldsdeep.gauntlet.synergy.v2;

import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by brent on 10/30/15.
 */
public class LineItemListFile {

    private String listName;
    private File synergyFile;
    private ArrayList<String> items;

    public LineItemListFile(String name, File filesDir){

        if(Utils.stringIsNullOrWhitespace(name)){
            name = "utils-BLANK-LIST";
            Utils.log("blank listname converted to '" + name + "'");
        }

        listName = name;
        items = new ArrayList<>();
        synergyFile = new File(filesDir, listName + ".txt");
    }

    public File getSynergyFile(){

        return synergyFile;
    }

    public boolean exists(){ return getSynergyFile().exists(); }

    public int size(){
        return items.size();
    }

    public List<String> getItems(){
        return items;
    }

    public void addItem(String item){
        items.add(item);
    }

    public String remove(int idx){
        return items.remove(idx);
    }

    public void add(int idx, String item){
        items.add(idx,item);
    }

    public void add(String item) { items.add(item); }

    public String get(int idx){
        return items.get(idx);
    }

    public void removeItem(String item){
        items.remove(item);
    }

    /**
     * loads line items from the associated file. any previous entries are cleared,
     * after loading, the items member of this object will only contain entries
     * from the associated file (ie. unsaved changes will be lost when this is called)
     */
    public void loadItems(){

        File toDoFile = getSynergyFile();
        try{
            items = new ArrayList<String>(FileUtils.readLines(toDoFile));
        }catch(IOException ex){
            items = new ArrayList<>();
        }
    }

    public void loadItems(List<String> itemsList){

        if(items == null){
            items = new ArrayList<>();
        }

        for(String item : itemsList){
            items.add(item);
        }
    }

    public void delete(){
        getSynergyFile().delete();
    }

    public void save(){

        File toDoFile = getSynergyFile();
        try{
            FileUtils.writeLines(toDoFile, items);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public String getListName() {
        return listName;
    }

    public void move(int moveFromPosition, int moveToPosition) {

        items.add(moveToPosition, items.remove(moveFromPosition));
    }

    /**
     * replaces item at position with items in list. original item is removed,
     * new items inserted at same position, in list order
     * @param pos - position of item to replace
     * @param list - ordered list to replace item with
     */
    public String replace(int pos, ArrayList<String> list) {

        String removed = items.remove(pos);

        Stack<String> reversedStack = new Stack<>();

        for(String item : list){

            reversedStack.push(item);
        }

        while(!reversedStack.empty()){

            items.add(pos, reversedStack.pop());
        }

        return removed;
    }

    public boolean contains(String item) {

        for(String itm : items){

            if(itm.equalsIgnoreCase(item)){
                return true;
            }
        }
        return false;
    }

    public HashMap<String, String> toHashMap() {

        Parser p = new Parser();

        HashMap<String, String> keyVals =
                new HashMap<>();

        for(String item : getItems()){

            HashMap<String, String> itemMap =
                    p.fragmentToHashMap(item);

            for(String key : itemMap.keySet()){

                keyVals.put(key, itemMap.get(key));
            }
        }

        return keyVals;
    }
}
