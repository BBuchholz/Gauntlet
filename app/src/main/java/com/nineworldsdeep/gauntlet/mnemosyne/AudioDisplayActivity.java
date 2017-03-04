package com.nineworldsdeep.gauntlet.mnemosyne;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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
import android.widget.SeekBar;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Tags;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.sqlite.DisplayNameDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyV3MainActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class AudioDisplayActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    // TODO: I would like to go through this tutorial and try to implement a better player from it
    // http://www.androidhive.info/2012/03/android-building-audio-player-tutorial/
    // has features like seekbar that I am not currently implementing but would like to eventually

    private AudioMediaEntry ame;
    private MediaPlayerSingleton mMediaPlayerSingleton;

    //private NwdDb db;

    public static final String EXTRA_AUDIOPATH =
            "com.nineworldsdeep.gauntlet.AUDIODISPLAY_AUDIO_PATH";


    @Override
    protected void onResume() {

        super.onResume();

        //trying this to fix a crash after idle issue
        if(mMediaPlayerSingleton == null){

            SeekBar seek = (SeekBar) findViewById(R.id.seekBar);
            mMediaPlayerSingleton = MediaPlayerSingleton.getInstance(seek);
        }

        if(mMediaPlayerSingleton != null){
            mMediaPlayerSingleton.updateSeek();
        }

        NwdDb.getInstance(this).open();

        refreshLayout();
    }

    @Override
    protected void onPause() {

        super.onPause();

        if(mMediaPlayerSingleton != null){
            mMediaPlayerSingleton.stopSeekUpdate();
        }

        NwdDb.getInstance(this).close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String audioPath = i.getStringExtra(EXTRA_AUDIOPATH);

        SeekBar seek = (SeekBar) findViewById(R.id.seekBar);
        mMediaPlayerSingleton = MediaPlayerSingleton.getInstance(seek);

        if(audioPath != null){
//
//            SeekBar seek = (SeekBar) findViewById(R.id.seekBar);
//            mMediaPlayerSingleton = MediaPlayerSingleton.getInstance(seek);

            try {

                HashMap<String,String> pathToTagString =
                        TagDbIndex.importExportPathToTagStringMap(NwdDb.getInstance(this));

                setNowPlaying(mMediaPlayerSingleton.queueAndPlayLast(
                        pathToTagString,
                        audioPath,
                        this));

//                setNowPlaying(mMediaPlayerSingleton.queueAndPlayLast(
//                        NwdDb.getInstance(this),
//                        audioPath,
//                        this));

            } catch (IOException e) {

                Utils.toast(this, e.getMessage());
            }

        }else{

            Utils.toast(this, "audio path null");
        }
//
//        refreshLayout();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_set_display_name:

                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.prompt, null);

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(this);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput1 = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                String currentValue = "";

                //don't prepopulate if it's just returning the file name
                if(!ame.getDisplayName()
                        .equalsIgnoreCase(ame.getFile().getName())){

                    currentValue = ame.getDisplayName();
                }

                if(!Utils.stringIsNullOrWhitespace(currentValue)){
                    userInput1.setText(currentValue);
                }

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        try {

                                            DisplayNameDbIndex
                                                    .setDisplayNameAndExportFile(
                                                            ame.getPath(),
                                                            userInput1.getText()
                                                                    .toString(),
                                                            NwdDb.getInstance(AudioDisplayActivity.this));

                                            //DisplayNameIndex.getInstance().sync();
                                            updateMediaInfo();

                                        } catch (Exception e) {

                                            Utils.toast(AudioDisplayActivity.this,
                                                    "error setting display name: " +
                                            e.getMessage());
                                        }
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

            case R.id.action_set_tag_string:

                li = LayoutInflater.from(this);
                promptsView = li.inflate(R.layout.prompt, null);

                alertDialogBuilder =
                        new AlertDialog.Builder(this);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput2 = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                currentValue = ame.getTags();

                if(!Utils.stringIsNullOrWhitespace(currentValue)){
                    userInput2.setText(currentValue);
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
    //                                    TagIndex.getInstance().sync();
                                        try {

                                            NwdDb db = NwdDb.getInstance(AudioDisplayActivity.this);

                                            ame.setAndSaveTagString(
                                                    userInput2.getText().toString(),
                                                    db);

                                            TagDbIndex.getMergedPathToTagStringMap(false, true, db);

                                            updateMediaInfo();

                                        } catch (Exception e) {

                                            Utils.toast(AudioDisplayActivity.this,
                                                    "error setting tag string: " +
                                            e.getMessage());
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                return true;

            case R.id.action_reset_player:

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

                String msg = "Reset Player? (Playlist will be emptied)";

                builder.setTitle("Reset Player")
                        .setMessage(msg)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                try{

                                    mMediaPlayerSingleton.resetPlayer();
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

            case R.id.action_go_to_home_screen:

                NavigateActivityCommand.navigateTo(
                        HomeListActivity.class, this
                );

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
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

        mMediaPlayerSingleton.stop();
    }

    public void playPrevious(View view) {

        try{

            setNowPlaying(mMediaPlayerSingleton.playPrevious(this));
            updateMediaInfo();

        }catch(Exception ex){

            Utils.toast(this, ex.getMessage());
        }
    }

    public void playNext(View view) {

        try{

            setNowPlaying(mMediaPlayerSingleton.playNext(this));
            updateMediaInfo();

        }catch(Exception ex){

            Utils.toast(this, ex.getMessage());
        }
    }

    @Override
    public void onPrepared(MediaPlayer player) {

        SeekBar seek = (SeekBar) findViewById(R.id.seekBar);
        seek.setMax(player.getDuration());
        mMediaPlayerSingleton.updateSeek();
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

                try {

                    AsyncTagSave ats = new AsyncTagSave();
                    ats.execute(tag);

//                    ame.setAndSaveTagString(
//                            Tags.toggleTag(tag, ame.getTags()),
//                            NwdDb.getInstance(AudioDisplayActivity.this));
//                    updateMediaInfo();

                } catch (Exception e) {

                    Utils.toast(AudioDisplayActivity.this,
                            "error setting tag string: " +
                    e.getMessage());
                }
            }
        });
    }

    private class AsyncTagSave extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {

            String result;

            try{

                //ListView lvItems = getListView();
                String tag = params[0];

                long start = System.nanoTime();

                NwdDb db = NwdDb.getInstance(AudioDisplayActivity.this);

                ame.setAndSaveTagString(
                            Tags.toggleTag(tag, ame.getTags()),
                            db);

                TagDbIndex.getMergedPathToTagStringMap(false, true, db);

                long elapsedTime = System.nanoTime() - start;
                long milliseconds = elapsedTime / 1000000;

                String elapsedTimeStr = Long.toString(milliseconds);

                result = "finished updating tags: " + elapsedTimeStr + "ms";

            }catch (Exception e){

                result = e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            Utils.toast(AudioDisplayActivity.this, result);

            updateMediaInfo();
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }

    }

    private void loadItems() {

        ListView lvItems = (ListView) findViewById(R.id.lvPlaylist);
        ListView lvTags = (ListView) findViewById(R.id.lvTagsFrequent);

        List<AudioMediaEntry> playlistEntries = mMediaPlayerSingleton.getPlaylist();
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
