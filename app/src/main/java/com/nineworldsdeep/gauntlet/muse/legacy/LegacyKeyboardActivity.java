package com.nineworldsdeep.gauntlet.muse.legacy;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;

import com.nineworldsdeep.gauntlet.muse.keys.Keyboard;

public class LegacyKeyboardActivity extends Activity {

    private Keyboard _keyboard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this._keyboard = new Keyboard(this);
        setContentView(this._keyboard);
    }
}
