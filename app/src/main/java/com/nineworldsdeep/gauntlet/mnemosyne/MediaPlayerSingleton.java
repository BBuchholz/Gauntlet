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

        if(mp.isPlaying()){
            mp.stop();
        }

        //TODO: this is a hack
        //it keeps crashing when I try to change
        //sources, so I'm just creating a new one
        mp = new MediaPlayer();

        mp.setDataSource(path);
        mp.prepare();
        mp.start();
    }
}
