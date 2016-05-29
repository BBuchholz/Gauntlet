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

        //TODO: we can remove this once db is up and running
        //I'm having issues with sqlite not found on my test device
        //this is a workaround that lets me access db structure
        //through the external storage
        if(Configuration.isInTestMode()){

            //use external db in folder NWD/sqlite
            db = new NwdDb(this, "test");

        }else {

            //use internal app db
            db = new NwdDb(this);
        }

        db.open();
        super.onResume();
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

            linkDisplayNameTestingCode();

            //nextTestingCodeGoesHere();
            //comment out others, retain for reference

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void linkDisplayNameTestingCode() {

        File filePath =
                Configuration.getSandboxFile(
                        "testDisplayNameLink",
                        "displayNameTest");

        db.linkDisplayNameToFile("Test Device",
                filePath.getAbsolutePath(),
                "Test Display Name");

        String dbType = db.isInternalDb() ? "internal" : "external";
        String dbName = db.getDatabaseName();

        Utils.toast(this, "linked in " + dbType + " database: " + dbName);
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
