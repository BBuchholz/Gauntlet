package com.nineworldsdeep.gauntlet.tapestry;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class MetaBrowserActivity extends AppCompatActivity {

    private String mCurrentNodeName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateDescriptionSpinner();

        Intent i = getIntent();
        mCurrentNodeName = i.getStringExtra(
                TapestryNodeActivity.EXTRA_CURRENT_NODE_NAME);

        if(Utils.stringIsNullOrWhitespace(mCurrentNodeName)){

            Utils.toast(this, "Node Name Not Specified");

        } else {

            setTitle(mCurrentNodeName);
        }

        refreshLayout();
    }

    private void populateDescriptionSpinner() {

        Spinner spDescription = (Spinner)this.findViewById(R.id.spDescription);

        ArrayList<String> lst = new ArrayList<>();
        lst.add("tags");
        lst.add("path");
        lst.add("hash");
        lst.add("hashedAt");
        lst.add("device");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, lst);

        spDescription.setAdapter(adapter);
    }

    private void refreshLayout() {

        ListView lvItems =
                (ListView) findViewById(R.id.lvItems);

        loadItems();
        setupSpinnerListener();
        setupListViewListener();
        registerForContextMenu(lvItems);
    }

    private void setupSpinnerListener() {

        final Spinner spDescription =
                (Spinner) findViewById(R.id.spDescription);

        spDescription.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                MetaEntry.setDescriptionKey(
                        spDescription.getSelectedItem().toString());

                refreshLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    private void setupListViewListener() {


    }

    private void loadItems() {

        ListView lvItems = (ListView) findViewById(R.id.lvItems);

        ArrayList<MetaEntry> meta =
                TapestryUtils.getMetaEntries(mCurrentNodeName);

        SimpleAdapter saMeta =
                new SimpleAdapter(
                        this.getBaseContext(),
                        meta,
                        MetaEntry.getLayout(),
                        MetaEntry.getMapKeysForView(),
                        MetaEntry.getIdsForViewElements());

        lvItems.setAdapter(saMeta);
    }

}
