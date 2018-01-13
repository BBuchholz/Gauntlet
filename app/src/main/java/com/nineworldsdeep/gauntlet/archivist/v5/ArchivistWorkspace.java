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

        //mock source types
        sourceTypes.add(new ArchivistSourceType("Book"));
        sourceTypes.add(new ArchivistSourceType("Video"));
        sourceTypes.add(new ArchivistSourceType("Magazine"));
        sourceTypes.add(new ArchivistSourceType("Webpage"));

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

    public static ArrayList<ArchivistSourceType> getSourceTypes() {
        return sourceTypes;
    }

    public static ArrayList<ArchivistSource> getOpenSources() {
        return openSources;
    }

    public static ArrayList<ArchivistSourceExcerpt> getOpenSourceExcerpts() {
        return openSourceExcerpts;
    }
}
