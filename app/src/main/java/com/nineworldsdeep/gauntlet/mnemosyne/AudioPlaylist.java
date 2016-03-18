package com.nineworldsdeep.gauntlet.mnemosyne;

import java.util.ArrayList;

/**
 * Created by brent on 3/18/16.
 */
public class AudioPlaylist {

    private ArrayList<AudioMediaEntry> entries =
            new ArrayList<>();
    private boolean loopAll;

    public AudioPlaylist(){

        //chained constructor, default loop all
        this(true);
    }

    public AudioPlaylist(boolean loopAll){

        this.loopAll = loopAll;
    }

    private int currentPosition = 0;

    public boolean hasEntryAtCurrentPosition(){

        return currentPosition < entries.size();
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
    public AudioMediaEntry getCurrent(){

        if(hasEntryAtCurrentPosition()){

            return entries.get(currentPosition);
        }

        return null;
    }

    public void goToNextTrack(){

        currentPosition += 1;

        if(loopAll && currentPosition > entries.size() - 1){

            resetPosition();
        }
    }

    public void goToPreviousTrack(){

        currentPosition -= 1;

        if(currentPosition < 0){

            if(loopAll){

                currentPosition = entries.size() - 1;

            }else{

                resetPosition();
            }
        }
    }

    public void advanceToLast(){

        currentPosition = entries.size() - 1;

        if(currentPosition < 0){

            resetPosition();
        }
    }

    public void resetPosition(){

        currentPosition = 0;
    }

    public void add(AudioMediaEntry ame){

        entries.add(ame);
    }

    public int size(){

        return entries.size();
    }
}
