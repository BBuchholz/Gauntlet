package com.nineworldsdeep.gauntlet.synergy.v2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;

public class SynergyListActivity
        extends AppCompatActivity{

    private static final int MENU_CONTEXT_COMPLETION_STATUS_ID = 1;
    private static final int MENU_CONTEXT_SHELVE_ID = 2;
    private static final int MENU_CONTEXT_QUEUE_ID = 3;
    private static final int MENU_CONTEXT_MOVE_TO_TOP_ID = 4;
    private static final int MENU_CONTEXT_MOVE_TO_BOTTOM_ID = 5;
    private static final int MENU_CONTEXT_SPLIT_ITEM_ID = 6;
    public static final int REQUEST_RESULT_SPLIT_ITEM = 7;

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
        registerForContextMenu(lvItems);
    }

    //adapted from: http://stackoverflow.com/questions/18632331/using-contextmenu-with-listview-in-android
    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        String title = slf.get(info.position);

        menu.setHeaderTitle(title);

        menu.add(Menu.NONE, MENU_CONTEXT_COMPLETION_STATUS_ID, Menu.NONE, "Toggle Completed Status");

        if(Utils.containsTimeStamp(slf.getListName())){

            menu.add(Menu.NONE, MENU_CONTEXT_SHELVE_ID, Menu.NONE, "Shelve");

        }else{

            menu.add(Menu.NONE, MENU_CONTEXT_QUEUE_ID, Menu.NONE, "Queue");
        }

        menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_TOP_ID,
                Menu.NONE, "Move To Top");

        menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_BOTTOM_ID,
                Menu.NONE, "Move To Bottom");

        menu.add(Menu.NONE, MENU_CONTEXT_SPLIT_ITEM_ID,
                Menu.NONE, "Split Item");
    }

    //adapted from: http://stackoverflow.com/questions/18632331/using-contextmenu-with-listview-in-android
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case MENU_CONTEXT_COMPLETION_STATUS_ID:

                toggleCompletionStatusAtPosition(info.position);

                return true;

            case MENU_CONTEXT_SHELVE_ID:

                shelvePosition(info.position);

                return true;

            case MENU_CONTEXT_QUEUE_ID:

                queuePosition(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_TOP_ID:

                moveToTop(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_BOTTOM_ID:

                moveToBottom(info.position);

                return true;

            case MENU_CONTEXT_SPLIT_ITEM_ID:

                splitItem(info.position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void splitItem(int position) {
        Intent intent = new Intent(this, SplitItemActivity.class);

        intent.putExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, position);
        intent.putExtra(Extras.STRING_SYNERGY_LIST_ITEM_TEXT, slf.get(position));

        startActivityForResult(intent, REQUEST_RESULT_SPLIT_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQUEST_RESULT_SPLIT_ITEM)
        {
            int pos = data.getIntExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, -1);

            if(pos > -1) {

                ArrayList<String> lst = (ArrayList<String>)
                        data.getSerializableExtra(SplitItemActivity.TEST_LIST);

                if(lst != null) {
                    for (String itm : lst)
                        Utils.toast(this, itm);
                }
            }
        }
    }

    private void moveToBottom(int pos) {

        int moveTo = getAddItemIndex() - 1;

        if(SynergyUtils.listItemIsCompleted(slf.get(pos))){

            moveTo = slf.size() - 1;
        }

        slf.move(pos, moveTo);
        slf.save();

        refreshListItems();

    }

    private void moveToTop(int pos) {

        int moveTo = 0;

        if(SynergyUtils.listItemIsCompleted(slf.get(pos))){

            moveTo = getAddItemIndex();
        }

        slf.move(pos, moveTo);
        slf.save();

        refreshListItems();
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

        if (id == R.id.action_shelveAll){
            promptShelveAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptShelveAll() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Shelve All")
                .setMessage("Shelve All Categorized Items?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        while(SynergyUtils.hasCategorizedItems(slf)){

                            int pos =
                                    SynergyUtils.getFirstCategorizedItemPosition(slf);
                            shelvePosition(pos);
                        }

                    }
                })
                .setNegativeButton("No", null)
                .show();
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

    private void queuePosition(int position) {

        if(!Utils.containsTimeStamp(slf.getListName())){

            //Utils.toast(this, "queueToDailyToDo position " + position);
            SynergyUtils.queueToDailyToDo(slf, position);
            Utils.toast(this, "queued");
            refreshListItems();

        }else{
            Utils.toast(this, "Queue only applies to non-timestamped lists");
        }
    }

    private void shelvePosition(final int position) {

        if(Utils.containsTimeStamp(slf.getListName())){

            Utils.toast(this, "shelve position " + position);
            String category = SynergyUtils.parseCategory(slf.get(position));

            if(Utils.stringIsNullOrWhitespace(category)){

                //Adapted from: http://www.mkyong.com/android/android-prompt-user-input-dialog-example/
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.prompt, null);

                TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
                tv.setText("Enter category: ");

                android.app.AlertDialog.Builder alertDialogBuilder =
                        new android.app.AlertDialog.Builder(this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        // get category from userInput and shelve
                                        SynergyUtils.shelve(slf, position, userInput.getText().toString());
                                        Utils.toast(getApplicationContext(), "shelved");
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }else {

                SynergyUtils.shelve(slf, position, category);
                Utils.toast(this, "shelved");
            }

            refreshListItems();

        }else{
            Utils.toast(this, "Shelve only applies to timestamped lists");
        }
    }

    private void toggleCompletionStatusAtPosition(int position){

        //move to bottom of list
        String removedItem = slf.remove(position);

        if (SynergyUtils.listItemIsCompleted(removedItem)) {
            //completed item being changed to incomplete
            int beginIndex = removedItem.indexOf("{") + 1;
            int endIndex = removedItem.lastIndexOf("}");
            removedItem = removedItem.substring(beginIndex, endIndex);
            slf.add(0, removedItem);
        } else {
            //incomplete item being changed to complete
            if(SynergyUtils.isCategorizedItem(removedItem)){

                SynergyUtils.completeCategorizedItem(removedItem);

            }else {

                removedItem = "completed={" + removedItem + "}";
                slf.add(removedItem);
            }
        }

        writeItems();

        refreshListItems();
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

    private void setListViewAdapter(ListView lvItems) {

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                slf.getItems()));
    }

    private void refreshListItems(){

        setListViewAdapter(getLvItems());
    }

    public void onAddItemClick(View view) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        if(!Utils.stringIsNullOrWhitespace(itemText)){

            slf.add(getAddItemIndex(), itemText);
            etNewItem.setText("");
            writeItems();
            refreshListItems();

        }else{

            Utils.toast(this, "item cannot be empty or whitespace");
        }
    }

    private int getAddItemIndex(){
        //this method is used to add new items above completed items, but still at bottom of list
        int idx = slf.size();

        while(idx > 0 && slf.get(idx - 1).startsWith("completed={"))
            idx--;

        return idx;
    }
}
