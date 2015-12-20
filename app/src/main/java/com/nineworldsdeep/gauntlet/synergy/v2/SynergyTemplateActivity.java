package com.nineworldsdeep.gauntlet.synergy.v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class SynergyTemplateActivity extends AppCompatActivity {

    private static final int MENU_CONTEXT_QUEUE_ID = 3;

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

        ListView lvItems =
                (ListView) findViewById(R.id.lvItems);

        registerForContextMenu(lvItems);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        String title = stf.get(info.position);

        menu.setHeaderTitle(title);

        menu.add(Menu.NONE, MENU_CONTEXT_QUEUE_ID, Menu.NONE, "Queue");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case MENU_CONTEXT_QUEUE_ID:

                queuePosition(info.position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void queuePosition(int position) {

        SynergyUtils.queueFromTemplate(stf, position);
        Utils.toast(this, "queued");
        refreshListItems();

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
