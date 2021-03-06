package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.SeekBar;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.mnemosyne.AudioMediaEntry;
import com.nineworldsdeep.gauntlet.mnemosyne.AudioPlaylist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brent on 11/25/15.
 */
public class MediaPlayerSingletonV5 {

    // TODO: I would like to go through this tutorial and try to implement a better player from it
    // http://www.androidhive.info/2012/03/android-building-audio-player-tutorial/
    //
    // this version is totally cowboy-coded, so its rough around the edges but it does what I
    // need it to, so for now, in the interest of getting it going, I'm just leaving this linkNodes
    // here for later :)

    private static MediaPlayerSingletonV5 singleton = null;
    private static SeekBar mSeekBar = null;
    private static Handler seekHandler = new Handler();

    private MediaPlayer mp;
    //private String nowPlayingPath;
    private AudioPlaylistV5 playlist = new AudioPlaylistV5();

    private MediaPlayerSingletonV5(){
        //singleton constructor
        mp = new MediaPlayer();

    }

    public static MediaPlayerSingletonV5 getInstance(
            SeekBar seekBarForDisplay,
            //final MediaPlayer.OnPreparedListener onPreparedListener,
            final MediaPlayer.OnCompletionListener onCompletionListener){

        if(singleton == null){

            // this should make our lazy instantiation thread safe
            synchronized (MediaPlayerSingletonV5.class){

                if(singleton == null){

                    singleton = new MediaPlayerSingletonV5();
                }
            }
        }

        if(seekBarForDisplay != null) {

            mSeekBar = seekBarForDisplay;
        }

//        if(onPreparedListener != null){
//
//            singleton.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    try{
//
//                        singleton.playNext(onPreparedListener);
//
//                    }catch(Exception ex){
//
//                        //do nothing
//                    }
//                }
//            });
//        }

        if(onCompletionListener != null){

            singleton.mp.setOnCompletionListener(onCompletionListener);
        }

        return singleton;
    }

    /**
     * only use this method signature when you are sure the seekbar
     * is being set somewhere else (such as when calling from a
     * different Activity).
     *
     * Otherwise, use getInstance(Seekbar);
     */
    public static MediaPlayerSingletonV5 getInstance() {

        return getInstance(null, null);
    }

    public MediaListItem queueAndPlayLast(
                                MediaListItem mli,
                                MediaPlayer.OnPreparedListener listener)
            throws IOException {

        playlist.add(mli);
        playlist.advanceToLast();

        play(listener);

        return mli;
    }

    public MediaListItem getMediaListItem(int position){

        return playlist.get(position);
    }

    public void queue(ArrayList<MediaListItem> items){

        for(MediaListItem mli : items){

            playlist.add(mli);
        }
    }

    public MediaListItem play(MediaPlayer.OnPreparedListener listener) throws IOException{

        if(mp != null && mp.isPlaying()){
            mp.stop();
        }

        //TODO: this is a hack
        //it keeps crashing when I try to change
        //sources, so I'm just creating a new one
        //mp = new MediaPlayer();

        MediaListItem mli = playlist.getCurrent();

        if(mli != null){

            mp.reset();
            mp.setDataSource(mli.getFile().getAbsolutePath());
            mp.setOnPreparedListener(listener);
            //mp.setLooping(true);
            mp.setLooping(false); //adding onCompletionListener
            mp.prepareAsync();
        }

        return mli;
    }

    Runnable updateRunnable = new Runnable() {

        @Override public void run()
        {
            updateSeek();
        }
    };

    public void updateSeek() {

        mSeekBar.setProgress(mp.getCurrentPosition());
        seekHandler.postDelayed(updateRunnable, 10);
    }

    public void stopSeekUpdate(){

        seekHandler.removeCallbacks(updateRunnable);
    }

    public MediaListItem playPrevious(MediaPlayer.OnPreparedListener listener) throws IOException {

        playlist.goToPreviousTrack();

        return play(listener);
    }

    public MediaListItem playNext(MediaPlayer.OnPreparedListener listener) throws IOException {

        playlist.goToNextTrack();

        return play(listener);
    }
//
//    public void queueAndPlayFromCurrent(HashMap<String,String> pathToTagString,
//                                        String path,
//                                        MediaPlayer.OnPreparedListener
//                                                listener)
//            throws IOException {
//
//        AudioMediaEntry ame = new AudioMediaEntry(path, pathToTagString);
//        playlist.add(ame);
//
//        play(listener);
//
//    }

    public void stop(){
        if(mp != null && mp.isPlaying()){
            mp.stop();
        }
    }

    public ArrayList<MediaListItem> getPlaylist() {

        return playlist.getAll();
    }

    public void resetPlayer() throws IOException {

        if(mp.isPlaying()){
            mp.stop();
        }

        mp = new MediaPlayer();

        playlist.clear();

    }

    public void removeItem(int position) {

        playlist.remove(position);

        if(playlist.isCurrentPosition(position)){

            stop();
        }
    }

    public int getCurrentPosition() {

        return playlist.getCurrentPosition();
    }
}
