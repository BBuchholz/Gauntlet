package com.nineworldsdeep.gauntlet.muse.legacy;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LegacyClefActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        TextView textview = new TextView(this);
        textview.setText("This is the Treble and Bass Clef");
        setContentView(textview);
    }
}
