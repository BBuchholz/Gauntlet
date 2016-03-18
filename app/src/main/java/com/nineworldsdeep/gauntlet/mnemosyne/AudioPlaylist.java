package com.nineworldsdeep.gauntlet.mnemosyne;

import java.util.ArrayList;

/**
 * Created by brent on 3/18/16.
 */
public class AudioPlaylist {

    private ArrayList<AudioMediaEntry> entries =
            new ArrayList<>();

    private int currentPosition = 0;

    public boolean hasNext(){
        return currentPosition < entries.size();
    }

    public AudioMediaEntry getCurrent(){
        if(hasNext()){
            return entries.get(currentPosition);
        }
        return null;
    }

    public void advance(){
        currentPosition += 1;
    }

    public void reset(){
        currentPosition = 0;
    }

    public void add(AudioMediaEntry ame){
        entries.add(ame);
    }

    public int size(){
        return entries.size();
    }
}
