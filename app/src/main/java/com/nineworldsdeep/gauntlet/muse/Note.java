package com.nineworldsdeep.gauntlet.muse;

/**
 * Created by brent on 10/13/15.
 */
public class Note {

    private int positionalValue;
    private String noteName;


    public Note(int notePosVal, String name) {
        //TODO: this should be posVal
        //logic is currently for absVal (prototyping)
        this.positionalValue = notePosVal;
        this.noteName = name;
    }

    public int getPositionalValue() {
        return positionalValue;
    }

    public String getNoteName() {
        return noteName;
    }
}
