package com.nineworldsdeep.gauntlet.mnemosyne.v5;

/**
 * Created by brent on 2/6/17.
 */
public class ExtensionEntry {

    private String extension;
    private int count;

    public ExtensionEntry(String ext, int count){

        this.extension = ext;
        this.count = count;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString(){

        return getExtension() + " (" + getCount() + ")";
    }
}
