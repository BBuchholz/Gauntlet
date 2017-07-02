package com.nineworldsdeep.gauntlet.hive;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IRefreshableUI;
import com.nineworldsdeep.gauntlet.core.ListBaseActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.core.Prompt;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            public void onClick(final View view) {


                LayoutInflater li = LayoutInflater.from(HiveMainActivity.this);
                View promptsView = li.inflate(R.layout.prompt, null);

                TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
                tv.setText("Enter Hive Root Name: ");

                android.app.AlertDialog.Builder alertDialogBuilder =
                        new android.app.AlertDialog.Builder(HiveMainActivity.this);

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

                                        if(hiveRootNameIsValid(name)){

                                            NwdDb db = NwdDb.getInstance(HiveMainActivity.this);
                                            db.open();

                                            db.insertHiveRootName(name);

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

        //check local device name is set
        NwdDb.getInstance(this).open();
        if(Configuration.getLocalMediaDevice(this,
                        NwdDb.getInstance(this)) == null){

            Prompt.promptSetLocalDeviceDescription(this, this);
        }
    }

    private boolean hiveRootNameIsValid(String name) {

//        asdf; //do the TODO
//        //TODO: this needs to only allow alphanumeric and hyphens
//        return !Utils.stringIsNullOrWhitespace(name);

        Matcher match = Pattern.compile("^[0-9a-z-]*$")
            .matcher(name);

        return match.find();
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshLayout();
    }

    @Override
    protected void readItems(ListView lv) {

        cmds.clear();

        ArrayList<HiveRoot> roots = NwdDb.getInstance(this).getAllHiveRoots(this);

        for(HiveRoot root : roots) {

            HashMap<String, String> extraKeyToValue = new HashMap<>();

            extraKeyToValue.put(EXTRA_HIVE_ROOT_ID,
                    Integer.toString(root.getHiveRootId()));
            extraKeyToValue.put(EXTRA_HIVE_ROOT_NAME, root.getHiveRootName());

            addNavigateActivityCommand(root.getHiveRootName(), extraKeyToValue, HiveRootActivity.class);
        }
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
