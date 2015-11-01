package com.nineworldsdeep.gauntlet.synergy;

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

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.synergy.v1.SynergyArchiveFile;

import java.util.List;

//TODO: refactor into package v2
public class SynergyMasterArchiveActivity extends AppCompatActivity {

    public static final String EXTRA_ARCHIVENAME =
            "com.nineworldsdeep.gauntlet.SYNERGY_ARCHIVENAME";
    private List<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    private void readItems(){

        items = SynergyArchiveFile.getAllArchiveNames();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_master_archive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvItems = (ListView)findViewById(R.id.lvItems);
        readItems();

        setupListViewListener();
    }

    private void setupListViewListener(){

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int idx,
                                    long id) {

                //get selected archive name
                String selectedArchive = items.get(idx);

                Intent intent = new Intent(view.getContext(),
                        SynergyArchiveActivity.class);
                intent.putExtra(EXTRA_ARCHIVENAME, selectedArchive);
                startActivity(intent);

            }
        });

    }
}
