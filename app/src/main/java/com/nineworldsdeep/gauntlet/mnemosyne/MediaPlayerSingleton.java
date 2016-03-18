package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by brent on 11/25/15.
 */
public class MediaPlayerSingleton {

    // TODO: I would like to go through this tutorial and try to implement a better player from it
    // http://www.androidhive.info/2012/03/android-building-audio-player-tutorial/
    //
    // this version is totally cowboy-coded, so its rough around the edges but it does what I
    // need it to, so for now, in the interest of getting it going, I'm just leaving this link
    // here for later :)

    private static MediaPlayerSingleton singleton = null;

    private MediaPlayer mp;
    //private String nowPlayingPath;
    private AudioPlaylist playlist = new AudioPlaylist();

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

    public void queueAndPlayFromCurrent(String path) throws IOException {

        //nowPlayingPath = path;
        AudioMediaEntry ame = new AudioMediaEntry();
        ame.setPath(path);
        playlist.add(ame);

        if(mp.isPlaying()){
            mp.stop();
        }

        //TODO: this is a hack
        //it keeps crashing when I try to change
        //sources, so I'm just creating a new one
        mp = new MediaPlayer();

        ame = playlist.getCurrent();

        if(ame != null){

            mp.setDataSource(ame.getPath());
            mp.prepare();
            mp.start();
        }
    }

    public void stop(){
        if(mp.isPlaying()){
            mp.stop();
        }
    }
}
