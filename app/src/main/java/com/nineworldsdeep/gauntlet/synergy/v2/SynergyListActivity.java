package com.nineworldsdeep.gauntlet.synergy.v2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v1.SynergyMasterListActivity;

import java.util.ArrayList;

public class SynergyListActivity
        extends AppCompatActivity
        implements IReadItemsCallback {

    private SynergyListFile slf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_list);

        //set listName
        Intent intent = getIntent();

        String listName =
                intent.getStringExtra(
                        SynergyMainActivity.EXTRA_SYNERGYMAIN_LISTNAME);

        ListView lvItems =
                (ListView)findViewById(R.id.lvItems);

        readItems(lvItems, listName);
        setupListViewListener(lvItems);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_synergy_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_archive){
            promptConfirmArchive();
            return true;
        }

        if (id == R.id.action_push){
            promptConfirmPush();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptConfirmArchive(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final boolean expired = Utils.isTimeStampExpired_yyyyMMdd(slf.getListName());
        String msg;

        if(expired){

            msg = "Archive all tasks?";

        }else{

            msg = "Archive completed tasks?";
        }

        builder.setTitle("Archive Tasks")
                .setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SynergyUtils.archive(slf.getListName(), expired);
                        Utils.toast(getApplicationContext(), "tasks archived");
                        readItems(slf.getListName());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    private void promptConfirmPush(){

        if(Utils.containsTimeStamp(slf.getListName())){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final String pushToName =
                    Utils.incrementTimeStampInString_yyyyMMdd(slf.getListName());

            builder.setTitle("Push Tasks")
                    .setMessage("Push tasks to " + pushToName + "?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String pushed = SynergyUtils.push(slf.getListName());
                            Utils.toast(getApplicationContext(),
                                    "tasks pushed to " + pushed);
                            readItems(slf.getListName());
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }else{

            Utils.toast(getApplicationContext(),
                    "push only affects timestamped lists");
        }
    }

    private void setupListViewListener(final ListView lvItems) {

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view,
                                                   int position,
                                                   long id) {


                        //move to bottom of list
                        String removedItem = slf.remove(position);

                        if (removedItem.startsWith("completed={") && removedItem.endsWith("}")) {
                            //completed item being changed to incomplete
                            //removedItem = removedItem.replace("completed={", "");
                            //removedItem = removedItem.replace("}", "");
                            int beginIndex = removedItem.indexOf("{") + 1;
                            int endIndex = removedItem.lastIndexOf("}");
                            removedItem = removedItem.substring(beginIndex, endIndex);
                            slf.add(0, removedItem);
                        } else {
                            //incomplete item being changed to complete
                            removedItem = "completed={" + removedItem + "}";
                            slf.add(removedItem);
                        }

                        setListViewAdapter(lvItems);

                        writeItems();
                        //return true consumes the long click event (marks it handled)
                        return true;
                    }
                });
    }

    private void writeItems() {

        if(slf != null){
            slf.save();
        }
    }

    public ListView getLvItems(){

        return (ListView)findViewById(R.id.lvItems);
    }

    public void readItems(String listName){

        ListView lvItems = getLvItems();

        readItems(lvItems, listName);
    }

    private void readItems(ListView lvItems, String listName) {

        slf = new SynergyListFile(listName);
        slf.loadItems();

        setListViewAdapter(lvItems);
    }

    private void setListViewAdapter(ListView lvItems){

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                slf.getItems()));
    }

    private void refreshListItems(){

        setListViewAdapter(getLvItems());
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        slf.add(getAddItemIndex(), itemText);
        etNewItem.setText("");
        writeItems();
        refreshListItems();
    }

    private int getAddItemIndex(){
        //this method is used to add new items above completed items, but still at bottom of list
        int idx = slf.size();

        while(idx > 0 && slf.get(idx - 1).startsWith("completed={"))
            idx--;

        return idx;
    }
}
