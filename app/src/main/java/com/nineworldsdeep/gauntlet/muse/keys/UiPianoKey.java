package com.nineworldsdeep.gauntlet.muse.keys;

import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by brent on 10/13/15.
 */
public abstract class UiPianoKey extends RectF {

    private float dotX, dotY, dotRadius;
    private int chromaticIndex;

    public UiPianoKey(float left, float top, float right, float bottom, int chromaticIndex) {
        super(left,top,right,bottom);
        this.chromaticIndex = chromaticIndex;
        float width = right - left;
        dotX = (left + width / 2.0f);
        dotY = (bottom - width / 2.0f);
        dotRadius = (width / 4.0f);
    }

    public int getChromaticIndex(){
        return this.chromaticIndex;
    }

    public float getDotX() {
        return dotX;
    }

    public float getDotY() {
        return dotY;
    }

    public float getDotRadius() {
        return dotRadius;
    }

    public abstract Paint getMyDotPaint();

    public abstract Paint getMyKeyPaint();

}
