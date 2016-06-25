package com.nineworldsdeep.gauntlet.sqlite;

import android.text.TextUtils;

import com.nineworldsdeep.gauntlet.MultiMapString;
import com.nineworldsdeep.gauntlet.mnemosyne.FileTagFragment;
import com.nineworldsdeep.gauntlet.mnemosyne.TagIndexFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brent on 11/17/15.
 */
public class TagDbIndex {

    public static String getTagStringForPath(String path, NwdDb db){

        HashMap<String,String> pathTagStrings =
                getMergedPathToTagStringMap(true, false, db);

        String tagString = "";

        if(pathTagStrings.containsKey(path)){

            tagString = pathTagStrings.get(path);
        }

        return tagString;
    }

    public static HashMap<String,String> importExportPathToTagStringMap(NwdDb db){

        return getMergedPathToTagStringMap(true, true, db);
    }

    public static HashMap<String, String> getMergedPathToTagStringMap(
            boolean importFile,
            boolean exportFile,
            NwdDb db){

        if(importFile) {

            //idempotent
            loadToDbFromFile(db);
        }

        List<Map<String, String>> records =
                db.getPathTagRecordsForCurrentDevice();

        MultiMapString pathToTags =
                new MultiMapString();

        for(Map<String, String> map : records){

            String path = map.get(NwdContract.COLUMN_PATH_VALUE);
            String tag = map.get(NwdContract.COLUMN_TAG_VALUE);

            pathToTags.put(path, tag);
        }

        HashMap<String, String> output =
                convertPathTagMultiMapToPathTagStringHashMap(pathToTags);

        if(exportFile) {

            saveToFile(output);
        }

        return output;
    }

    private static HashMap<String, String>
        convertPathTagMultiMapToPathTagStringHashMap(
            MultiMapString pathToTags) {

        HashMap<String,String> pathToTagString =
                new HashMap<>();

        for(String path : pathToTags.keySet()){

            pathToTagString.put(path,
                    TextUtils.join(", ", pathToTags.get(path)));
        }

        return pathToTagString;
    }

    public static void saveToFile(HashMap<String, String> pathToTagString) {

        TagIndexFile tif = new TagIndexFile();

        for(Map.Entry<String,String> ent : pathToTagString.entrySet()){

            tif.addTagString(ent.getKey(), ent.getValue());
        }

        tif.save();
    }

    public static void loadToDbFromFile(NwdDb db){

        TagIndexFile tif = new TagIndexFile();
        tif.loadItems();

        setTags(tif.getFileTagFragments(), db);
    }

    private static void setTags(List<FileTagFragment> fileTagFragments, NwdDb db) {

        MultiMapString pathToTags = new MultiMapString();

        for(FileTagFragment ftf : fileTagFragments){

            pathToTags.putCommaStringValues(ftf.getPath(), ftf.getTags());
        }

        db.linkTagsToFile(pathToTags);
    }

    public static void setTagStringOld(String path,
                                    String commaSeparatedTagString,
                                    NwdDb db) {

        //be sure to import all file tags to db, so we don't erase them all on output (saves below)
        HashMap<String,String> justForTesting = getMergedPathToTagStringMap(true, false, db);

        //using for breakpoint
        justForTesting.isEmpty();

        //remove all tags so that any removed tags will disappear
        db.removeTagsForFile(path);

        MultiMapString pathTags = new MultiMapString();

        pathTags.putCommaStringValues(path, commaSeparatedTagString);

        db.linkTagsToFile(pathTags);

        //ignore output, saves to export file without loading previous (done above)
        justForTesting = getMergedPathToTagStringMap(false, true, db);

        //using for breakpoint
        justForTesting.isEmpty();
    }

        public static void setTagString(String path,
                                        String commaSeparatedTagString,
                                        NwdDb db){

        //remove all tags so that any removed tags will disappear
        db.removeTagsForFile(path);

        MultiMapString pathTags = new MultiMapString();

        pathTags.putCommaStringValues(path, commaSeparatedTagString);

        db.linkTagsToFile(pathTags);
    }
}
