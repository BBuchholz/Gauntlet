package com.nineworldsdeep.gauntlet.synergy.v1;

import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyListFile;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated use v2.SynergyListActivity
 */
@Deprecated
public class SynergyListActivity extends AppCompatActivity {

    private List<String> items;
    private String listName;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_list);

        //set listName
        Intent intent = getIntent();

        listName =
                intent.getStringExtra(
                        SynergyMasterListActivity.EXTRA_LISTNAME);

        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        readItems();

        setupListViewListener();
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

    private void promptConfirmPush(){

       if(Utils.containsTimeStamp(listName)){

           AlertDialog.Builder builder = new AlertDialog.Builder(this);

           final String pushToName =
                   Utils.incrementTimeStampInString_yyyyMMdd(listName);

           builder.setTitle("Push Tasks")
                   .setMessage("Push tasks to " + pushToName + "?")
                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           String pushed = SynergyUtils.push(listName);
                           Utils.toast(getApplicationContext(),
                                   "tasks pushed to " + pushed);
                           readItems();
                       }
                   })
                   .setNegativeButton("No", null)
                   .show();

       }else{

           Utils.toast(getApplicationContext(),
                   "push only affects timestamped lists");
       }
    }

    private void promptConfirmArchive(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final boolean expired = Utils.isTimeStampExpired_yyyyMMdd(listName);
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
                        SynergyUtils.archive(listName, expired);
                        Utils.toast(getApplicationContext(), "tasks archived");
                        readItems();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void onAddItemClick(View view) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        addItem(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void addItem(String itemText){
        items.add(getAddItemIndex(), itemText);
        itemsAdapter.notifyDataSetChanged();
    }

    private int getAddItemIndex(){
        //this method is used to add new items above completed items, but still at bottom of list
        int idx = items.size();

        while(idx > 0 && items.get(idx - 1).startsWith("completed={"))
            idx--;

        return idx;
    }

    private void readItems(){
        SynergyListFile slf = new SynergyListFile(listName);

        slf.loadItems();

        items = slf.getItems();

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
    }

    //attached a long click listener to the listview
    private void setupListViewListener(){

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView,
                                                   View view,
                                                   int idx,
                                                   long id) {

                        //removes the item within array at position
                        //items.remove(idx);

                        //move to bottom of list
                        String removedItem = items.remove(idx);

                        if (removedItem.startsWith("completed={") && removedItem.endsWith("}")) {
                            //completed item being changed to incomplete
                            //removedItem = removedItem.replace("completed={", "");
                            //removedItem = removedItem.replace("}", "");
                            int beginIndex = removedItem.indexOf("{") + 1;
                            int endIndex = removedItem.lastIndexOf("}");
                            removedItem = removedItem.substring(beginIndex, endIndex);
                            items.add(0, removedItem);
                        } else {
                            //incomplete item being changed to complete
                            removedItem = "completed={" + removedItem + "}";
                            items.add(removedItem);
                        }

                        //refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        //return true consumes the long click event (marks it handled)
                        return true;
                    }
                }
        );

    }

    private void writeItems(){

        SynergyListFile slf = new SynergyListFile(listName);

        slf.loadItems(items);

        slf.save();
    }

}
