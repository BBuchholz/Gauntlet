package com.nineworldsdeep.gauntlet.archivist.v5;

import java.util.ArrayList;

public class ArchivistWorkspace {

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
    private static ArrayList<ArchivistSourceType> sourceTypes;
    private static ArrayList<ArchivistSource> openSources;
    private static ArrayList<ArchivistSourceExcerpt> openSourceExcerpts;

    static {
        sourceTypes = new ArrayList<>();
        openSources = new ArrayList<>();
        openSourceExcerpts = new ArrayList<>();

        loadTestingValues();
    }

    public static void loadTestingValues(){

        sourceTypes.add(new ArchivistSourceType("Book"));
        sourceTypes.add(new ArchivistSourceType("Video"));
        sourceTypes.add(new ArchivistSourceType("Magazine"));
        sourceTypes.add(new ArchivistSourceType("Webpage"));
    }

    public static ArrayList<ArchivistSourceType> getSourceTypes() {
        return sourceTypes;
    }
}
