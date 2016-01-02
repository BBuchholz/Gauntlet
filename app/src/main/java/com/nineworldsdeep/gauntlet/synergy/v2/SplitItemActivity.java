package com.nineworldsdeep.gauntlet.synergy.v2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class SplitItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void moveWordNext(View view) {
        Utils.toast(this, "move next");
    }

    public void moveWordPrev(View view) {
        Utils.toast(this, "move prev");
    }

    public void addSplit(View view) {
        Utils.toast(this, "add split");
    }

    public void removeSplit(View view) {
        Utils.toast(this, "remove split");
    }
}
