package com.nineworldsdeep.gauntlet.synergy.v2;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v1.ListItem;
import com.nineworldsdeep.gauntlet.synergy.v1.SynergyArchiveFile;
import com.nineworldsdeep.gauntlet.synergy.v1.SynergyTemplateFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by brent on 10/30/15.
 */
public class SynergyUtils {

    public static List<String> getAllListNames(){

        String[] exts = {"txt"};
        List<String> lst = new ArrayList<String>();
        Configuration cfg = new Configuration();

        for (File f : FileUtils.listFiles(cfg.getSynergyDirectory(), exts, false)){
            lst.add(FilenameUtils.removeExtension(f.getName()));
        }

        Collections.sort(lst);

        return lst;
    }

    public static void generateFromTemplate(String templateName, String timeStampedListName){

        //TODO:implement in package v2
        //TODO: change return type to LineItemListFile (or a subclass?)
//
//        SynergyListFile slf = new SynergyListFile(timeStampedListName);
//        slf.loadItems();
//
//        SynergyTemplateFile stf = new SynergyTemplateFile(templateName);
//        stf.loadItems();
//
//        for(String itm : stf.items){
//            slf.items.add(itm);
//        }
//
//        slf.save();
    }

    public static String push(String listName){

        //TODO:implement in package v2
//        if(Utils.containsTimeStamp(listName)){
//
//            String pushToName =
//                    Utils.incrementTimeStampInString_yyyyMMdd(listName);
//
//            SynergyListFile currentFile =
//                    new SynergyListFile(listName);
//            currentFile.loadItems();
//
//            SynergyListFile pushToFile =
//                    new SynergyListFile(pushToName);
//            pushToFile.loadItems();
//
//            for(String itm : currentFile.items){
//                pushToFile.items.add(itm);
//            }
//
//            pushToFile.save();
//            currentFile.delete();
//
//            return pushToName;
//        }
//
        return null;
    }


    //TODO:implement in package v2
    public static void archive(String listName) {
        archive(listName, false);
    }

    //TODO:implement in package v2
    public static void archive(String listName, boolean expired) {

//        SynergyListFile slf = new SynergyListFile(listName);
//        slf.loadItems();
//
//        SynergyArchiveFile saf = new SynergyArchiveFile(listName);
//        saf.loadItems();
//
//        List<String> toBeRemoved = new ArrayList<>();
//
//        for(String itm : slf.items){
//            if(expired || ListItem.isCompleted(itm)){
//                toBeRemoved.add(itm);
//            }
//        }
//
//        for(String itm : toBeRemoved){
//            slf.items.remove(itm);
//            saf.items.add(itm);
//        }
//
//        saf.save();
//
//        if(slf.items.size() > 0){
//            slf.save();
//        }else{
//            slf.save(); //this can be removed once delete is implemented
//            slf.delete();
//        }
    }
}
