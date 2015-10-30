package com.nineworldsdeep.gauntlet.synergy.v1;

import android.os.Environment;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.nineworldsdeep.gauntlet.R;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//TODO: refactor into package v2
public class SynergyActivity extends ActionBarActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private Spinner spnLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy);

        addItemsToSpinner();

        addListenerForSpinnerItemSelection();

        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        readItems();

        setupListViewListener();
    }

    public ListView getListView(){
        return (ListView) findViewById(R.id.lvItems);
    }

    // add to do spnLists
    public void addItemsToSpinner() {

        spnLists = (Spinner) findViewById(R.id.spnLists);
        List<String> list = getListNames();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLists.setAdapter(dataAdapter);
    }

    //attached a long click listener to the listview
    private void setupListViewListener(){

        //TODO: remove after testing
        boolean useMultiMode = true;

        if(useMultiMode){

            //working on context action bar
            //http://developer.android.com/guide/topics/ui/menus.html#CAB
            final ListView listView = getListView();
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                      long id, boolean checked) {
                    // Here you can do something when items are selected/de-selected,
                    // such as update the title in the CAB
                    String selectedCount = listView.getCheckedItemCount() + " selected";
                    mode.setTitle(selectedCount);
                    Toast.makeText(getApplicationContext(), "setTitle() called", Toast.LENGTH_LONG).show();
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    // Respond to clicks on the actions in the CAB
                    switch (item.getItemId()) {
                        case R.id.menu_complete:
                            completeSelectedItems();
                            mode.finish(); // Action picked, so close the CAB
                            return true;
                        case R.id.menu_move:
                            Toast.makeText(getApplicationContext(), "menu move selected", Toast.LENGTH_LONG).show();
                            mode.finish();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    // Inflate the menu for the CAB
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.menu_synergy_context, menu);
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    // Here you can make any necessary updates to the activity when
                    // the CAB is removed. By default, selected items are deselected/unchecked.
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    // Here you can perform updates to the CAB due to
                    // an invalidate() request
                    return false;
                }
            });

        }else{

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
    }

    private void completeSelectedItems(){

        Toast.makeText(getApplicationContext(), "completeSelectedItems()", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        File toDoFile = getToDoFile();
        try{
            items = new ArrayList<String>(FileUtils.readLines(toDoFile));
        }catch(IOException ex){
            items = new ArrayList<>();
        }

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
    }

    private List<String> getListNames(){

        String[] exts = {"txt"};
        List<String> lst = new ArrayList<String>();

        for (File f : FileUtils.listFiles(getToDoDirectory(), exts, false)){
            lst.add(FilenameUtils.removeExtension(f.getName()));
        }

        return lst;
    }

    public void addListenerForSpinnerItemSelection() {
        spnLists = (Spinner) findViewById(R.id.spnLists);
        spnLists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                readItems(); //change current list
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public String getListName(){
        spnLists = (Spinner) findViewById(R.id.spnLists);

        Object obj = spnLists.getSelectedItem();
        if(obj == null){
            obj = "default";
        }
        return String.valueOf(obj);
    }

    private void writeItems(){
        File toDoFile = getToDoFile();
        try{
            FileUtils.writeLines(toDoFile, items);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private File getToDoDirectory(){

        File root = Environment.getExternalStorageDirectory();
        File synergyDir = new File(root.getAbsolutePath() + "/NWD/synergy");
        if(!synergyDir.exists()){
            synergyDir.mkdirs();
        }
        return synergyDir;
    }

    private File getToDoFile(){
        boolean useOldFileStrategy = false;

        if(useOldFileStrategy) {
            //copied this from the web, adapting
            //File root = android.os.Environment.getExternalStorageDirectory();
            //File dir = new File (root.getAbsolutePath() +"/mydir");
            //dir.mkdirs();

            File root = Environment.getExternalStorageDirectory();
            File filesDir = new File(root.getAbsolutePath() + "/NWD/text");

            //String filesDir = "/storage/emulated/0/NWD/text/";
            return new File(filesDir, "todo.txt");
        }else{
            String listName = getListName();
            File filesDir = getToDoDirectory();
            return new File(filesDir, listName + ".txt");
        }
    }
}
