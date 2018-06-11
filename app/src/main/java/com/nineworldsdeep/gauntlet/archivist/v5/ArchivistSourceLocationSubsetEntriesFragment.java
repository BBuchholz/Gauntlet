package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nineworldsdeep.gauntlet.R;

public class ArchivistSourceLocationSubsetEntriesFragment extends Fragment {

    private ArchivistSourceDetailActivity parentSourceDetailActivity;
    private SourceLocationEntryAdapter sourceLocationEntryAdapter;

    public ArchivistSourceLocationSubsetEntriesFragment() {
        // Required empty public constructor
    }

    public void setParentSourceDetailActivity(ArchivistSourceDetailActivity parent){
        this.parentSourceDetailActivity = parent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);

        sourceLocationEntryAdapter = new SourceLocationEntryAdapter(parentSourceDetailActivity);
        recyclerView.setAdapter(sourceLocationEntryAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }


    public void refreshSourceLocationEntries(Context context) {

        sourceLocationEntryAdapter.refreshSourceLocationEntries(context);
    }
}
