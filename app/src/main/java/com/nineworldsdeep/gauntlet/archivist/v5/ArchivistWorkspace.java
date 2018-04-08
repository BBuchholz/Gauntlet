package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseArray;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.sqlite.NwdContract;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class ArchivistWorkspace {

/*
 * Mirrors MnemosyneRegistry, being a static entity,
 * holding/managing source types, open sources,
 * and open source excerpts.
 *
 * Models the idea of a workspace, one needn't load all sources,
 * one would retrieve the sources (say a book) that one is working with
 * one may retrieve multiple sources like one might have multiple books
 * open on a desk, and one might be focusing on multiple excerpts withing
 * those sources at a time, without needing to load all excerpt from
 * every source. that is the concept we are modeling here
 */
    private static HashMap<String, ArchivistSourceType> namesToSourceTypes;
    //private static HashMap<Integer, ArchivistSourceType> idsToSourceTypes;
    private static SparseArray<ArchivistSourceType> idsToSourceTypes;
    private static ArrayList<ArchivistSourceExcerpt> openSourceExcerpts;
    private static ArchivistSourceType currentSourceType;

    private static HashMap<String, String> fragmentKeysToTitles;

    static {
        namesToSourceTypes = new HashMap<>();
        idsToSourceTypes = new SparseArray<>();
        openSourceExcerpts = new ArrayList<>();
        fragmentKeysToTitles = new HashMap<>();
        currentSourceType = null;

        loadTestingValues();
    }

    private static void addSourceType(ArchivistSourceType sourceType){

        namesToSourceTypes.put(sourceType.getSourceTypeName(), sourceType);
        idsToSourceTypes.put(sourceType.getSourceTypeId(), sourceType);
    }

    private static void loadTestingValues(){

        //mock excerpts
//        openSourceExcerpts.add(new ArchivistSourceExcerpt("Pages 3-10", "a passage of text from the book"));
//        openSourceExcerpts.add(new ArchivistSourceExcerpt("Quote", "something someone cool said once"));
//        openSourceExcerpts.add(new ArchivistSourceExcerpt("0:15:05 - 0:17:23", "a transcript of the audio"));
//        openSourceExcerpts.add(new ArchivistSourceExcerpt("Web Page Section C", "some stuff from the page"));

        openSourceExcerpts.add(new ArchivistSourceExcerpt(1,1,"asdf","asdf","asdf","asdf"));
    }

    static ArrayList<ArchivistSourceType> getSourceTypes(Context context) {
        refreshSourceTypes(context);
        ArrayList<ArchivistSourceType> lst = new ArrayList<>(namesToSourceTypes.values());
        Collections.sort(lst);
        return lst;
    }

    private static void refreshSourceTypes(Context context) {

        NwdDb db = NwdDb.getInstance(context);

        ArrayList<ArchivistSourceType> allTypes = db.getArchivistSourceTypes(context);

        for(ArchivistSourceType sourceType : allTypes){

            addSourceType(sourceType);
        }
    }

    static ArrayList<ArchivistSource> getSources(Context context) {

        ArrayList<ArchivistSource> openSources;

        NwdDb db = NwdDb.getInstance(context);

        if(currentSourceTypeIsAllTypes()){
            //return all sources
            openSources = db.getAllArchivistSources(context);
        }else{
            //return for current source type (where clause using source type id)
            openSources = db.getArchivistSourcesForTypeId(context,
                    currentSourceType.getSourceTypeId());
        }

        return openSources;
    }



    static ArrayList<ArchivistSourceExcerpt> getOpenSourceExcerpts() {
        return openSourceExcerpts;
    }

    static void setCurrentSourceTypeByName(String typeName) {

        fragmentKeysToTitles.put("Sources", typeName);
        currentSourceType = getSourceTypeByName(typeName);
    }

    static void setFragmentTitle(String fragmentKey, String fragmentTitle){
        fragmentKeysToTitles.put(fragmentKey, fragmentTitle);
    }

    static ArchivistSourceType getCurrentSourceType(){
        return currentSourceType;
    }

    static String getFragmentTitle(String fragmentKey) {

        return fragmentKeysToTitles.get(fragmentKey);
    }

    private static ArchivistSourceType getSourceTypeByName(String name) {

        return namesToSourceTypes.get(name);
    }

    static boolean currentSourceTypeIsAllTypes() {

        return currentSourceType == null ||
                currentSourceType.getSourceTypeName().equals(
                        ArchivistSourceType.ALL_SOURCE_TYPES_NAME);

    }

    public static ArchivistSourceType getSourceTypeById(int sourceTypeId) {
        return idsToSourceTypes.get(sourceTypeId);
    }
}
