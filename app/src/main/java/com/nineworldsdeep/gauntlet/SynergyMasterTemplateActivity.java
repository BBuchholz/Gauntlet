package com.nineworldsdeep.gauntlet;

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

import java.util.List;

public class SynergyMasterTemplateActivity extends AppCompatActivity {

    public static final String EXTRA_TEMPLATENAME =
            "com.nineworldsdeep.gauntlet.SYNERGY_TEMPLATENAME";
    private List<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    private void readItems(){

        items = SynergyTemplateFile.getAllTemplateNames();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_master_template);
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
                        SynergyTemplateActivity.class);
                intent.putExtra(EXTRA_TEMPLATENAME, selectedArchive);
                startActivity(intent);

            }
        });

    }

}
