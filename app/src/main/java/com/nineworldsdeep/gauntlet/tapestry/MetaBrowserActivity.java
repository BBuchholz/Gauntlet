package com.nineworldsdeep.gauntlet.tapestry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyUtils;

import java.io.File;
import java.util.ArrayList;

public class MetaBrowserActivity extends AppCompatActivity {

    private static final int MENU_EXPORT_DB = 1;

    private String mCurrentNodeName = null;
    private ArrayList<MetaEntry> mMeta;

    private NwdDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateDescriptionSpinner();

        assignDb();

        Intent i = getIntent();
        mCurrentNodeName = i.getStringExtra(
                TapestryNodeActivity.EXTRA_CURRENT_NODE_NAME);

        if(Utils.stringIsNullOrWhitespace(mCurrentNodeName)){

            Utils.toast(this, "Node Name Not Specified");

        } else {

            setTitle(mCurrentNodeName);
        }

        refreshLayout();
    }

    @Override
    protected void onResume() {

        super.onResume();

        assignDb();

        //moved to assignDb() //db.open();
    }

    private void assignDb(){

        if(db == null || db.needsTestModeRefresh()){

            if(Configuration.isInTestMode()){

                //use external db in folder NWD/sqlite
                db = new NwdDb(this, "test");

            }else {

                //use internal app db
                db = new NwdDb(this);
            }
        }

        db.open();
    }

    @Override
    protected void onPause() {

        db.close();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_meta_browser, menu);

        MenuItem menuItem =
                menu.add(Menu.NONE,
                         MENU_EXPORT_DB,
                         Menu.NONE,
                         "Export DB");

        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //keep static ids up here, and those defined
        //as resources at the bottom. I don't think
        //they will conflict, but in case they ever
        //were to, this will guarantee that our
        //custom ids take precedence
        if (id == MENU_EXPORT_DB){

            db.export(this);
            return true;

        } else if (id == R.id.action_test) {

            Configuration.setTestMode(true);
            assignDb();

            //all transactions should be idempotent
            //so we needn't comment any out here
            //and since we are using the test db
            //we can just run everything in our
            //data access object to test it


            String filePath =
                Configuration.getSandboxFile(
                        "testDisplayNameLink",
                        "displayNameTest").getAbsolutePath();

            displayNameTestingCode(filePath);
            hashTestingCode(filePath);
            tagTestingCode(filePath);
            devicePathTestingCode(filePath);
            audioTranscriptTestingCode(filePath);
            localDeviceConfigTestingCode();

            //nextTestingCodeGoesHere();

            String dbType = db.isInternalDb() ? "internal" : "external";
            String dbName = db.getDatabaseName();

            Utils.toast(this, "tested in " + dbType + " database: " + dbName);

            Configuration.setTestMode(false);
            assignDb();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void localDeviceConfigTestingCode() {


    }

    private void audioTranscriptTestingCode(String filePath) {


    }

    private void devicePathTestingCode(String filePath) {


    }

    private void tagTestingCode(String filePath) {


    }

    private void hashTestingCode(String filePath) {

        String sha1EmptyFileHash = "da39a3ee5e6b4b0d3255bfef95601890afd80709";

        String hashedAt = SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss();

        db.linkHashToFile(TapestryUtils.getCurrentDeviceName(),
                          filePath,
                          sha1EmptyFileHash,
                          hashedAt);
    }

    private void displayNameTestingCode(String filePath) {

        db.linkDisplayNameToFile(TapestryUtils.getCurrentDeviceName(),
                                 filePath,
                                 "Test Display Name");
    }


    private void populateDescriptionSpinner() {

        Spinner spDescription = (Spinner)this.findViewById(R.id.spDescription);

        ArrayList<String> lst = new ArrayList<>();
        lst.add("tags");
        lst.add("path");
        lst.add("hash");
        lst.add("hashedAt");
        lst.add("device");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, lst);

        spDescription.setAdapter(adapter);
    }

    private void refreshLayout() {

        ListView lvItems =
                (ListView) findViewById(R.id.lvItems);

        loadItems();
        setupSpinnerListener();
        setupListViewListener();
        registerForContextMenu(lvItems);
    }

    private void setupSpinnerListener() {

        final Spinner spDescription =
                (Spinner) findViewById(R.id.spDescription);

        spDescription.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                MetaEntry.setDescriptionKey(
                        spDescription.getSelectedItem().toString());

                refreshLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    private void setupListViewListener() {

        ListView lvItems = (ListView)findViewById(R.id.lvItems);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MetaEntry me = (MetaEntry)mMeta.get(position);

                startActivity(me.getIntent(view.getContext()));
            }
        });
    }

    private void loadItems() {

        ListView lvItems = (ListView) findViewById(R.id.lvItems);

        mMeta = TapestryUtils.getMetaEntries(mCurrentNodeName);

        SimpleAdapter saMeta =
                new SimpleAdapter(
                        this.getBaseContext(),
                        mMeta,
                        MetaEntry.getLayout(),
                        MetaEntry.getMapKeysForView(),
                        MetaEntry.getIdsForViewElements());

        lvItems.setAdapter(saMeta);
    }

}
