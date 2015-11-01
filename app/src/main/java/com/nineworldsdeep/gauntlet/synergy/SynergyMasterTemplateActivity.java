package com.nineworldsdeep.gauntlet.synergy;

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
import com.nineworldsdeep.gauntlet.synergy.v1.SynergyListFile;
import com.nineworldsdeep.gauntlet.synergy.v1.SynergyTemplateFile;

import java.io.File;
import java.util.List;

//TODO: refactor into package v2
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
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvItems = (ListView)findViewById(R.id.lvItems);
        readItems();

        setupListViewListeners();
    }

    private void setupListViewListeners(){

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int idx,
                                    long id) {

                //get selected template name
                String selectedTemplate = items.get(idx);

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
                String selectedTemplate = items.get(idx);

                promptConfirmGenFromTemplate(selectedTemplate);

                return true;
            }
        });

    }

    private void promptConfirmGenFromTemplate(String templateName){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String timestampedListName =
                SynergyTemplateFile.getTimeStampedListName(templateName);

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

        File f = SynergyListFile.getSynergyFile(timestampedListName);

        if(f.exists()){

            Utils.toast(getApplicationContext(), timestampedListName +
                    " already exists! cannot create...");

        }else{

            SynergyListFile.generateFromTemplate(templateName, timestampedListName);
            Utils.toast(getApplicationContext(), timestampedListName + " created");
        }

    }
}
