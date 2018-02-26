package com.nineworldsdeep.gauntlet.archivist.v5;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArchivistSourceTypesFragment extends Fragment {

    private ArchivistActivity parentArchivistActivity;

    public ArchivistSourceTypesFragment() {
        // Required empty public constructor
    }

    public void setParentArchivistActivity(ArchivistActivity parentArchivistActivity){
        this.parentArchivistActivity = parentArchivistActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        SourceTypeAdapter adapter = new SourceTypeAdapter(parentArchivistActivity);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return recyclerView;
    }


}
