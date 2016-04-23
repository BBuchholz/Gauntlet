package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.io.IOException;
import java.util.List;

public class AudioDisplayActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    // TODO: I would like to go through this tutorial and try to implement a better player from it
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

    private void setNowPlaying(AudioMediaEntry ame){

        //display info here
        TextView tvHeader = (TextView)findViewById(R.id.tvHeader);
        TextView tvTagLine= (TextView)findViewById(R.id.tvTagline);

        tvHeader.setText(ame.getDefaultName());
        tvTagLine.setText(ame.getTags());

        this.ame = ame;
    }

    public void stopPlayback(View view) {

        mps.stop();
    }

    public void playPrevious(View view) {

        try{

            setNowPlaying(mps.playPrevious(this));

        }catch(Exception ex){

            Utils.toast(this, ex.getMessage());
        }
    }

    public void playNext(View view) {

        try{

            setNowPlaying(mps.playNext(this));

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

    }

    private void loadItems() {

        ListView lvItems = (ListView) findViewById(R.id.lvPlaylist);

        List<AudioMediaEntry> playlistEntries = mps.getPlaylist();

        lvItems.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        playlistEntries)
        );
    }
}
