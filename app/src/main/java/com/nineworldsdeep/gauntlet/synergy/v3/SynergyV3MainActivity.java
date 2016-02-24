package com.nineworldsdeep.gauntlet.synergy.v3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyListFile;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

public class SynergyV3MainActivity extends AppCompatActivity {

    public static final String EXTRA_SYNERGYMAIN_LISTNAME =
            "com.nineworldsdeep.gauntlet.SYNERGYMAINACTIVITY_LISTNAME";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_synergy_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id == R.id.action_show_archive){
            startActivity(new Intent(this, SynergyArchivesActivity.class));
            return true;
        }

        if (id == R.id.action_show_templates){
            startActivity(new Intent(this, SynergyTemplatesActivity.class));
            return true;
        }

        if(id == R.id.action_push_all){
            Utils.toast(this, "Sucks yo, but push all is not yet implemented...");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Synergy V3");

        refreshLayout();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        refreshLayout();
    }

    private void refreshLayout(){

        ListView lvItems = (ListView)findViewById(R.id.lvItems);

        readItems(lvItems);
        setupListViewListener(lvItems);
    }

    public void onAddItemClick(View view) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemText = itemText.trim();

        itemText = Utils.toPascalCase(itemText);

        if(!Utils.stringIsNullOrWhitespace(itemText)){

            SynergyListFile slf =
                    new SynergyListFile(itemText);
            slf.loadItems(); //just in case it already exists
            slf.save();
            etNewItem.setText("");
            readItems();
        }
    }

    private void setupListViewListener(final ListView lvItems) {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int idx,
                                    long id) {

                //get selected list name
                String selectedList = (String) lvItems.getItemAtPosition(idx);

                Intent intent = new Intent(view.getContext(),
                        SynergyListActivity.class);
                intent.putExtra(EXTRA_SYNERGYMAIN_LISTNAME, selectedList);
                startActivity(intent);

            }
        });
    }

    private void readItems(){
        readItems((ListView)findViewById(R.id.lvItems));
    }

    private void readItems(ListView lvItems) {

        lvItems.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        SynergyUtils.getAllListNames()));
    }

}
