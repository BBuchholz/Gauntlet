package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.io.IOException;

public class AudioDisplayActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    // TODO: I would like to go through this tutorial and try to implement a better player from it
    // http://www.androidhive.info/2012/03/android-building-audio-player-tutorial/
    // has features like seekbar that I am not currently implementing but would like to eventually

    private FileListItem ili;
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
        String s = i.getStringExtra(EXTRA_AUDIOPATH);

        if(s != null){

            ili = new FileListItem(s);

            Utils.toast(this, ili.toString());

            //mp = MediaPlayer.create(this, Uri.parse(ili.getFile().getPath()));
            //mp.start();

            mps = MediaPlayerSingleton.getInstance();

            try {
                mps.queueAndPlayLast(ili.getFile().getPath(), this);

            } catch (IOException e) {

                Utils.toast(this, e.getMessage());
            }

        }else{

            Utils.toast(this, "image path null");
        }
    }

    public void stopPlayback(View view) {

        mps.stop();
    }

    public void playPrevious(View view) {

        try{

            mps.playPrevious(this);

        }catch(Exception ex){

            Utils.toast(this, ex.getMessage());
        }
    }

    public void playNext(View view) {

        try{

            mps.playNext(this);

        }catch(Exception ex){

            Utils.toast(this, ex.getMessage());
        }
    }

    @Override
    public void onPrepared(MediaPlayer player) {

        player.start();
    }
}
