package com.nineworldsdeep.gauntlet.synergy.v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SynergyTemplateActivity extends AppCompatActivity {

    private SynergyTemplateFile stf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_template_v2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        String templateName =
                intent.getStringExtra(
                        SynergyTemplatesActivity.EXTRA_TEMPLATENAME);

        readItems(templateName);
    }

    private void readItems(String templateName) {

        stf = new SynergyTemplateFile(templateName);
        stf.loadItems();

        setListViewAdapter();
    }

    private void setListViewAdapter() {

        ListView lvItems = (ListView)findViewById(R.id.lvItems);

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                stf.getItems()));
    }

    public void onAddItemClick(View view) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        stf.add(stf.size(), itemText);
        etNewItem.setText("");
        writeItems();
        refreshListItems();
    }

    private void refreshListItems(){

        setListViewAdapter();
    }

    private void writeItems() {

        if(stf != null){
            stf.save();
        }
    }
}
