package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import com.nineworldsdeep.gauntlet.mnemosyne.AudioMediaEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 3/18/16.
 */
public class AudioPlaylistV5 {

    private ArrayList<MediaListItem> mediaListItems =
            new ArrayList<>();
    private boolean loopAll;

    public AudioPlaylistV5(){

        //chained constructor, default loop all
        this(true);
    }

    public AudioPlaylistV5(boolean loopAll){

        this.loopAll = loopAll;
    }

    private int currentPosition = 0;

    public boolean hasEntryAtCurrentPosition(){

        return currentPosition < mediaListItems.size();
    }

    /**
     * will get the entry at the current position
     * and will return null if there is no entry
     * at the current position (either playlist is
     * empty or current position has incremented
     * beyond the upper bound of the playlist,
     * in which case resetPosition() can be called to
     * set the current position back to 0)
     * @return
     */
    public MediaListItem getCurrent(){

        if(hasEntryAtCurrentPosition()){

            return mediaListItems.get(currentPosition);
        }

        return null;
    }

    public void goToNextTrack(){

        currentPosition += 1;

        if(loopAll && currentPosition > mediaListItems.size() - 1){

            resetPosition();
        }
    }

    public void goToPreviousTrack(){

        currentPosition -= 1;

        if(currentPosition < 0){

            if(loopAll){

                currentPosition = mediaListItems.size() - 1;

            }else{

                resetPosition();
            }
        }
    }

    public void advanceToLast(){

        currentPosition = mediaListItems.size() - 1;

        if(currentPosition < 0){

            resetPosition();
        }
    }

    public void resetPosition(){

        currentPosition = 0;
    }

    public void add(MediaListItem mli){

        mediaListItems.add(mli);
    }

    public int size(){

        return mediaListItems.size();
    }

    public ArrayList<MediaListItem> getAll() {

        return mediaListItems;
    }

    public void clear() {

        mediaListItems.clear();
        resetPosition();
    }
}
