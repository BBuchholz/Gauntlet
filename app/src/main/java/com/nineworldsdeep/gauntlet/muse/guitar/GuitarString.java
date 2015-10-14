package com.nineworldsdeep.gauntlet.muse.guitar;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by brent on 10/13/15.
 */
public class GuitarString {

    Point[] points = new Point[2];
    int positionalId;
    PointF labelPos, noteLabelPos;

    public GuitarString(Point lowEnd, Point highEnd, int positionalId, PointF labelPos, PointF noteLabelPos){
        this.points[0] = lowEnd;
        this.points[1] = highEnd;
        this.positionalId = positionalId;
        this.labelPos = labelPos;
        this.noteLabelPos = noteLabelPos;
    }

    public Point[] getPoints() {
        return points;
    }

    public int getPositionalId() {
        return positionalId;
    }

    public PointF getStringLabelPosition() {
        return labelPos;
    }

    public PointF getNoteLabelPosition(){
        return noteLabelPos;
    }

}
