package com.nineworldsdeep.gauntlet.core;

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

import com.nineworldsdeep.gauntlet.MainActivity;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.TestModeActivity;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.bookSegments.AliasListActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.AudioListV2Activity;
import com.nineworldsdeep.gauntlet.mnemosyne.ImageListV2Activity;
import com.nineworldsdeep.gauntlet.mnemosyne.PdfListActivity;
import com.nineworldsdeep.gauntlet.muse.MuseMainActivity;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyV3MainActivity;
import com.nineworldsdeep.gauntlet.tapestry.ConfigFile;
import com.nineworldsdeep.gauntlet.tapestry.TapestryNodeActivity;
import com.nineworldsdeep.gauntlet.tapestry.TapestryUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HomeListActivity extends AppCompatActivity {

    private List<NavigateActivityCommand> cmds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //refreshLayout();

        //prompt for device name if not set
        checkAndPromptForDeviceName();
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshLayout();
    }

    private void refreshLayout(){

        cmds = new ArrayList<>();

        //to prototype new features in test mode...

        if(Configuration.isInTestMode()) {

            //...add here while still in proto-phase, then...



        }

        //...move to here when it's ready to go live :)

        addNavigateActivityCommand("Synergy V3", SynergyV3MainActivity.class);
        addNavigateActivityCommand("Muse", MuseMainActivity.class);
        addNavigateActivityCommand("Images", ImageListV2Activity.class);
        addNavigateActivityCommand("Audio", AudioListV2Activity.class);
        addNavigateActivityCommand("PDFs", PdfListActivity.class);
        addNavigateActivityCommand("Sources", AliasListActivity.class);
        addNavigateActivityCommand("Tapestry", TapestryNodeActivity.class);
        addNavigateActivityCommand("Test", TestModeActivity.class);
        addNavigateActivityCommand("PrevHome", MainActivity.class);

        ListView lvItems = (ListView)findViewById(R.id.lvItems);

        lvItems.setAdapter(
                new ArrayAdapter<NavigateActivityCommand>(
                        this, android.R.layout.simple_list_item_1, cmds));

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NavigateActivityCommand cmd = cmds.get(position);
                cmd.navigate();
            }
        });
    }

    private void addNavigateActivityCommand(String text, Class activity){

        cmds.add(new NavigateActivityCommand(text, activity, this));
    }

    private void checkAndPromptForDeviceName(){

        if(TapestryUtils.getCurrentDeviceName() == null){

            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.prompt, null);

            TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
            tv.setText("No Device Set, Enter Device Name: ");

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

                                    if(!Utils.stringIsNullOrWhitespace(name)){

                                        ConfigFile f = new ConfigFile();
                                        f.setDeviceName(name);
                                        f.save();

                                        Utils.toast(HomeListActivity.this,
                                                "device name stored");
                                    }else{

                                        Utils.toast(HomeListActivity.this,
                                                "invalid device name");
                                    }
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {

                                    Utils.toast(HomeListActivity.this, "cancelled");

                                    dialog.cancel();
                                }
                            });

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.show();
        }
    }

}
