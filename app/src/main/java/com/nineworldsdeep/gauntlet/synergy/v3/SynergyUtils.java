package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;
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

        for(SynergyListItem itm : slf.getItems()){
            if(listIsExpired || SynergyUtils.listItemIsCompleted(itm)){
                //this prevents an archive bug, github issue #48
                //will simply not remove anything that is incomplete & categorized
                //ignores completed items, even if categorized, but
                //since v3 is underway, this is a negligible for now
                //a bit hackish but can be fixed once v3 is fully implemented
                //for now, user should use "shelve all categorized items" from menu
                if(!itm.isCategorizedItem()){
                    toBeRemoved.add(itm);
                }
            }
        }

        for(SynergyListItem itm : toBeRemoved){
            slf.remove(itm);
            saf.add(itm);
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
