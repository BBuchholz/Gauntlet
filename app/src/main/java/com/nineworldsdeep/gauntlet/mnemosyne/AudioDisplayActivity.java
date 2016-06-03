package com.nineworldsdeep.gauntlet.mnemosyne;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.nineworldsdeep.gauntlet.Tags;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.tapestry.ConfigFile;
import com.nineworldsdeep.gauntlet.tapestry.TapestryUtils;

import java.io.IOException;
import java.util.List;

public class AudioDisplayActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    // TODO: I would like to go through this tutorial and try to implement a better player from it
    // TODO: LICENSE NOTES/RESEARCH
    // http://www.androidhive.info/2012/03/android-building-audio-player-tutorial/
    // has features like seekbar that I am not currently implementing but would like to eventually

    private AudioMediaEntry ame;
    private MediaPlayerSingleton mps;

    public static final String EXTRA_AUDIOPATH =
            "com.nineworldsdeep.gauntlet.AUDIODISPLAY_AUDIO_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i = getIntent();
        String audioPath = i.getStringExtra(EXTRA_AUDIOPATH);

        if(audioPath != null){

            //ame = new FileListItem(s);

            //Utils.toast(this, ame.toString());
            Utils.toast(this, audioPath);

            //mp = MediaPlayer.create(this, Uri.parse(ame.getFile().getPath()));
            //mp.start();

            mps = MediaPlayerSingleton.getInstance();

            try {
                setNowPlaying(mps.queueAndPlayLast(audioPath, this));

            } catch (IOException e) {

                Utils.toast(this, e.getMessage());
            }

        }else{

            Utils.toast(this, "audio path null");
        }

        refreshLayout();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_set_display_name) {

            //Adapted from: TODO: LICENSE NOTES/RESEARCH
    // http://www.mkyong.com/android/android-prompt-user-input-dialog-example/
            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.prompt, null);

            AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            String currentValue = "";

            //don't prepopulate if it's just returning the file name
            if(!ame.getDisplayName()
                    .equalsIgnoreCase(ame.getFile().getName())){

                currentValue = ame.getDisplayName();
            }

            if(!Utils.stringIsNullOrWhitespace(currentValue)){
                userInput.setText(currentValue);
            }

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text
                                    ame.setDisplayName(userInput.getText().toString());
                                    DisplayNameIndex.getInstance().save();
                                    updateMediaInfo();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();


            return true;

        } else if (id == R.id.action_set_tag_string) {

            //Adapted from: TODO: LICENSE NOTES/RESEARCH
    // http://www.mkyong.com/android/android-prompt-user-input-dialog-example/
            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.prompt, null);

            AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            String currentValue = ame.getTags();

            if(!Utils.stringIsNullOrWhitespace(currentValue)){
                userInput.setText(currentValue);
            }

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    // get user input and set it to result
                                    // edit text

//                                    ili.setTagString(userInput.getItem().toString());
//                                    TagIndex.getInstance().save();
                                    ame.setAndSaveTagString(userInput.getText().toString());
                                    updateMediaInfo();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

            return true;

        } else if(id == R.id.action_seed){

            String currentDevice = TapestryUtils.getCurrentDeviceName();

            if(currentDevice == null) {
                //prompt for one
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.prompt, null);

                TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
                tv.setText("No Device Set, Enter Device Name, Then Try Again: ");

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(this);

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

                                        ConfigFile f = new ConfigFile();
                                        f.setDeviceName(name);
                                        f.save();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                Utils.toast(this, "seed discarded");

            }else{

                String currentGardenName = TapestryUtils.getCurrentGardenName(currentDevice);

                TapestryUtils
                        .linkNodeToAudioPath(currentGardenName,
                                ame.getPath());

                Utils.toast(this, "seed planted: " + currentGardenName);
            }

            return true;

        } else if(id == R.id.action_seed_new){

            String currentDevice = TapestryUtils.getCurrentDeviceName();

            if(currentDevice == null) {
                //prompt for one
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.prompt, null);

                TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
                tv.setText("No Device Set, Enter Device Name, Then Try Again: ");

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(this);

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

                                        ConfigFile f = new ConfigFile();
                                        f.setDeviceName(name);
                                        f.save();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                Utils.toast(this, "seed discarded");

            }else{

                String currentGardenName = TapestryUtils.getNewGardenName(currentDevice);

                TapestryUtils
                        .linkNodeToAudioPath(currentGardenName,
                                             ame.getPath());

                Utils.toast(this, "seed planted: " + currentGardenName);
            }

            return true;

        } else if (id == R.id.action_link_to_node){

            LayoutInflater li = LayoutInflater.from(AudioDisplayActivity.this);
            View promptsView = li.inflate(R.layout.prompt, null);

            TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
            tv.setText("Enter Node Name: ");

            android.app.AlertDialog.Builder alertDialogBuilder =
                    new android.app.AlertDialog.Builder(AudioDisplayActivity.this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    // get list name from userInput and move
                                    String processedName =
                                            TapestryUtils.processNodeName(
                                                    userInput.getText().toString());

                                    TapestryUtils
                                            .linkNodeToAudioPath(processedName,
                                                                 ame.getPath());

                                    Utils.toast(AudioDisplayActivity.this, "linked");

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

            return true;

        } else if(id == R.id.action_reset_player){

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

            String msg = "Reset Player? (Playlist will be emptied)";

            builder.setTitle("Reset Player")
                    .setMessage(msg)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            try{

                                mps.resetPlayer();
                                setNowPlaying(null);
                                refreshLayout();

                            }catch(Exception ex){

                                Utils.toast(getApplicationContext(),
                                        "error resetting player: " +
                                                ex.getMessage());
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void setNowPlaying(AudioMediaEntry ame){

        this.ame = ame;

        updateMediaInfo();
    }

    private void updateMediaInfo(){

        String defaultName = "";
        String tags = "";

        if(this.ame != null){

            defaultName = this.ame.getDefaultName();

            //String tagsTemp = this.ame.getTags();

            //prevent duplicates if display name not set
//            if(!defaultName.equalsIgnoreCase(tagsTemp)){
//
//                tags = tagsTemp;
//            }

            tags = ame.getTags();
        }

        //display info here
        TextView tvHeader = (TextView)findViewById(R.id.tvHeader);
        TextView tvTagLine= (TextView)findViewById(R.id.tvTagline);

        tvHeader.setText(defaultName);
        tvTagLine.setText(tags);
    }

    public void stopPlayback(View view) {

        mps.stop();
    }

    public void playPrevious(View view) {

        try{

            setNowPlaying(mps.playPrevious(this));
            updateMediaInfo();

        }catch(Exception ex){

            Utils.toast(this, ex.getMessage());
        }
    }

    public void playNext(View view) {

        try{

            setNowPlaying(mps.playNext(this));
            updateMediaInfo();

        }catch(Exception ex){

            Utils.toast(this, ex.getMessage());
        }
    }

    @Override
    public void onPrepared(MediaPlayer player) {

        player.start();
    }

    private void refreshLayout(){

        ListView lvPlaylist =
                (ListView) findViewById(R.id.lvPlaylist);

        loadItems();
        setupListViewListener();
        registerForContextMenu(lvPlaylist);
    }

    private void setupListViewListener() {

        final ListView lvTags = (ListView) findViewById(R.id.lvTagsFrequent);

        lvTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tag = (String) lvTags.getItemAtPosition(position);
                ame.setAndSaveTagString(Tags.toggleTag(tag, ame.getTags()));
                updateMediaInfo();
            }
        });
    }

    private void loadItems() {

        ListView lvItems = (ListView) findViewById(R.id.lvPlaylist);
        ListView lvTags = (ListView) findViewById(R.id.lvTagsFrequent);

        List<AudioMediaEntry> playlistEntries = mps.getPlaylist();
        List<String> frequentTags = Tags.getFrequent();

        lvItems.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        playlistEntries)
        );

        lvTags.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        frequentTags)
        );
    }
}
