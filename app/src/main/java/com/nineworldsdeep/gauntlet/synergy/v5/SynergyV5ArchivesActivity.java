package com.nineworldsdeep.gauntlet.synergy.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;

public class SynergyV5ArchivesActivity extends AppCompatActivity {

    public static final String EXTRA_ARCHIVENAME =
            "com.nineworldsdeep.gauntlet.SYNERGY_ARCHIVENAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_archives);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        readItems();
        setupListViewListener();
    }

    private void setupListViewListener() {

        final ListView lvItems =
                (ListView)findViewById(R.id.lvItems);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int idx,
                                    long id) {

                //get selected archive name
                String selectedArchive =
                        (String)lvItems.getItemAtPosition(idx);

                Intent intent = new Intent(view.getContext(),
                        SynergyV5ArchiveActivity.class);
                intent.putExtra(EXTRA_ARCHIVENAME, selectedArchive);
                startActivity(intent);

            }
        });
    }

    private void readItems() {

        ListView lvItems =
                (ListView)findViewById(R.id.lvItems);

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                SynergyV5Utils.getAllArchiveNames()));
    }

}
