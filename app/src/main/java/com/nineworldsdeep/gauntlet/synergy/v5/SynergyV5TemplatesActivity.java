package com.nineworldsdeep.gauntlet.synergy.v5;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

public class SynergyV5TemplatesActivity extends AppCompatActivity {

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
                        (String) lvItems.getItemAtPosition(idx);

                Intent intent = new Intent(view.getContext(),
                        SynergyV5TemplateActivity.class);
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
                        (String) lvItems.getItemAtPosition(idx);

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
                        SynergyV5Utils.getAllTemplateNames(this)));
    }

    private void promptConfirmGenFromTemplate(String templateName){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String timestampedListName =
                SynergyV5Utils.getTimeStampedListName(templateName);

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

        SynergyV5List synergyList = new SynergyV5List(timestampedListName);

        if(synergyList.exists()){

            Utils.toast(getApplicationContext(), timestampedListName +
                    " already exists! cannot create...");

        }else{

            SynergyV5List newList =
                    SynergyV5Utils.generateFromTemplate(templateName,
                            timestampedListName);
            newList.save(this, NwdDb.getInstance(this));
            Utils.toast(getApplicationContext(), timestampedListName + " created");
        }

    }

    public void onAddItemClick(View view) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemText = itemText.trim();

        itemText = Utils.processName(itemText);

        if(!Utils.stringIsNullOrWhitespace(itemText)){
            SynergyV5Template template =
                    new SynergyV5Template(itemText);
            template.loadItems(); //just in case it already exists
            template.save();
            etNewItem.setText("");
            readItems();
        }
    }
}
