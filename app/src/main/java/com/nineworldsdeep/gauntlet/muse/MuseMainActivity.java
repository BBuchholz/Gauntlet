package com.nineworldsdeep.gauntlet.muse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.muse.clef.MuseClefActivity;
import com.nineworldsdeep.gauntlet.muse.guitar.GuitarActivity;
import com.nineworldsdeep.gauntlet.muse.keys.MuseKeyboardActivity;
import com.nineworldsdeep.gauntlet.muse.legacy.LegacyClefActivity;
import com.nineworldsdeep.gauntlet.muse.legacy.LegacyGuitarActivity;
import com.nineworldsdeep.gauntlet.muse.legacy.LegacyKeyboardActivity;

public class MuseMainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_muse_main);

    }

    public void launchClef(View view){

        startActivity(new Intent(this, MuseClefActivity.class));
    }

    public void launchKeys(View view){

        startActivity(new Intent(this, MuseKeyboardActivity.class));
    }

    public void launchFrets(View view){

        startActivity(new Intent(this, GuitarActivity.class));
    }

    public void launchLegacyClef(View view){

        startActivity(new Intent(this, LegacyClefActivity.class));
    }

    public void launchLegacyKeys(View view){

        startActivity(new Intent(this, LegacyKeyboardActivity.class));
    }

    public void launchLegacyFrets(View view){

        startActivity(new Intent(this, LegacyGuitarActivity.class));
    }
}
