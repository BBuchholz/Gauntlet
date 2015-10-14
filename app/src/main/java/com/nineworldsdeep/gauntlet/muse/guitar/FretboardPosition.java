package com.nineworldsdeep.gauntlet.muse.guitar;

import android.graphics.RectF;

/**
 * Created by brent on 10/13/15.
 */
public class FretboardPosition {

    private boolean highlighted;
    private RectF touchRegion, drawRegion;
    private boolean zeroFret = false;
    private int stringPosId, fretPosId;
    private boolean valid;

    public FretboardPosition(float left, float top, float right, float bottom, Fret fret, GuitarString string){
        this.touchRegion = new RectF(left,top,right,bottom);
        this.drawRegion = this.generateDrawRegion(touchRegion);
        highlighted = false;
        this.zeroFret = fret.isZeroFret();
        this.stringPosId = string.getPositionalId();
        this.fretPosId = fret.getPositionalId();
    }

    public int getStringPosId() {
        return stringPosId;
    }

    public int getFretPosId() {
        return fretPosId;
    }

    public boolean isZeroFret() {
        return zeroFret;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isValid(){
        return this.valid;
    }

    public void setValid(boolean valid){
        this.valid = valid;
    }

    public RectF getTouchRegion(){
        return this.touchRegion;
    }

    public RectF getDrawRegion(){
        return this.drawRegion;
    }

    private RectF generateDrawRegion(RectF tRegion){

        float newLeft, newRight, newTop, newBottom;
        float height = tRegion.bottom - tRegion.top;
        float width = tRegion.right - tRegion.left;
        float smallerDimension = (height > width) ? width : height;
        float widthOffset = width / 2;
        float heightOffset = height / 2;
        float drawRegionRadius = smallerDimension / 4;

        float xMid = tRegion.left + widthOffset;
        float yMid = tRegion.top + heightOffset;

        newTop = yMid - drawRegionRadius;
        newBottom = yMid + drawRegionRadius;
        newLeft = xMid - drawRegionRadius;
        newRight = xMid + drawRegionRadius;


        return new RectF(newLeft,newTop,newRight,newBottom);

    }

}
