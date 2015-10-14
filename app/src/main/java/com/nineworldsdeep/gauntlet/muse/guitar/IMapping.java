package com.nineworldsdeep.gauntlet.muse.guitar;

/**
 * Created by brent on 10/13/15.
 */
public interface IMapping {
    public String getFretViewLabel(int viewFretPosition);
    public String getStringViewLabel(int viewStringPosition);
    public String getNoteName(int viewFretPosition, int viewStringPosition);
    public String setSixStringTuning(String tuning);
    public String getSixStringTuning();
}
