package com.nineworldsdeep.gauntlet.muse.guitar;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by brent on 10/13/15.
 */
public class Fret {

    Point[] points = new Point[2];
    boolean zeroFret = false;
    int positionalId;
    private PointF labelPosition;

    public Fret(Point left, Point right, boolean isZeroFret, int positionalId, PointF labelPosition){
        this.points[0] = left;
        this.points[1] = right;
        this.zeroFret = isZeroFret;
        this.positionalId = positionalId;
        this.labelPosition = labelPosition;
    }

    public Point[] getPoints() {
        return points;
    }

    public boolean isZeroFret() {
        return zeroFret;
    }

    public int getPositionalId() {
        return positionalId;
    }

    public PointF getFretLabelPosition() {
        return labelPosition;
    }

}
