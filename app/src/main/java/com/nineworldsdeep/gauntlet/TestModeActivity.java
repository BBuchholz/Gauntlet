package com.nineworldsdeep.gauntlet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class TestModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateStatusDisplay();
    }

    public void toggleTestMode(View view) {

        Configuration.setTestMode(!Configuration.isInTestMode());
        updateStatusDisplay();
    }

    private void updateStatusDisplay() {

        TextView tv = (TextView) findViewById(R.id.tvStatus);
        String status = "[undefined]";

        if(Configuration.isInTestMode()){
            status = "ACTIVE";
        }else{
            status = "INACTIVE";
        }

        tv.setText(status);
    }
}
