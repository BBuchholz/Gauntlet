package com.nineworldsdeep.gauntlet;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SynergyListActivity extends ActionBarActivity {

    private ArrayList<String> items;
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

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
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
        File toDoFile = SynergyListFile.getSynergyFile(listName);
        try{
            items = new ArrayList<String>(FileUtils.readLines(toDoFile));
        }catch(IOException ex){
            items = new ArrayList<>();
        }

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
        File toDoFile = SynergyListFile.getSynergyFile(listName);
        try{
            FileUtils.writeLines(toDoFile, items);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

}
