package com.nineworldsdeep.gauntlet.synergy.v2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.io.File;

public class SynergyTemplatesActivity extends AppCompatActivity {

    public static final String EXTRA_TEMPLATENAME =
            "com.nineworldsdeep.gauntlet.SYNERGY_TEMPLATENAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_templates);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        readItems();
        setupListViewListeners();
    }

    private void setupListViewListeners() {

        final ListView lvItems =
                (ListView)findViewById(R.id.lvItems);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int idx,
                                    long id) {

                //get selected template name
                String selectedTemplate =
                        (String)lvItems.getItemAtPosition(idx);

                Intent intent = new Intent(view.getContext(),
                        SynergyTemplateActivity.class);
                intent.putExtra(EXTRA_TEMPLATENAME, selectedTemplate);
                startActivity(intent);

            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view,
                                           int idx,
                                           long id) {

                //get selected template name
                String selectedTemplate =
                        (String)lvItems.getItemAtPosition(idx);

                promptConfirmGenFromTemplate(selectedTemplate);

                return true;
            }
        });
    }

    private void readItems() {

        ListView lvItems =
                (ListView)findViewById(R.id.lvItems);

        lvItems.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        SynergyUtils.getAllTemplateNames()));
    }

    private void promptConfirmGenFromTemplate(String templateName){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String timestampedListName =
                SynergyUtils.getTimeStampedListName(templateName);

        final String tName = templateName;

        builder.setTitle("Generate From Template")
                .setMessage("Generate " + timestampedListName + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        createTimeStampedList(tName, timestampedListName);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void createTimeStampedList(String templateName,
                                       String timestampedListName){

        SynergyListFile slf = new SynergyListFile(timestampedListName);
        File f = slf.getSynergyFile();

        if(f.exists()){

            Utils.toast(getApplicationContext(), timestampedListName +
                    " already exists! cannot create...");

        }else{

            SynergyListFile newSlf =
                    SynergyUtils.generateFromTemplate(templateName,
                            timestampedListName);
            newSlf.save();
            Utils.toast(getApplicationContext(), timestampedListName + " created");
        }

    }

}
