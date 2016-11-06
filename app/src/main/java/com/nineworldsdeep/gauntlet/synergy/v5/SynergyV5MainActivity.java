package com.nineworldsdeep.gauntlet.synergy.v5;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.ListBaseActivity;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.synergy.v2.ListEntry;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListOrdering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SynergyV5MainActivity extends ListBaseActivity {

    public static final String EXTRA_SYNERGYMAIN_LISTNAME =
            "com.nineworldsdeep.gauntlet.SYNERGYMAINACTIVITY_LISTNAME";

    private static final int MENU_CONTEXT_COPY_LIST = 1;
    private static final int MENU_CONTEXT_RENAME_LIST = 2;
    private static final int MENU_CONTEXT_COPY_NAME_TO_CLIPBOARD = 3;

    //public static boolean ORDER_BY_COUNT = false;
    public SynergyListOrdering ordering;
    private List<ListEntry> currentListEntries;


    public void setOrdering(SynergyListOrdering ordering) {
        this.ordering = ordering;
        String orderName = "";

        switch (ordering){

            case ByNameDescending:
                orderName = "NameDesc";
                break;

            case ByNameAscending:
                orderName = "NameAsc";
                break;

            case ByItemCountDescending:
                orderName = "CountDesc";
                break;

            case ByItemCountAscending:
                orderName = "CountAsc";
                break;
        }

        setTitle("Synergy (" + orderName + ")");
    }

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
            startActivity(new Intent(this, SynergyV5ArchivesActivity.class));
            return true;
        }

        if (id == R.id.action_show_templates){
            startActivity(new Intent(this, SynergyV5TemplatesActivity.class));
            return true;
        }

        if (id == R.id.action_toggle_sort){

            incrementOrderingSelection();
            refreshLayout();

            return true;
        }

        if(id == R.id.action_push_all){

            pushAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void incrementOrderingSelection() {

        switch (ordering){

            case ByNameDescending:
                setOrdering(SynergyListOrdering.ByNameAscending);
                break;

            case ByNameAscending:
                setOrdering(SynergyListOrdering.ByItemCountDescending);
                break;

            case ByItemCountDescending:
                setOrdering(SynergyListOrdering.ByItemCountAscending);
                break;

            case ByItemCountAscending:
                setOrdering(SynergyListOrdering.ByNameDescending);
                break;
        }
    }

    private void pushAll() {

        List<String> listNames = new ArrayList<>();

        for(ListEntry le : currentListEntries){

            if(Utils.containsTimeStamp(le.getListName())){
                listNames.add(le.getListName());
            }
        }

        SynergyV5Utils.pushAll(listNames);

        refreshLayout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Synergy V5");
        setOrdering(SynergyListOrdering.ByNameAscending);

        refreshLayout();
    }

    @Override
    protected ListView getListView() {

        return (ListView)findViewById(R.id.lvItems);
    }

    public void onAddItemClick(View view) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemText = itemText.trim();

        itemText = Utils.processName(itemText);

        if(!Utils.stringIsNullOrWhitespace(itemText)){

//            SynergyListFile slf =
//                    new SynergyListFile(itemText);
//            slf.loadItems(); //just in case it already exists
//            slf.save();
//            etNewItem.setText("");
//            readItems();

            SynergyV5List synLst = new SynergyV5List(itemText);

            NwdDb.getInstance(this).save(synLst);

            etNewItem.setText("");

            Utils.toast(this, "List: " + synLst.getListName() + " saved.");

            readItems();
        }
    }

    @Override
    protected void setupListViewListener(final ListView lvItems) {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int idx,
                                    long id) {

                //get selected list name
                ListEntry selectedList = (ListEntry) lvItems.getItemAtPosition(idx);

                Intent intent = new Intent(view.getContext(),
                        SynergyV5ListActivity.class);
                intent.putExtra(EXTRA_SYNERGYMAIN_LISTNAME,
                                selectedList.getListName());
                startActivity(intent);

            }
        });
    }

    // http://stackoverflow.com/questions/18632331/using-contextmenu-with-listview-in-android
    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        String title = currentListEntries.get(info.position).getListName();

        menu.setHeaderTitle(title);

        menu.add(Menu.NONE, MENU_CONTEXT_COPY_LIST, Menu.NONE, "Copy");
        menu.add(Menu.NONE, MENU_CONTEXT_RENAME_LIST, Menu.NONE, "Rename");
        menu.add(Menu.NONE, MENU_CONTEXT_COPY_NAME_TO_CLIPBOARD,
                    Menu.NONE, "Copy Name To Clipboard");


    }

    // http://stackoverflow.com/questions/18632331/using-contextmenu-with-listview-in-android
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case MENU_CONTEXT_COPY_LIST:

                promptCopyListAtPosition(info.position);

                return true;

            case MENU_CONTEXT_RENAME_LIST:

                promptRenameListAtPosition(info.position);

                return true;

            case MENU_CONTEXT_COPY_NAME_TO_CLIPBOARD:

                copyListNameToClipboard(info.position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void copyListNameToClipboard(int position) {

        String listName = currentListEntries.get(position).getListName();

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("synergy-list-name", listName);
        clipboard.setPrimaryClip(clip);

        Utils.toast(this, "[" + listName + "] copied to clipboard.");
    }

    private void promptRenameListAtPosition(int position) {

        //Adapted from:
        // http://www.mkyong.com/android/android-prompt-user-input-dialog-example/
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

        final String selectedListName = currentListEntries.get(position).getListName();

        userInput.setText(selectedListName);
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

                                SynergyV5Utils.rename(selectedListName, processedName);

                                Utils.toast(getApplicationContext(), "renamed");
                                refreshLayout();
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

    private void promptCopyListAtPosition(int position) {

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

        final String selectedListName = currentListEntries.get(position).getListName();

        userInput.setText(selectedListName);
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

                                SynergyV5Utils.copy(selectedListName, processedName);

                                Utils.toast(getApplicationContext(), "copied");
                                refreshLayout();
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

    private void readItems(){
        readItems((ListView)findViewById(R.id.lvItems));
    }

    @Override
    protected void readItems(ListView lvItems) {

        List<ListEntry> lst =
                SynergyV5Utils.getAllListEntries(this,
                        NwdDb.getInstance(this));

        if(ordering == SynergyListOrdering.ByItemCountDescending){

            Collections.sort(lst, new Comparator<ListEntry>() {

                public int compare(ListEntry one, ListEntry other) {

                    if(one.getItemCount() == other.getItemCount()){
                        return 0;
                    }

                    if (one.getItemCount() > other.getItemCount()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        }

        if(ordering == SynergyListOrdering.ByItemCountAscending){

            Collections.sort(lst, new Comparator<ListEntry>() {

                public int compare(ListEntry one, ListEntry other) {

                    if(one.getItemCount() == other.getItemCount()){
                        return 0;
                    }

                    if (one.getItemCount() < other.getItemCount()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        }

        if(ordering == SynergyListOrdering.ByNameDescending){

            //just invert the default
            Collections.reverse(lst);
        }

        if(ordering == SynergyListOrdering.ByNameAscending){

            //do nothing, it's the default order
        }


        currentListEntries = lst;

        lvItems.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        currentListEntries));
    }

}
