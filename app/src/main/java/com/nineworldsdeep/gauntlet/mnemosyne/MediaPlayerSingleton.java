package com.nineworldsdeep.gauntlet.mnemosyne;

import android.media.MediaPlayer;

import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brent on 11/25/15.
 */
public class MediaPlayerSingleton{

    // TODO: I would like to go through this tutorial and try to implement a better player from it
    // http://www.androidhive.info/2012/03/android-building-audio-player-tutorial/
    //
    // this version is totally cowboy-coded, so its rough around the edges but it does what I
    // need it to, so for now, in the interest of getting it going, I'm just leaving this linkNodes
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

            // this should make our lazy instantiation thread safe
            synchronized (MediaPlayerSingleton.class){

                if(singleton == null){

                    singleton = new MediaPlayerSingleton();
                }
            }
        }

        return singleton;
    }

    public AudioMediaEntry queueAndPlayLast(HashMap<String,String> pathToTagString,
                                            String path,
                                            MediaPlayer.OnPreparedListener
                                                    listener)
            throws IOException {

        //nowPlayingPath = path;
        AudioMediaEntry ame = new AudioMediaEntry(path, pathToTagString);
        playlist.add(ame);
        playlist.advanceToLast();

        play(listener);

        return ame;
    }

    private AudioMediaEntry play(MediaPlayer.OnPreparedListener listener) throws IOException{

        if(mp != null && mp.isPlaying()){
            mp.stop();
        }

        //TODO: this is a hack
        //it keeps crashing when I try to change
        //sources, so I'm just creating a new one
        //mp = new MediaPlayer();

        AudioMediaEntry ame = playlist.getCurrent();

        if(ame != null){

            mp.reset();
            mp.setDataSource(ame.getPath());
            mp.setOnPreparedListener(listener);
            mp.setLooping(true);
            mp.prepareAsync();
        }

        return ame;
    }

    public AudioMediaEntry playPrevious(MediaPlayer.OnPreparedListener listener) throws IOException {

        //nowPlayingPath = path;
        playlist.goToPreviousTrack();

//        if(mp.isPlaying()){
//            mp.stop();
//        }
//
//        //TODO: this is a hack
//        //it keeps crashing when I try to change
//        //sources, so I'm just creating a new one
//
//        AudioMediaEntry ame = playlist.getCurrent();
//
//        if(ame != null){
//
//            mp = new MediaPlayer();
//
//            if(ame != null){
//
//                mp.setDataSource(ame.getPath());
//                mp.prepare();
//                mp.start();
//            }

        //}

        return play(listener);
    }

    public AudioMediaEntry playNext(MediaPlayer.OnPreparedListener listener) throws IOException {

        //nowPlayingPath = path;
        playlist.goToNextTrack();

        return play(listener);
//
//        if(mp.isPlaying()){
//            mp.stop();
//        }
//
//        //TODO: this is a hack
//        //it keeps crashing when I try to change
//        //sources, so I'm just creating a new one
//
//        AudioMediaEntry ame = playlist.getCurrent();
//
//        if(ame != null){
//
//            mp = new MediaPlayer();
//
//            if(ame != null){
//
//                mp.setDataSource(ame.getPath());
//                mp.prepare();
//                mp.start();
//            }
//        }
    }

    public void queueAndPlayFromCurrent(HashMap<String,String> pathToTagString,
                                        String path,
                                        MediaPlayer.OnPreparedListener
                                                listener)
            throws IOException {

        //nowPlayingPath = path;
        AudioMediaEntry ame = new AudioMediaEntry(path, pathToTagString);
        playlist.add(ame);

        play(listener);

//        if(mp.isPlaying()){
//            mp.stop();
//        }
//
//        //TODO: this is a hack
//        //it keeps crashing when I try to change
//        //sources, so I'm just creating a new one
//        mp = new MediaPlayer();
//
//        ame = playlist.getCurrent();
//
//        if(ame != null){
//
//            mp.setDataSource(ame.getPath());
//            mp.prepare();
//            mp.start();
//        }
    }

    public void stop(){
        if(mp != null && mp.isPlaying()){
            mp.stop();
        }
    }

    public List<AudioMediaEntry> getPlaylist() {

        return playlist.getAll();
    }

    public void resetPlayer() throws IOException {

        //TODO: experimental -> test this

        if(mp.isPlaying()){
            mp.stop();
        }

        mp = new MediaPlayer();

        //AudioMediaEntry ame = playlist.getCurrent();
        playlist.clear();

//        if(ame != null){
//
//            mp.setDataSource(ame.getPath());
//            mp.prepare();
//            mp.start();
//        }
    }
}
