package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brent on 1/31/16.
 */
public class SynergyUtils {

    public static boolean listItemIsCompleted(SynergyListItem sli) {
        return sli.isCompleted();
    }

    public static void archive(String listName, boolean listIsExpired) {
        SynergyListFile slf = new SynergyListFile(listName);
        slf.loadItems();

        SynergyArchiveFile saf = new SynergyArchiveFile(listName);
        saf.loadItems();

        List<SynergyListItem> toBeRemoved = new ArrayList<>();
        HashMap<String, List<SynergyListItem>> catArchiveToItems = new HashMap<>();

        for(SynergyListItem itm : slf.getItems()){
            if(listIsExpired || SynergyUtils.listItemIsCompleted(itm)){

                if(!itm.isCategorizedItem()){
                    toBeRemoved.add(itm);
                }else{
                    //is a categorized item, archive to a different file
                    String category = itm.getCategory();

                    if(!catArchiveToItems.containsKey(category)){
                        catArchiveToItems.put(category, new ArrayList<SynergyListItem>());
                    }

                    catArchiveToItems.get(category).add(itm);
                }
            }
        }

        for(SynergyListItem itm : toBeRemoved){
            slf.remove(itm);
            saf.add(itm);
        }

        for(String category : catArchiveToItems.keySet()){

            List<SynergyListItem> lst = catArchiveToItems.get(category);

            SynergyArchiveFile thisSaf =
                    new SynergyArchiveFile(category);

            for(SynergyListItem itm : lst){

                slf.remove(itm);
                itm.trimCategory();
                thisSaf.add(itm);
            }

            thisSaf.save();
        }

        saf.save();

        if(slf.size() > 0){
            slf.save();
        }else{
            slf.delete();
        }
    }

    public static void shelveAllCategorized(SynergyListFile slf){

        while(slf.hasCategorizedItems()){

            int pos = slf.getFirstCategorizedItemPosition();

            String category = slf.get(pos).getCategory();

            shelve(slf, pos, category);
        }
    }

    private static void shelve(SynergyListFile shelveFromFile,
                               int position,
                               String category) {

        SynergyListFile shelveToFile = new SynergyListFile(category);
        shelveToFile.loadItems();

        //remove category if exists
        SynergyListItem itemToShelve = shelveFromFile.remove(position);

        if(itemToShelve.isCategorizedItem()){

            itemToShelve.trimCategory();
        }

        //add to top of list
        shelveToFile.add(0, itemToShelve);

        shelveToFile.save();
        shelveFromFile.save();
    }

    public static String push(String listName) {

        if(Utils.containsTimeStamp(listName)){

            String pushToName =
                    Utils.incrementTimeStampInString_yyyyMMdd(listName);

            SynergyListFile currentFile =
                    new SynergyListFile(listName);
            currentFile.loadItems();

            //shelve any categorized items (related to github issue #48)
            //see comment to v2.SynergyUtils.archive()
            SynergyUtils.shelveAllCategorized(currentFile);

            //reload after shelve
            currentFile.loadItems();

            SynergyListFile pushToFile =
                    new SynergyListFile(pushToName);
            pushToFile.loadItems();

            for(SynergyListItem itm : currentFile.getItems()){
                pushToFile.add(itm);
            }

            pushToFile.save();
            archive(currentFile.getListName(), true);

            return pushToName;
        }

        return null;
    }
}
