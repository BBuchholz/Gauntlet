package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.text.ParseException;
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
    //private static ArrayList<ArchivistSourceExcerpt> openSourceExcerpts;
    private static ArchivistSourceType currentSourceType;
    private static ArchivistSource currentSource;

    private static HashMap<String, String> mainActivityFragmentKeysToTitles;
    private static HashMap<String, String> sourceDetailActivityFragmentKeysToTitles;

    static {
        namesToSourceTypes = new HashMap<>();
        idsToSourceTypes = new SparseArray<>();
        //openSourceExcerpts = new ArrayList<>();
        mainActivityFragmentKeysToTitles = new HashMap<>();
        sourceDetailActivityFragmentKeysToTitles = new HashMap<>();
        currentSourceType = null;
        currentSource = null;
    }

    private static void addSourceType(ArchivistSourceType sourceType){

        namesToSourceTypes.put(sourceType.getSourceTypeName(), sourceType);
        idsToSourceTypes.put(sourceType.getSourceTypeId(), sourceType);
    }

//    private static void genTestExcerpts(
//            ArrayList<ArchivistSourceExcerpt> openSourceExcerpts,
//            int numberToGenerate){
//
//        //mock excerpts
////        openSourceExcerpts.add(new ArchivistSourceExcerpt("Pages 3-10", "a passage of text from the book"));
////        openSourceExcerpts.add(new ArchivistSourceExcerpt("Quote", "something someone cool said once"));
////        openSourceExcerpts.add(new ArchivistSourceExcerpt("0:15:05 - 0:17:23", "a transcript of the audio"));
////        openSourceExcerpts.add(new ArchivistSourceExcerpt("Web Page Section C", "some stuff from the page"));
//
//        Lorem lorem = LoremIpsum.getInstance();
//
//        Random rand = new Random();
//
//        for(int i = 1; i <= numberToGenerate; i++) {
//            String loremValue = lorem.getWords(1, i * 5);
//            addTestExcerpt(openSourceExcerpts, i, loremValue, lorem, rand);
//        }
//    }

//    private static void addTestExcerpt(ArrayList<ArchivistSourceExcerpt> openSourceExcerpts, int id, String loremExcerptValue, Lorem lorem, Random rand) {
//
//        String name = "test excerpt #" + Integer.toString(id);
//
//        ArchivistSourceExcerpt ase =
//                new ArchivistSourceExcerpt(id,1,loremExcerptValue,name + " pages",name + " begin time",name + " end time");
//
//        //max will be between 1 and 8
//        int maxNumTags = rand.nextInt(8) + 1;
//
//        for(int i = 1; i < maxNumTags; i++) {
//
//            ase.tag(lorem.getWords(1, 3));
//        }
//
//        openSourceExcerpts.add(ase);
//    }


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

    static ArrayList<ArchivistSourceLocationEntry> getSourceLocationEntriesForCurrentSource(Context context) {

        //use current source to get entries
        int sourceId = -1;

        if(getCurrentSource() != null){
            sourceId = getCurrentSource().getSourceId();
        }

        NwdDb db = NwdDb.getInstance(context);

        ArrayList<ArchivistSourceLocationEntry> entries =
                null;
        try {
            entries = db.getArchivistSourceLocationSubsetEntriesForSourceId(sourceId);
        } catch (Exception ex) {
            Utils.toast(context, "Error retrieving entries: " + ex.getMessage());
        }

        boolean useMockup = entries != null && entries.size() < 1;

        if(useMockup){

            for (int i = 1; i < 6; i++) {

                String numString = " " + Integer.toString(i);
                entries.add(new ArchivistSourceLocationEntry(
                        -1,
                        -1,
                        -1,
                        "demo location" + numString,
                        "demo subset" + numString,
                        "demo entry name" + numString));
            }
        }

        return entries;
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

    static ArrayList<ArchivistSourceExcerpt> getOpenSourceExcerpts(
            Context context)
                throws Exception {

        ArrayList<ArchivistSourceExcerpt> openSourceExcerpts = new ArrayList<>();

        if(currentSource != null && currentSource.getSourceId() > 0) {

            NwdDb db = NwdDb.getInstance(context);

            openSourceExcerpts =
                    db.getArchivistSourceExcerptsForSourceId(
                            context,
                            currentSource.getSourceId());

            db.populateArchivistSourceExcerptTaggings(openSourceExcerpts);
        }

        return openSourceExcerpts;
    }

    static void setCurrentSourceTypeByName(String typeName) {

        mainActivityFragmentKeysToTitles.put("Sources", typeName);
        currentSourceType = getSourceTypeByName(typeName);
    }

    static void setMainActivityFragmentTitle(String fragmentKey, String fragmentTitle){
        mainActivityFragmentKeysToTitles.put(fragmentKey, fragmentTitle);
    }

    static void setSourceDetailActivityFragmentTitle(String fragmentKey, String fragmentTitle){
        sourceDetailActivityFragmentKeysToTitles.put(fragmentKey, fragmentTitle);
    }

    static ArchivistSourceType getCurrentSourceType(){
        return currentSourceType;
    }

    static ArchivistSource getCurrentSource(){
        return currentSource;
    }

    static void setCurrentSource(ArchivistSource archivistSource){
        currentSource = archivistSource;
    }

    static String getSourceDetailActivityFragmentTitle(String fragmentKey) {

        return sourceDetailActivityFragmentKeysToTitles.get(fragmentKey);
    }

    static String getMainActivityFragmentTitle(String fragmentKey) {

        return mainActivityFragmentKeysToTitles.get(fragmentKey);
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

    public static ArrayList<ArchivistSourceLocation> getAllLocations(Context context) {

        ArrayList<ArchivistSourceLocation> allLocations;

        NwdDb db = NwdDb.getInstance(context);

        return db.getAllArchivistSourceLocations();
    }

    public static ArrayList<ArchivistSourceLocationSubset> getLocationSubsets(
            Context context, int sourceLocationId) {

        ArrayList<ArchivistSourceLocationSubset> subsetsForLocation;

        NwdDb db = NwdDb.getInstance(context);

        return db.getArchivistSourceLocationSubsetsForLocationId(sourceLocationId);
    }
}
