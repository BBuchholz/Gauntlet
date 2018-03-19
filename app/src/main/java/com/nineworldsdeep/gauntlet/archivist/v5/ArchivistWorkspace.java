package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Context;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
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
    private static ArrayList<ArchivistSource> openSources;
    private static ArrayList<ArchivistSourceExcerpt> openSourceExcerpts;
    private static ArchivistSourceType currentSourceType;

    private static HashMap<String, String> fragmentKeysToTitles;

    static {
        namesToSourceTypes = new HashMap<>();
        openSources = new ArrayList<>();
        openSourceExcerpts = new ArrayList<>();
        fragmentKeysToTitles = new HashMap<>();
        currentSourceType = null;

        loadTestingValues();
    }

    private static void addSourceType(ArchivistSourceType sourceType){

        namesToSourceTypes.put(sourceType.getSourceTypeName(), sourceType);
    }

    private static void loadTestingValues(){

        //mock sources
        openSources.add(new ArchivistSource("Test Book One", "a book"));
        openSources.add(new ArchivistSource("Test Book Two", "another book"));
        openSources.add(new ArchivistSource("Test Movie Segment", "a citation of a movie segment"));
        openSources.add(new ArchivistSource("Test Web Page", "some source site"));

        //mock excerpts
        openSourceExcerpts.add(new ArchivistSourceExcerpt("Pages 3-10", "a passage of text from the book"));
        openSourceExcerpts.add(new ArchivistSourceExcerpt("Quote", "something someone cool said once"));
        openSourceExcerpts.add(new ArchivistSourceExcerpt("0:15:05 - 0:17:23", "a transcript of the audio"));
        openSourceExcerpts.add(new ArchivistSourceExcerpt("Web Page Section C", "some stuff from the page"));
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

    static ArrayList<ArchivistSource> getOpenSources() {
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

    static ArchivistSourceType getSourceTypeByName(String name) {

        return namesToSourceTypes.get(name);
    }

    public static boolean currentSourceTypeIsAllTypes() {

        return currentSourceType.getSourceTypeName().equals(ArchivistSourceType.ALL_SOURCE_TYPES_NAME);
    }
}
