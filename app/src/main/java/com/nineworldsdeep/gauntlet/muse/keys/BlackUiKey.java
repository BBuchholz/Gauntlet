package com.nineworldsdeep.gauntlet.muse.keys;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by brent on 10/13/15.
 */
public class BlackUiKey extends UiPianoKey {

    private static final Paint myDotPaint, myKeyPaint;

    static {
        myDotPaint = new Paint();
        myKeyPaint = new Paint();

        myDotPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myDotPaint.setColor(Color.WHITE);

        myKeyPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myKeyPaint.setColor(Color.BLACK);
    }

    public BlackUiKey(float left, float top, float right, float bottom, int chromaticIndex) {
        super(left, top, right, bottom, chromaticIndex);

    }

    public Paint getMyDotPaint() {

        return myDotPaint;
    }

    public Paint getMyKeyPaint() {

        return myKeyPaint;
    }

}