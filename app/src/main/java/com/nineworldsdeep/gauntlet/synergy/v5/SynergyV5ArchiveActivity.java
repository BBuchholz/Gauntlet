package com.nineworldsdeep.gauntlet.synergy.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;

public class SynergyV5ArchiveActivity extends AppCompatActivity {

    private SynergyV5Archive sa;

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
                        SynergyV5ArchivesActivity.EXTRA_ARCHIVENAME);

        readItems(archiveName);
    }

    private void readItems(String archiveName) {

        sa = new SynergyV5Archive(archiveName);
        sa.loadItems();

        setListViewAdapter();
    }

    private void setListViewAdapter() {

        ListView lvItems =
                (ListView)findViewById(R.id.lvItems);

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                sa.getItems()));
    }

}
