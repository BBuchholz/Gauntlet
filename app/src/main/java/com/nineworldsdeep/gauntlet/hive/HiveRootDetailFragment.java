package com.nineworldsdeep.gauntlet.hive;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
//import com.nineworldsdeep.gauntlet.hive.dummy.DummyContent;
import com.nineworldsdeep.gauntlet.hive.dummy.DummyContentHiveRoots;

/**
 * A fragment representing a single HiveRoot detail screen.
 * This fragment is either contained in a {@link HiveRootListActivity}
 * in two-pane mode (on tablets) or a {@link HiveRootDetailActivity}
 * on handsets.
 */
public class HiveRootDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

//    /**
//     * The dummy content this fragment is presenting.
//     */
//    private DummyContent.DummyItem mItem;

    private HiveRoot mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HiveRootDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String i = getArguments().getString(ARG_ITEM_ID);
            mItem = DummyContentHiveRoots.getItemMap().get(i);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getHiveRootName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hiveroot_detail, container, false);

        // Show the multiline details as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.hiveroot_detail))
                    .setText(mItem.toMultilineString());
        }

        return rootView;
    }
}
