package com.nineworldsdeep.gauntlet.hive;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.ListBaseActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static com.nineworldsdeep.gauntlet.hive.HiveRootsActivity.EXTRA_HIVE_ROOT_KEY;

public class HiveLobesActivity extends ListBaseActivity {

    private static final int MENU_CONTEXT_MOVE_ALL_FROM_STAGING = 1;
    private static final int MENU_CONTEXT_COPY_ALL_FROM_STAGING = 2;
    private static final int MENU_CONTEXT_INTAKE_ALL = 3;


    private ArrayList<NavigateActivityCommand> mCommands = new ArrayList<>();
    private HashMap<NavigateActivityCommand, HiveLobe> mCommandsToLobes =
            new HashMap<>();

    private HiveRoot mHiveRoot;

//    public static final String EXTRA_HIVE_LOBE_TYPE =
//            "com.nineworldsdeep.gauntlet.EXTRA_HIVE_LOBE_TYPE";

    public static final String EXTRA_HIVE_LOBE_KEY =
            "com.nineworldsdeep.gauntlet.EXTRA_HIVE_LOBE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hive_lobes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavigateActivityCommand.navigateTo(
                        HiveRootsActivity.class,
                        HiveLobesActivity.this);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lobes");

        String hiveRootKey = getIntent().getStringExtra(
                EXTRA_HIVE_ROOT_KEY
        );

        if(!Utils.stringIsNullOrWhitespace(hiveRootKey)
                && HiveRegistry.hasRootRegistered(hiveRootKey)) {

            mHiveRoot = HiveRegistry.getHiveRoot(hiveRootKey);
            TextView tv = (TextView)findViewById(R.id.tvHierarchyAddress);
            tv.setText(hiveRootKey);

        }else{

            Utils.toast(this, "hive root not registered");
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        NavigateActivityCommand cmd = mCommands.get(info.position);
        HiveLobe lobe = mCommandsToLobes.get(cmd);

        menu.setHeaderTitle(lobe.getHiveLobeName());

        if(!ConfigHive.isStagingRoot(lobe.getHiveRoot())) {

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_ALL_FROM_STAGING,
                    Menu.NONE, "Move All From Staging");

            menu.add(Menu.NONE, MENU_CONTEXT_COPY_ALL_FROM_STAGING,
                    Menu.NONE, "Copy All From Staging");

            menu.add(Menu.NONE, MENU_CONTEXT_INTAKE_ALL,
                    Menu.NONE, "Intake All");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case MENU_CONTEXT_MOVE_ALL_FROM_STAGING:

                moveAllFromStaging(info.position);
                return true;

            case MENU_CONTEXT_COPY_ALL_FROM_STAGING:

                copyAllFromStaging(info.position);
                return true;

            case MENU_CONTEXT_INTAKE_ALL:

                intakeAll(info.position);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void intakeAll(int position) {

        NavigateActivityCommand navCmd = mCommands.get(position);
        HiveLobe lobe = mCommandsToLobes.get(navCmd);

        try {

            UtilsHive.intake(this, lobe.siftFiles(mHiveRoot));
            Utils.toast(this, "intake successful.");
            refreshLayout();

        } catch (Exception e) {

            Utils.toast(this, "intake error: " + e.getMessage());
        }
    }

    private void copyAllFromStaging(int position) {

        NavigateActivityCommand navCmd = mCommands.get(position);
        HiveLobe lobe = mCommandsToLobes.get(navCmd);

        UtilsHive.copyAllFromStagingTo(this, lobe);
        refreshLayout();
    }

    private void moveAllFromStaging(int position) {

        NavigateActivityCommand navCmd = mCommands.get(position);
        HiveLobe lobe = mCommandsToLobes.get(navCmd);

        UtilsHive.moveAllFromStagingTo(this, lobe);
        refreshLayout();
    }

    @Override
    public void onBackPressed(){

        NavigateActivityCommand.navigateTo(
                HiveRootsActivity.class,
                HiveLobesActivity.this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshLayout();
    }

    @Override
    protected void readItems(ListView lv) {

        mCommands.clear();
        mCommandsToLobes.clear();

        UtilsHive.refreshLobes(mHiveRoot);

        for(HiveLobe hl : mHiveRoot.getLobes()){

            HiveRegistry.registerLobe(hl);

            HashMap<String, String> extraKeyToValue = new HashMap<>();

            String lobeKey = HiveRegistry.getLobeKey(hl);

            extraKeyToValue.put(EXTRA_HIVE_LOBE_KEY, lobeKey);

            File associatedDirectory = hl.getAssociatedDirectory();

            int count;
            try{

                count = associatedDirectory.listFiles().length;

            }catch(NullPointerException ex){

                count = 0;
            }
            String displayValue = hl.getHiveLobeName() + " (" +
                    count + ")";

            addNavigateActivityCommand(hl, displayValue, extraKeyToValue, HiveSporesActivity.class);
        }

    }

    private void addNavigateActivityCommand(
            HiveLobe lobe,
            String text,
            HashMap<String, String> extraKeyToValue,
            Class activity){

        NavigateActivityCommand navCmd =
                new NavigateActivityCommand(text, extraKeyToValue, activity, this);
        mCommands.add(navCmd);
        mCommandsToLobes.put(navCmd, lobe);
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


}


