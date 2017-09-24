package com.nineworldsdeep.gauntlet.hive;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.ListBaseActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;

import java.util.ArrayList;
import java.util.HashMap;

import static com.nineworldsdeep.gauntlet.hive.HiveRootsActivity.EXTRA_HIVE_ROOT_ID;
import static com.nineworldsdeep.gauntlet.hive.HiveRootsActivity.EXTRA_HIVE_ROOT_KEY;

public class HiveLobesActivity extends ListBaseActivity {

    private ArrayList<NavigateActivityCommand> cmds = new ArrayList<>();

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

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lobes");

        String hiveRootKey = getIntent().getStringExtra(
                EXTRA_HIVE_ROOT_KEY
        );

        if(!Utils.stringIsNullOrWhitespace(hiveRootKey)
                && HiveRegistry.hasRootRegistered(hiveRootKey)) {

            mHiveRoot = HiveRegistry.getHiveRoot(hiveRootKey);

        }else{

            Utils.toast(this, "hive root not registered");
        }

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

        cmds.clear();

//        for(HiveLobeType hiveLobeType : HiveLobeType.values()) {
//
//            HashMap<String, String> extraKeyToValue = new HashMap<>();
//
//            extraKeyToValue.put(EXTRA_HIVE_LOBE_KEY, this.mHiveRoot.getHiveRootName());
//            //extraKeyToValue.put(EXTRA_HIVE_LOBE_TYPE, hiveLobeType.toString());
//
//            addNavigateActivityCommand(hiveLobeType.toString(), extraKeyToValue, HiveSporesActivity.class);
//        }

        UtilsHive.refreshLobes(mHiveRoot);

        for(HiveLobe hl : mHiveRoot.getLobes()){

            HiveRegistry.registerLobe(hl);

            HashMap<String, String> extraKeyToValue = new HashMap<>();

            String lobeKey = HiveRegistry.getLobeKey(hl);

            extraKeyToValue.put(EXTRA_HIVE_LOBE_KEY, lobeKey);

            addNavigateActivityCommand(hl.getHiveLobeName(), extraKeyToValue, HiveSporesActivity.class);
        }

        String testing = "just need a breakpoint";
    }

    private void addNavigateActivityCommand(
            String text,
            HashMap<String, String> extraKeyToValue,
            Class activity){

        cmds.add(new NavigateActivityCommand(text, extraKeyToValue, activity, this));
    }

    @Override
    protected ListView getListView() {

        return (ListView)findViewById(R.id.lvItems);
    }

    @Override
    protected void setupListViewListener(ListView lvItems) {

        lvItems.setAdapter(
                new ArrayAdapter<>(
                        this, android.R.layout.simple_list_item_1, HiveLobeType.values()));

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id) {

                NavigateActivityCommand cmd = cmds.get(position);
                cmd.navigate();
            }
        });
    }

}


