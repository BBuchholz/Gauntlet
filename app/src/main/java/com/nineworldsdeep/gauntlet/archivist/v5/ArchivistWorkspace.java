package com.nineworldsdeep.gauntlet.archivist.v5;

import com.nineworldsdeep.gauntlet.R;

import java.util.ArrayList;
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
    private static ArrayList<ArchivistSourceType> sourceTypes;
    private static ArrayList<ArchivistSource> openSources;
    private static ArrayList<ArchivistSourceExcerpt> openSourceExcerpts;

    private static HashMap<String, String> fragmentKeysToTitles;

    static {
        sourceTypes = new ArrayList<>();
        openSources = new ArrayList<>();
        openSourceExcerpts = new ArrayList<>();
        fragmentKeysToTitles = new HashMap<>();

        loadTestingValues();
    }

    private static void loadTestingValues(){

        //mock source types
        sourceTypes.add(new ArchivistSourceType("Article", R.drawable.article));
        sourceTypes.add(new ArchivistSourceType("Book", R.drawable.book));
        sourceTypes.add(new ArchivistSourceType("Misc Source", R.drawable.misc_source));
        sourceTypes.add(new ArchivistSourceType("Movie", R.drawable.movie));
        sourceTypes.add(new ArchivistSourceType("Quote", R.drawable.quote));
        sourceTypes.add(new ArchivistSourceType("Video", R.drawable.video));
        sourceTypes.add(new ArchivistSourceType("Web", R.drawable.web));

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

    static ArrayList<ArchivistSourceType> getSourceTypes() {
        return sourceTypes;
    }

    static ArrayList<ArchivistSource> getOpenSources() {
        return openSources;
    }

    static ArrayList<ArchivistSourceExcerpt> getOpenSourceExcerpts() {
        return openSourceExcerpts;
    }

    static void setFragmentTitle(String fragmentKey, String fragmentTitle) {

        fragmentKeysToTitles.put(fragmentKey, fragmentTitle);
    }

    static String getFragmentTitle(String fragmentKey) {

        return fragmentKeysToTitles.get(fragmentKey);
    }
}
