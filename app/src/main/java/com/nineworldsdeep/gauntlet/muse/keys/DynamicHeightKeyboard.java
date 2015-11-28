package com.nineworldsdeep.gauntlet.muse.keys;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.nineworldsdeep.gauntlet.R;

/**
 * Created by brent on 11/27/15.
 */
public class DynamicHeightKeyboard extends Keyboard {

    private float mHeightRatio;

    public DynamicHeightKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DynamicHeightKeyboard, 0, 0);
        try {
            mHeightRatio = ta.getFloat(R.styleable.DynamicHeightKeyboard_heightRatio, 0.0f);
        } finally {
            ta.recycle();
        }
    }

    public DynamicHeightKeyboard(Context context) {
        super(context);
    }



    public void setHeightRatio(float ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.0f) {

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);

            //setMeasuredDimension(width, height);
            super.onMeasure(width, height);
        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
