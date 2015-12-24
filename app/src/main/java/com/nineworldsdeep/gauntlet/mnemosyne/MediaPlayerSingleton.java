package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by brent on 11/25/15.
 */
public class MediaPlayerSingleton {

    private static MediaPlayerSingleton singleton = null;

    private MediaPlayer mp;
    private String nowPlayingPath;

    private MediaPlayerSingleton(){
        //singleton constructor
        mp = new MediaPlayer();

    }

    public static MediaPlayerSingleton getInstance(){

        if(singleton == null){
            singleton = new MediaPlayerSingleton();
        }

        return singleton;
    }

    public void play(String path) throws IOException {

        nowPlayingPath = path;

        if(mp.isPlaying()){
            mp.stop();
        }

        //TODO: this is a hack
        //it keeps crashing when I try to change
        //sources, so I'm just creating a new one
        mp = new MediaPlayer();

        mp.setDataSource(nowPlayingPath);
        mp.prepare();
        mp.start();
    }

    public void stop(){
        if(mp.isPlaying()){
            mp.stop();
        }
    }
}
