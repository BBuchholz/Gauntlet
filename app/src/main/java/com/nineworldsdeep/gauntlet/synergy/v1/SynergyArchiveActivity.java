package com.nineworldsdeep.gauntlet.synergy.v1;

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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//TODO: refactor into package v2
public class SynergyArchiveActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private String archiveName;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_archive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set archiveName
        Intent intent = getIntent();

        archiveName =
                intent.getStringExtra(
                        SynergyMasterArchiveActivity.EXTRA_ARCHIVENAME);

        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        readItems();

    }

    private void readItems(){
        File archiveFile = SynergyArchiveFile.getSynergyArchiveFile(archiveName);
        try{
            items = new ArrayList<String>(FileUtils.readLines(archiveFile));
        }catch(IOException ex){
            items = new ArrayList<>();
        }

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
    }

}
