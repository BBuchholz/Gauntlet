package com.nineworldsdeep.gauntlet.synergy.v3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.nineworldsdeep.gauntlet.synergy.v2.SplitItemActivity;
import com.nineworldsdeep.gauntlet.tapestry.ConfigFile;
import com.nineworldsdeep.gauntlet.tapestry.TapestryUtils;

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
    private static final int MENU_CONTEXT_MOVE_TO_LIST_ID = 8;
    private static final int MENU_CONTEXT_MOVE_TO_LYRICS_ID = 9;
    private static final int MENU_CONTEXT_MOVE_TO_FRAGMENTS_ID = 10;
    private static final int MENU_CONTEXT_EDIT_ITEM = 11;
    public static final int REQUEST_RESULT_EDIT_ITEM = 12;

    private SynergyListFile mSlf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_list);

        //set listName
        Intent intent = getIntent();

        String listName =
                intent.getStringExtra(
                        SynergyV3MainActivity.EXTRA_SYNERGYMAIN_LISTNAME); //TODO: this EXTRA should be in this class, refactor when you have time to test

        setTitle(listName);

        refreshLayout(listName);
    }

    private ListView getListView(){

        return (ListView)findViewById(R.id.lvItems);
    }

    private void refreshLayout(String listName){

        ListView lvItems =
                getListView();

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

        String title = mSlf.get(info.position).getItem();

        menu.setHeaderTitle(title);

        menu.add(Menu.NONE, MENU_CONTEXT_COMPLETION_STATUS_ID, Menu.NONE, "Toggle Completed Status");

        if(Utils.containsTimeStamp(mSlf.getListName())){

            menu.add(Menu.NONE, MENU_CONTEXT_SHELVE_ID, Menu.NONE, "Shelve");

        }else{

            menu.add(Menu.NONE, MENU_CONTEXT_QUEUE_ID, Menu.NONE, "Queue");
        }

        if(mSlf.getListName().startsWith("Fragments")){

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_LYRICS_ID,
                    Menu.NONE, "Move To Lyrics");

        }else{

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_TOP_ID,
                    Menu.NONE, "Move To Top");

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_BOTTOM_ID,
                    Menu.NONE, "Move To Bottom");

        }

        if(mSlf.getListName().startsWith("Lyric")){

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FRAGMENTS_ID,
                    Menu.NONE, "Move To Fragments");
        }

        menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_LIST_ID,
                Menu.NONE, "Move To List");

        menu.add(Menu.NONE, MENU_CONTEXT_SPLIT_ITEM_ID,
                Menu.NONE, "Split Item");

        menu.add(Menu.NONE, MENU_CONTEXT_EDIT_ITEM,
                Menu.NONE, "Edit Item");
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

            case MENU_CONTEXT_MOVE_TO_LYRICS_ID:

                moveToLyrics(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FRAGMENTS_ID:

                moveToFragments(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_TOP_ID:

                moveToTop(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_BOTTOM_ID:

                moveToBottom(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_LIST_ID:

                moveToList(info.position);

                return true;

            case MENU_CONTEXT_SPLIT_ITEM_ID:

                splitItem(info.position);

                return true;

            case MENU_CONTEXT_EDIT_ITEM:

                editItem(info.position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void editItem(int position) {

        Intent intent = new Intent(this, SynergyEditItemActivity.class);

        intent.putExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, position);
        intent.putExtra(Extras.STRING_SYNERGY_LINE_ITEM_RAW_TEXT,
                mSlf.get(position).toLineItem());

        startActivityForResult(intent, REQUEST_RESULT_EDIT_ITEM);
    }

    private void splitItem(int position) {
        Intent intent = new Intent(this, SplitItemActivity.class);

        intent.putExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, position);
        intent.putExtra(Extras.STRING_SYNERGY_LIST_ITEM_TEXT,
                mSlf.get(position).getItem());

        startActivityForResult(intent, REQUEST_RESULT_SPLIT_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQUEST_RESULT_SPLIT_ITEM && data != null)
        {
            int pos = data.getIntExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, -1);

            if(pos > -1) {

                ArrayList<String> lst = (ArrayList<String>)
                        data.getSerializableExtra(Extras.ARRAYLIST_STRING_LIST_ITEMS);

                if(lst != null) {

                    ArrayList<SynergyListItem> sliList = new ArrayList<>();

                    for(String s : lst){
                        sliList.add(new SynergyListItem(s));
                    }

                    // need to specify false for archiveOne() or else
                    // remove gets called twice if sliList has only one item
                    mSlf.archiveOne(mSlf.replace(pos, sliList), false);
                    mSlf.save();
                    refreshListItems();

                }
            }
        }

        if(requestCode== REQUEST_RESULT_EDIT_ITEM && data != null){

            int pos = data.getIntExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, -1);

            if(pos > -1){

                String newRawText =
                        data.getStringExtra(Extras.STRING_SYNERGY_LINE_ITEM_RAW_TEXT);

                Utils.toast(this, pos + " : [" + newRawText + "]");

                // just a hack cuz i don't feel like
                // overloading the replace method right now
                ArrayList<SynergyListItem> lst = new ArrayList<>();
                lst.add(new SynergyListItem(newRawText));

                // need to specify false for archiveOne() or else
                // remove gets called twice if sliList has only one item
                mSlf.archiveOne(mSlf.replace(pos, lst), false);
                mSlf.save();
                refreshListItems();
            }
        }

        if(requestCode== RESULT_CANCELED){
            //do nothing
            //it crashes on back button without this, see:
            //http://stackoverflow.com/questions/20782619/failure-delivering-result-resultinfo
        }
    }

    private void moveToBottom(int pos) {

        int moveTo = getAddItemIndex() - 1;

        if(SynergyUtils.listItemIsCompleted(mSlf.get(pos))){

            moveTo = mSlf.size() - 1;
        }

        mSlf.move(pos, moveTo);
        mSlf.save();

        refreshListItems();

    }

    private void moveToTop(int pos) {

        int moveTo = 0;

        if(SynergyUtils.listItemIsCompleted(mSlf.get(pos))){

            moveTo = getAddItemIndex();
        }

        mSlf.move(pos, moveTo);
        mSlf.save();

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

        } else if (id == R.id.action_archive){

            promptConfirmArchive();
            return true;

        } else if (id == R.id.action_update_template){

            promptUpdateTemplate();
            return true;

        } else if (id == R.id.action_push){

            promptConfirmPush();
            return true;

        } else if (id == R.id.action_shelveAll){

            promptShelveAll();
            return true;

        } else if(id == R.id.action_seed){

            String currentDevice = TapestryUtils.getCurrentDevice();

            if(currentDevice == null) {
                //prompt for one
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.prompt, null);

                TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
                tv.setText("No Device Set, Enter Device Name, Then Try Again: ");

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
                                    public void onClick(DialogInterface dialog, int id) {

                                        String name = userInput.getText().toString();

                                        //prevent hyphens, which are used for junctions
                                        name = name.replace("-", "_");

                                        ConfigFile f = new ConfigFile();
                                        f.setDeviceName(name);
                                        f.save();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                Utils.toast(this, "seed discarded");

            }else{

                String currentGardenName = TapestryUtils.getCurrentGardenName(currentDevice);

                TapestryUtils.linkNodeToSynergyList(currentGardenName,
                                                    mSlf.getListName());

                Utils.toast(this, "seed planted: " + currentGardenName);
            }

            return true;

        } else if(id == R.id.action_seed_new){

            String currentDevice = TapestryUtils.getCurrentDevice();

            if(currentDevice == null) {
                //prompt for one
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.prompt, null);

                TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
                tv.setText("No Device Set, Enter Device Name, Then Try Again: ");

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
                                    public void onClick(DialogInterface dialog, int id) {

                                        String name = userInput.getText().toString();

                                        //prevent hyphens, which are used for junctions
                                        name = name.replace("-", "_");

                                        ConfigFile f = new ConfigFile();
                                        f.setDeviceName(name);
                                        f.save();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                Utils.toast(this, "seed discarded");

            }else{

                String currentGardenName = TapestryUtils.getNewGardenName(currentDevice);

                TapestryUtils.linkNodeToSynergyList(currentGardenName,
                                                    mSlf.getListName());

                Utils.toast(this, "seed planted: " + currentGardenName);
            }

            return true;

        } else if (id == R.id.action_link_to_node){

            LayoutInflater li = LayoutInflater.from(SynergyListActivity.this);
            View promptsView = li.inflate(R.layout.prompt, null);

            TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
            tv.setText("Enter Node Name: ");

            android.app.AlertDialog.Builder alertDialogBuilder =
                    new android.app.AlertDialog.Builder(SynergyListActivity.this);

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

                                    // get list name from userInput and move
                                    String processedName =
                                            TapestryUtils.processNodeName(
                                                    userInput.getText().toString());

                                    TapestryUtils
                                            .linkNodeToSynergyList(processedName,
                                                    mSlf.getListName());

                                    Utils.toast(SynergyListActivity.this, "linked");

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

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void promptUpdateTemplate() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String trimmedName = Utils.trimTimeStamp_yyyyMMdd(mSlf.getListName());

        String msg = "Update Template: " + trimmedName + "?";

        builder.setTitle("UpdateTemplate")
                .setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        SynergyUtils.updateTemplate(trimmedName, mSlf);
                        Utils.toast(getApplicationContext(), "template updated");
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void promptShelveAll() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Shelve All")
                .setMessage("Shelve All Categorized Items?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        while(mSlf.hasCategorizedItems()){

                            int pos =
                                    mSlf.getFirstCategorizedItemPosition();
                            shelvePosition(pos);
                        }

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void promptConfirmArchive(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final boolean expired = Utils.isTimeStampExpired_yyyyMMdd(mSlf.getListName());
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
                        SynergyUtils.archive(mSlf.getListName(), expired);
                        Utils.toast(getApplicationContext(), "tasks archived");
                        readItems(mSlf.getListName());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    private void promptConfirmPush(){

        if(Utils.containsTimeStamp(mSlf.getListName())){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final String pushToName =
                    Utils.incrementTimeStampInString_yyyyMMdd(mSlf.getListName());

            builder.setTitle("Push Tasks")
                    .setMessage("Push tasks to " + pushToName + "?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String pushed = SynergyUtils.push(mSlf.getListName());
                            Utils.toast(getApplicationContext(),
                                    "tasks pushed to " + pushed);
                            readItems(mSlf.getListName());
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

        if(!Utils.containsTimeStamp(mSlf.getListName())){

            //Utils.toast(this, "queueToDailyToDo position " + position);
            mSlf.queueToDailyToDo(position);
            Utils.toast(this, "queued");
            refreshListItems();

        }else{
            Utils.toast(this, "Queue only applies to non-timestamped lists");
        }
    }

    private void moveToLyrics(final int position){

        SynergyUtils.move(mSlf, position, "Lyrics");
        Utils.toast(getApplicationContext(), "moved to Lyrics");
        refreshLayout(mSlf.getListName());
    }

    private void moveToFragments(final int position){

        SynergyUtils.move(mSlf, position, "Fragments");
        Utils.toast(getApplicationContext(), "moved to Fragments");
        refreshLayout(mSlf.getListName());
    }

    private void moveToList(final int position) {

        //Adapted from: http://www.mkyong.com/android/android-prompt-user-input-dialog-example/
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt, null);

        TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
        tv.setText("Enter listName: ");

        android.app.AlertDialog.Builder alertDialogBuilder =
                new android.app.AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        userInput.setText(mSlf.getListName() + "-");
        userInput.setSelection(userInput.getText().length());

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                // get list name from userInput and move
                                String processedName =
                                        Utils.processName(
                                                userInput.getText().toString());

                                SynergyUtils.move(mSlf, position, processedName);

                                Utils.toast(getApplicationContext(), "moved");
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
    }

    private void shelvePosition(final int position) {

        if(Utils.containsTimeStamp(mSlf.getListName())){

            Utils.toast(this, "shelve position " + position);
            String category = mSlf.get(position).getCategory();

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
                                        mSlf.shelve(position, userInput.getText().toString());
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

                mSlf.shelve(position, category);
                Utils.toast(this, "shelved");
            }

            refreshListItems();

        }else{
            Utils.toast(this, "Shelve only applies to timestamped lists");
        }
    }

    private void toggleCompletionStatusAtPosition(int position){
//
//        //move to bottom of list
//        SynergyListItem removedItem = mSlf.remove(position);
//
//        if (removedItem.isCompleted()) {
//            //completed item being changed to incomplete
//            int beginIndex = removedItem.getItem().indexOf("{") + 1;
//            int endIndex = removedItem.getItem().lastIndexOf("}");
//            removedItem = removedItem.getItem().substring(beginIndex, endIndex);
//            mSlf.add(0, removedItem);
//        } else {
//            //incomplete item being changed to complete
//            if(SynergyUtils.isCategorizedItem(removedItem)){
//
//                SynergyUtils.completeCategorizedItem(removedItem);
//
//            }else {
//
//                removedItem = "completed={" + removedItem + "}";
//                mSlf.add(removedItem);
//            }
//        }
//
//        writeItems();
//
//        refreshListItems();

        if(!mSlf.get(position).isCompleted()){

            mSlf.get(position).markCompleted();
            moveToBottom(position);

        }else{

            mSlf.get(position).markIncomplete();
            moveToTop(position);
        }
    }

    private void writeItems() {

        if(mSlf != null){
            mSlf.save();
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

        mSlf = new SynergyListFile(listName);
        mSlf.loadItems();

        setListViewAdapter(lvItems);
    }

    private void setListViewAdapter(ListView lvItems) {

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                mSlf.getItems()));
    }

    private void refreshListItems(){

        setListViewAdapter(getLvItems());
    }

    public void onAddItemClick(View view) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        itemText = Utils.removeHardReturns(itemText);

        if(!Utils.stringIsNullOrWhitespace(itemText)){

            mSlf.add(getAddItemIndex(), itemText);
            etNewItem.setText("");
            writeItems();
            refreshListItems();

        }else{

            Utils.toast(this, "item cannot be empty or whitespace");
        }
    }

    private int getAddItemIndex(){
        //this method is used to add new items above completed items, but still at bottom of list
        int idx = mSlf.size();

//        while(idx > 0 && mSlf.get(idx - 1).startsWith("completed={"))
        while(idx > 0 && SynergyUtils.listItemIsCompleted(mSlf.get(idx - 1)))
            idx--;

        return idx;
    }
}
