package com.nineworldsdeep.gauntlet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.core.Configuration;

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


    }

    private void updateStatusDisplay() {

        TextView tv = (TextView) findViewById(R.id.tvTestModeStatus);
        String status;

        if(Configuration.isInTestMode()){
            status = "DISABLED";
        }else{
            status = "DISABLED";
        }

        tv.setText(status);

        tv = (TextView) findViewById(R.id.tvDeleteDbForDevStatus);

        if(Configuration.isInDeleteDatabaseForDevelopmentMode()){
            status = "DISABLED";
        }else{
            status = "DISABLED";
        }

        tv.setText(status);
    }

    public void toggleDeleteDbForDev(View view) {


    }
}
