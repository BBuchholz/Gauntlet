package com.nineworldsdeep.gauntlet.synergy.v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;

public class SynergyArchiveActivity extends AppCompatActivity {

    private SynergyArchiveFile saf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_archive_v2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        String archiveName =
                intent.getStringExtra(
                        SynergyArchivesActivity.EXTRA_ARCHIVENAME);

        readItems(archiveName);
    }

    private void readItems(String archiveName) {

        saf = new SynergyArchiveFile(archiveName);
        saf.loadItems();

        setListViewAdapter();
    }

    private void setListViewAdapter() {

        ListView lvItems =
                (ListView)findViewById(R.id.lvItems);

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                saf.getItems()));
    }

}
