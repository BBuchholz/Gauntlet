package com.nineworldsdeep.gauntlet.hive;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.IRefreshableUI;
import com.nineworldsdeep.gauntlet.core.ListBaseActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.core.Prompt;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HiveRootsActivity extends ListBaseActivity implements IRefreshableUI {

    private ArrayList<NavigateActivityCommand> mCommands = new ArrayList<>();
    private ArrayList<HiveRoot> mHiveRoots = new ArrayList<>();

    public static final String EXTRA_HIVE_ROOT_ID =
            "com.nineworldsdeep.gauntlet.EXTRA_HIVE_ROOT_ID";
    public static final String EXTRA_HIVE_ROOT_NAME =
            "com.nineworldsdeep.gauntlet.EXTRA_HIVE_ROOT_NAME";

    private static final int MENU_CONTEXT_DEACTIVATE = 1;
    private static final int MENU_CONTEXT_ACTIVATE = 2;
    private static final int MENU_CONTEXT_ENSURE_FOLDERS = 3;


    @Override
    public void onBackPressed(){

        NavigateActivityCommand.navigateTo(
                HomeListActivity.class,
                HiveRootsActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hive_roots);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavigateActivityCommand.navigateTo(
                        HomeListActivity.class,
                        HiveRootsActivity.this);
            }
        });

        populateActiveRootsSelectorSpinner();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                LayoutInflater li = LayoutInflater.from(HiveRootsActivity.this);
                View promptsView = li.inflate(R.layout.prompt, null);

                TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
                tv.setText("Enter Hive Root Name: ");

                android.app.AlertDialog.Builder alertDialogBuilder =
                        new android.app.AlertDialog.Builder(HiveRootsActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                String deviceName = Configuration.getLocalDeviceName();

                if(!hasActiveRootName(deviceName)) {

                    userInput.setText(deviceName);
                    userInput.setSelection(userInput.getText().length());
                }

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        String name = userInput.getText().toString();

                                        if(hiveRootNameIsValid(name)){

                                            NwdDb db = NwdDb.getInstance(HiveRootsActivity.this);
                                            db.open();

                                            db.ensureHiveRootName(name);

                                            refreshLayout();

                                            Snackbar.make(view, "Added Hive Root: " + name, Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();

                                        }else{

                                            Snackbar.make(view, "Invalid Hive Root: " + name, Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        Snackbar.make(view, "Cancelled", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();

                                        dialog.cancel();
                                    }
                                });

                android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Roots");

        //check local device name is set
        NwdDb.getInstance(this).open();
        if(Configuration.getLocalMediaDevice(this,
                        NwdDb.getInstance(this)) == null){

            Prompt.promptSetLocalDeviceDescription(this, this);
        }
    }

    private boolean hasActiveRootName(String rootName) {

        boolean found = false;

        for(HiveRoot hr : mHiveRoots){

            if(hr.getHiveRootName().equalsIgnoreCase(rootName)){

                found = true;
            }
        }

        return found;
    }

    private boolean hiveRootNameIsValid(String name) {

        Matcher match = Pattern.compile("^[0-9a-z-]*$")
            .matcher(name);

        return match.find();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        HiveRoot root = mHiveRoots.get(info.position);

        menu.setHeaderTitle(root.getHiveRootName());

        if(root.isActive()){

            menu.add(Menu.NONE, MENU_CONTEXT_DEACTIVATE, Menu.NONE, "Deactivate");
            menu.add(Menu.NONE, MENU_CONTEXT_ENSURE_FOLDERS, Menu.NONE, "Ensure Folders");

        }else{

            menu.add(Menu.NONE, MENU_CONTEXT_ACTIVATE, Menu.NONE, "Activate");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case MENU_CONTEXT_DEACTIVATE:

                deactivate(info.position);
                return true;

            case MENU_CONTEXT_ACTIVATE:

                activate(info.position);
                return true;

            case MENU_CONTEXT_ENSURE_FOLDERS:

                ensureFolders(info.position);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void ensureFolders(int position) {

        HiveRoot hr = mHiveRoots.get(position);

        for(File folder : Configuration.getFoldersForHiveRoot(hr)){

            if(!folder.exists()){

                folder.mkdirs();
            }
        }

        Utils.toast(this, "ensured");
    }

    private void deactivate(int position) {

        HiveRoot hr = mHiveRoots.get(position);

        hr.deactivate();

        NwdDb db = NwdDb.getInstance(this);
        db.open();

        db.syncByName(this, hr);

        refreshLayout();
    }

    private void activate(int position) {

        HiveRoot hr = mHiveRoots.get(position);

        hr.activate();

        NwdDb db = NwdDb.getInstance(this);
        db.open();

        db.syncByName(this, hr);

        refreshLayout();
    }


    @Override
    protected void onResume(){
        super.onResume();
        refreshLayout();
    }

    @Override
    protected void readItems(ListView lv) {

        mCommands.clear();
        mHiveRoots.clear();

        //detect unregistered root folders
        if(UtilsHive.checkAndRegisterNewRootFolders(NwdDb.getInstance(this), this)){

            Utils.toast(this, "newly detected roots added (deactivated)");
        }

        ArrayList<HiveRoot> roots;

        if(isActiveRootsSelected()){

            roots = NwdDb.getInstance(this).getActiveHiveRoots(this);

        }else{

            roots = NwdDb.getInstance(this).getDeactivatedHiveRoots(this);
        }

        for(HiveRoot root : roots) {

            HashMap<String, String> extraKeyToValue = new HashMap<>();

            extraKeyToValue.put(EXTRA_HIVE_ROOT_ID,
                    Integer.toString(root.getHiveRootId()));

            extraKeyToValue.put(EXTRA_HIVE_ROOT_NAME, root.getHiveRootName());

            mHiveRoots.add(root);
            addNavigateActivityCommand(root.getHiveRootName(), extraKeyToValue, HiveLobesActivity.class);
        }

        setupSpinnerListener();
    }

    private boolean isActiveRootsSelected() {

        Spinner spSelectedStatus = (Spinner)findViewById(R.id.spActiveRootsSelector);

        Object selected = spSelectedStatus.getSelectedItem();

        if(selected != null) {

            return spSelectedStatus
                    .getSelectedItem()
                    .toString()
                    .equalsIgnoreCase("Active");
        }

        return false;
    }

    private void setupSpinnerListener() {

        final Spinner spStatus =
                (Spinner) findViewById(R.id.spActiveRootsSelector);

        spStatus.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                refreshLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    private void populateActiveRootsSelectorSpinner() {

        Spinner spActiveRootsSelector =
                (Spinner)this.findViewById(R.id.spActiveRootsSelector);

        ArrayList<String> lst = new ArrayList<>();
        lst.add("Active");
        lst.add("Deactivated");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, lst);

        spActiveRootsSelector.setAdapter(adapter);
    }

    @Override
    protected ListView getListView() {

        return (ListView)findViewById(R.id.lvItems);
    }

    @Override
    protected void setupListViewListener(ListView lvItems) {

        lvItems.setAdapter(
                new ArrayAdapter<>(
                        this, android.R.layout.simple_list_item_1, mCommands));

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id) {

                NavigateActivityCommand cmd = mCommands.get(position);
                cmd.navigate();
            }
        });
    }

    private void addNavigateActivityCommand(
            String text,
            HashMap<String, String> extraKeyToValue,
            Class activity){

        mCommands.add(new NavigateActivityCommand(text, extraKeyToValue, activity, this));
    }

}
