package com.nineworldsdeep.gauntlet.core;

import android.content.DialogInterface;
import android.os.Bundle;
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
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistActivity;
import com.nineworldsdeep.gauntlet.hive.experimental.HiveRootListActivity;
import com.nineworldsdeep.gauntlet.hive.HiveRootsActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.ImageGridActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.PdfListActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.TransferActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.playerService.MediaPlayerServiceActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.AudioDisplayV5Activity;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.ImageGridV5Activity;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.ImageListV5Activity;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MnemosyneV5ScanActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.TagBrowserV5Activity;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.demo.ImageBrowserActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.AudioListV5Activity;
import com.nineworldsdeep.gauntlet.muse.MuseMainActivity;
import com.nineworldsdeep.gauntlet.synergy.SynergyDrawerActivity;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5MainActivity;
import com.nineworldsdeep.gauntlet.tapestry.v1.ConfigFile;
import com.nineworldsdeep.gauntlet.tapestry.v1.TapestryUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeListActivity extends ListBaseActivity {

    private List<NavigateActivityCommand> cmds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //prompt for device name if not set
        checkAndPromptForDeviceName();
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshLayout();
    }

    @Override
    protected void readItems(ListView lv) {

        cmds.clear();

        addNavigateActivityCommand("Synergy V5", SynergyV5MainActivity.class);
        addNavigateActivityCommand("Images V5", ImageListV5Activity.class);
        addNavigateActivityCommand("Audio V5", AudioListV5Activity.class);
        addNavigateActivityCommand("Audio Player V5", AudioDisplayV5Activity.class);
        addNavigateActivityCommand("PDFs", PdfListActivity.class);
        addNavigateActivityCommand("Transfers", TransferActivity.class);
        addNavigateActivityCommand("Hive", HiveRootsActivity.class);
        addNavigateActivityCommand("Archivist", ArchivistActivity.class);
        addNavigateActivityCommand("Tag Browser V5", TagBrowserV5Activity.class);
        addNavigateActivityCommand("Lab", LabListActivity.class);

//        addNavigateActivityCommand("Image Grid V5", ImageGridV5Activity.class);
//        addNavigateActivityCommand("MediaPlayerService", MediaPlayerServiceActivity.class);
//        addNavigateActivityCommand("Hive Hierarchy Exp.", HiveRootListActivity.class);
//        addNavigateActivityCommand("Synergy Drawer", SynergyDrawerActivity.class);
//        addNavigateActivityCommand("Image Grid", ImageGridActivity.class);
//        addNavigateActivityCommand("Muse", MuseMainActivity.class);
//        addNavigateActivityCommand("Image Browser", ImageBrowserActivity.class);

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

    private void addNavigateActivityCommand(String commandText, Class activity){

        cmds.add(new NavigateActivityCommand(commandText, activity, this));
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
