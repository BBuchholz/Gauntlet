package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
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
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;

public class AudioDisplayV5Activity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    // TODO: I would like to go through this tutorial and try to implement a better player from it
    // http://www.androidhive.info/2012/03/android-building-audio-player-tutorial/
    // has features like seekbar that I am not currently implementing but would like to eventually

    private MediaListItem currentMediaListItem;
    private MediaPlayerSingletonV5 mMediaPlayerSingletonV5;

    //private NwdDb db;

    private static final int MENU_CONTEXT_REMOVE_ITEM = 1;

    public static final String EXTRA_AUDIO_PATH =
            "com.nineworldsdeep.gauntlet.AUDIO_DISPLAY_AUDIO_PATH";

    @Override
    protected void onResume() {

        super.onResume();

        //trying this to fix a crash after idle issue
        if(mMediaPlayerSingletonV5 == null){

            SeekBar seek = (SeekBar) findViewById(R.id.seekBar);
            mMediaPlayerSingletonV5 =
                    MediaPlayerSingletonV5.getInstance(seek, generateCompletionListener());
        }

        if(mMediaPlayerSingletonV5 != null){
            mMediaPlayerSingletonV5.updateSeek();
        }

        NwdDb.getInstance(this).open();

        refreshLayout();
    }

    protected MediaPlayer.OnCompletionListener generateCompletionListener(){

        MediaPlayer.OnCompletionListener listener =
                new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        try{

                            NwdDb db = NwdDb.getInstance(AudioDisplayV5Activity.this);

                            setNowPlaying(mMediaPlayerSingletonV5.playNext(AudioDisplayV5Activity.this), db);
                            updateMediaInfo();

                        }catch(Exception ex){

                            Utils.toast(AudioDisplayV5Activity.this, ex.getMessage());
                        }
                    }
                };

        return listener;
    }


    protected void onPause() {

        super.onPause();

        if(mMediaPlayerSingletonV5 != null){
            mMediaPlayerSingletonV5.stopSeekUpdate();
        }

        NwdDb.getInstance(this).close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_display_v5);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String audioPath = i.getStringExtra(EXTRA_AUDIO_PATH);

        SeekBar seek = (SeekBar) findViewById(R.id.seekBar);
        mMediaPlayerSingletonV5 = MediaPlayerSingletonV5.getInstance(seek, generateCompletionListener());

        if(audioPath != null){

            try {

                NwdDb db = NwdDb.getInstance(this);
                db.open();

                MediaListItem mli = new MediaListItem(audioPath);

                setNowPlaying(
                        mMediaPlayerSingletonV5.queueAndPlayLast(
                        mli,
                        this), db);

            } catch (Exception e) {

                Utils.toast(this, e.toString());
            }

        }else{

            //only for testing
            //Utils.toast(this, "audio path null");
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        String title =
                mMediaPlayerSingletonV5
                        .getMediaListItem(info.position)
                        .getFile()
                        .getName();

        menu.setHeaderTitle(title);

        menu.add(Menu.NONE, MENU_CONTEXT_REMOVE_ITEM, Menu.NONE, "Remove From Playlist");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case MENU_CONTEXT_REMOVE_ITEM:

                removeItem(info.position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void removeItem(int position) {

        mMediaPlayerSingletonV5.removeItem(position);

        if(position == mMediaPlayerSingletonV5.getCurrentPosition()){

            try {

                setNowPlaying(null, null);

            }catch(Exception ex){

                Utils.toast(getApplicationContext(),
                        "error resetting player: " +
                                ex.getMessage());
            }
        }

        refreshLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio_display_v5, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_set_tag_string:

                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.prompt, null);

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(this);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput2 = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                String currentValue = currentMediaListItem.getTags();

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

                                            NwdDb db = NwdDb.getInstance(AudioDisplayV5Activity.this);

                                            currentMediaListItem.setTagsFromTagString(
                                                    userInput2.getText().toString());

                                            db.sync(currentMediaListItem.getMedia());

                                            updateMediaInfo();

                                        } catch (Exception e) {

                                            Utils.toast(AudioDisplayV5Activity.this,
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
                AlertDialog alertDialog = alertDialogBuilder.create();

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

                                    mMediaPlayerSingletonV5.resetPlayer();
                                    setNowPlaying(null, null);
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

    private void setNowPlaying(MediaListItem mli, NwdDb db) throws Exception {

        currentMediaListItem = mli;

        if(currentMediaListItem != null) {

            currentMediaListItem.hashMedia();

            if (db != null) {

                db.sync(currentMediaListItem.getMedia());
            }
        }

        updateMediaInfo();
    }

    private void updateMediaInfo(){

        String defaultName = "";
        String tags = "";

        if(this.currentMediaListItem != null){

            defaultName = this.currentMediaListItem.getFile().getName();

            //String tagsTemp = this.currentMediaListItem.getTags();

            //prevent duplicates if display name not set
//            if(!defaultName.equalsIgnoreCase(tagsTemp)){
//
//                tags = tagsTemp;
//            }

            tags = currentMediaListItem.getTags();
        }

        //display info here
        TextView tvHeader = (TextView)findViewById(R.id.tvHeader);
        TextView tvTagLine= (TextView)findViewById(R.id.tvTagline);

        tvHeader.setText(defaultName);
        tvTagLine.setText(tags);
    }

    public void stopPlayback(View view) {

        mMediaPlayerSingletonV5.stop();
    }

    public void playPrevious(View view) {

        try{

            NwdDb db = NwdDb.getInstance(this);

            setNowPlaying(mMediaPlayerSingletonV5.playPrevious(this), db);
            updateMediaInfo();

        }catch(Exception ex){

            Utils.toast(this, ex.toString());
        }
    }

    public void playNext(View view) {

        try{

            NwdDb db = NwdDb.getInstance(this);

            setNowPlaying(mMediaPlayerSingletonV5.playNext(this), db);
            updateMediaInfo();

        }catch(Exception ex){

            Utils.toast(this, ex.getMessage());
        }
    }

    public void play(View view){

        try{

            mMediaPlayerSingletonV5.play(this);

        }catch(Exception ex){

            Utils.toast(this, ex.getMessage());
        }
    }

    @Override
    public void onPrepared(MediaPlayer player) {

        SeekBar seek = (SeekBar) findViewById(R.id.seekBar);
        seek.setMax(player.getDuration());
        mMediaPlayerSingletonV5.updateSeek();
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

                    //asdf; //tag toggle not working
                    AsyncTagSave ats = new AsyncTagSave();
                    ats.execute(tag);

//                    currentMediaListItem.setAndSaveTagString(
//                            Tags.toggleTag(tag, currentMediaListItem.getTags()),
//                            NwdDb.getInstance(AudioDisplayActivity.this));
//                    updateMediaInfo();

                } catch (Exception e) {

                    Utils.toast(AudioDisplayV5Activity.this,
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

                NwdDb db = NwdDb.getInstance(AudioDisplayV5Activity.this);

//                currentMediaListItem.setAndSaveTagString(
//                            Tags.toggleTag(tag, currentMediaListItem.getTags()),
//                            db);
//
//                asdf;
//                //if hasTag(tag), untag, else, tag

                if(currentMediaListItem.hasTag(tag)){

                    //toggle tag
                    if(currentMediaListItem.isTagged(tag)) {

                        currentMediaListItem.untag(tag);

                    }else{

                        currentMediaListItem.tag(tag);
                    }

                }else{

                    currentMediaListItem.tag(tag);
                }

                db.sync(currentMediaListItem.getMedia());

                long elapsedTime = System.nanoTime() - start;
                long milliseconds = elapsedTime / 1000000;

                String elapsedTimeStr = Long.toString(milliseconds);

                result = "finished updating tags: " + elapsedTimeStr + "ms";

            }catch (Exception e){

                result = "Error updating tags: " + e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            Utils.toast(AudioDisplayV5Activity.this, result);

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

        ArrayList<MediaListItem> playlistEntries = mMediaPlayerSingletonV5.getPlaylist();
        ArrayList<String> frequentTags = Tags.getFrequentV5(this);

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
