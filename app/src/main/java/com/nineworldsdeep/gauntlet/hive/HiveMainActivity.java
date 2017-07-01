package com.nineworldsdeep.gauntlet.hive;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IRefreshableUI;
import com.nineworldsdeep.gauntlet.core.ListBaseActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.core.Prompt;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;
import java.util.HashMap;

public class HiveMainActivity extends ListBaseActivity implements IRefreshableUI {

    private ArrayList<NavigateActivityCommand> cmds = new ArrayList<>();
    public static final String EXTRA_HIVE_ROOT_ID =
            "com.nineworldsdeep.gauntlet.EXTRA_HIVE_ROOT_ID";
    public static final String EXTRA_HIVE_ROOT_NAME =
            "com.nineworldsdeep.gauntlet.EXTRA_HIVE_ROOT_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hive_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Added Hive Root (in progress)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //check local device name is set
        NwdDb.getInstance(this).open();
        if(Configuration.getLocalMediaDevice(this,
                        NwdDb.getInstance(this)) == null){

            Prompt.promptSetLocalDeviceDescription(this, this);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshLayout();
    }

    @Override
    protected void readItems(ListView lv) {

        cmds.clear();

        int rootId = 1;
        String rootName = "test-root";

        //demo
        HashMap<String, String> extraKeyToValue = new HashMap<>();
        extraKeyToValue.put(EXTRA_HIVE_ROOT_ID, Integer.toString(rootId));
        extraKeyToValue.put(EXTRA_HIVE_ROOT_NAME, rootName);

        addNavigateActivityCommand(rootName, extraKeyToValue, HiveRootActivity.class);
    }

    @Override
    protected ListView getListView() {

        return (ListView)findViewById(R.id.lvItems);
    }

    @Override
    protected void setupListViewListener(ListView lvItems) {

        lvItems.setAdapter(
                new ArrayAdapter<>(
                        this, android.R.layout.simple_list_item_1, cmds));

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id) {

                NavigateActivityCommand cmd = cmds.get(position);
                cmd.navigate();
            }
        });
    }

    private void addNavigateActivityCommand(
            String text,
            HashMap<String, String> extraKeyToValue,
            Class activity){

        cmds.add(new NavigateActivityCommand(text, extraKeyToValue, activity, this));
    }

}
