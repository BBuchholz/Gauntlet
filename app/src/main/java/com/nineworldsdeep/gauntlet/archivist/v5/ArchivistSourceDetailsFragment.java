package com.nineworldsdeep.gauntlet.archivist.v5;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nineworldsdeep.gauntlet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArchivistSourceDetailsFragment extends Fragment {


    public ArchivistSourceDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_archivist_source_details, container, false);
    }

}
