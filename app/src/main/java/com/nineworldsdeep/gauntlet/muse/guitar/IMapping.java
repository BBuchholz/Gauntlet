package com.nineworldsdeep.gauntlet.muse.guitar;

/**
 * Created by brent on 10/13/15.
 */
public interface IMapping {
    String getFretViewLabel(int viewFretPosition);
    String getStringViewLabel(int viewStringPosition);
    String getNoteName(int viewFretPosition, int viewStringPosition);
    String setSixStringTuning(String tuning);
    String getSixStringTuning();
}
