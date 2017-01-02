package com.nineworldsdeep.gauntlet.synergy.v5;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.synergy.v2.ListEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 11/4/16.
 */
public class SynergyV5Utils {

    public static void pushAll(List<String> listNames) {


    }

    public static void rename(String previousName, String newName) {

    }

    public static void copy(String copyFromListName, String copyToListName) {

    }

    public static List<ListEntry> getAllListEntries(Context c, NwdDb db){

        List<ListEntry> lst = new ArrayList<>();

        for(String listName : getAllListNames(c, db)){

            ListEntry le = new ListEntry();
            le.setListName(listName);
            //we will change the query to count list items as well
            //then we can get rid of getAllListNames() in favor of this
            //one
            le.setItemCount(0);
            lst.add(le);
        }

        return lst;
    }

    private static List<String> getAllListNames(Context c, NwdDb db) {

        return db.synergyV5GetActiveListNames(c);
    }

    public static List<String> getAllArchiveNames() {

        return new ArrayList<>();
    }

    public static SynergyV5List generateFromTemplate(String templateName, String timestampedListName) {
        return null;
    }

    public static String getTimeStampedListName(String templateName) {
        return null;
    }


    public static List<String> getAllTemplateNames(Context c) {
        Utils.toast(c, "templates not yet implemented");
        return new ArrayList<String>();
    }

    public static void queueFromTemplate(SynergyV5Template stf, int position) {

    }

    public static boolean listItemIsCompleted(SynergyV5ListItem item) {

        return false;
    }

    public static boolean listExists(String listName) {
        return false;
    }

    public static boolean isActiveQueue(String listName) {
        return false;
    }

    public static void updateTemplate(String trimmedName, SynergyV5List mSlf) {

    }

    public static void archive(String listName, boolean expired) {

    }

    public static String push(String listName) {
        return null;
    }

    public static void move(SynergyV5List sourceList,
                            SynergyV5ListItem item,
                            String processedTargetListName,
                            NwdDb db,
                            Context context) {

        SynergyV5List lst = new SynergyV5List(processedTargetListName);

        SynergyV5ListItem toBeArchived =
                sourceList.getItemByItemValue(item.getItemValue());

        item.activate();
        toBeArchived.archive();

        lst.add(item);
        lst.sync(context, db);
        sourceList.sync(context, db);
    }

    public static void copyToClipboard(Context ctx,
                                       String stringLabel,
                                       String stringToCopy) {

        ClipboardManager clipboard =
                (ClipboardManager) ctx.getSystemService(ctx.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(stringLabel, stringToCopy);
        clipboard.setPrimaryClip(clip);
    }
}
