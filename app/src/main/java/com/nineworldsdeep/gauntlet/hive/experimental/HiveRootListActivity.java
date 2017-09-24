package com.nineworldsdeep.gauntlet.hive.experimental;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.nineworldsdeep.gauntlet.R;

//import com.nineworldsdeep.gauntlet.hive.experimental.dummy.DummyContent;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.hive.HiveRoot;
import com.nineworldsdeep.gauntlet.hive.experimental.dummy.DummyContentHiveRoots;

import java.util.List;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * An activity representing a list of HiveRoots. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link HiveRootDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class HiveRootListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiveroot_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.hiveroot_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.hiveroot_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(
                new SimpleItemRecyclerViewAdapter(
                        DummyContentHiveRoots.getItems()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<HiveRoot> mValues;

        public SimpleItemRecyclerViewAdapter(List<HiveRoot> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.hiveroot_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mHiveRootIdView.setText(String.valueOf(mValues.get(position).getHiveRootId()));
            holder.mHiveRootNameView.setText(mValues.get(position).getHiveRootName());
            holder.mHiveRootActivatedAtView.setText(
                    TimeStamp.toYyyy_MM_dd_hh_mm_ss(mValues.get(position).getActivatedAt()));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(HiveRootDetailFragment.ARG_ITEM_ID,
                                Integer.toString(holder.mItem.getHiveRootId()));
                        HiveRootDetailFragment fragment = new HiveRootDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.hiveroot_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, HiveRootDetailActivity.class);
                        intent.putExtra(HiveRootDetailFragment.ARG_ITEM_ID,
                                Integer.toString(holder.mItem.getHiveRootId()));

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mHiveRootIdView;
            public final TextView mHiveRootNameView;
            public final TextView mHiveRootActivatedAtView;
            public HiveRoot mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mHiveRootIdView = (TextView) view.findViewById(R.id.tvHiveRootId);
                mHiveRootNameView = (TextView) view.findViewById(R.id.tvHiveRootName);
                mHiveRootActivatedAtView =
                        (TextView) view.findViewById(R.id.tvHiveRootActivatedAt);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mHiveRootNameView.getText() + "'";
            }
        }
    }
}
