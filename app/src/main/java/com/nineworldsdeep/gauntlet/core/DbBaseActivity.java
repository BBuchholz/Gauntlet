package com.nineworldsdeep.gauntlet.core;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

public abstract class DbBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume(){
        super.onResume();
        NwdDb.getInstance(this).open();
    }
}
