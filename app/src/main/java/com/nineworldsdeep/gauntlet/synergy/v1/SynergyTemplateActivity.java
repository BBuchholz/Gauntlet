package com.nineworldsdeep.gauntlet.synergy.v1;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.ArrayList;

@Deprecated
public class SynergyTemplateActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private String templateName;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_template);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set templateName
        Intent intent = getIntent();

        templateName =
                intent.getStringExtra(
                        SynergyMasterTemplateActivity.EXTRA_TEMPLATENAME);

        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        readItems();
    }

    private void readItems(){
        //TODO: refactor to use v2
        File archiveFile = SynergyTemplateFile.getSynergyTemplateFile(templateName);
        try{
            items = new ArrayList<String>(FileUtils.readLines(archiveFile));
        }catch(IOException ex){
            items = new ArrayList<>();
        }

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
    }

    public void onAddItemClick(View view) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        addItem(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void addItem(String itemText){
        if(!itemText.trim().isEmpty()) {
            items.add(items.size(), itemText); //add to end of list
            itemsAdapter.notifyDataSetChanged();
        }else{
            Utils.toast(getApplicationContext(), "cannot add empty item");
        }
    }

    private void writeItems(){
        //TODO: refactor to use v2
        File toDoFile = SynergyTemplateFile.getSynergyTemplateFile(templateName);
        try{
            FileUtils.writeLines(toDoFile, items);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
