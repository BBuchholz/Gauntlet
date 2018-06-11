package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class ArchivistAddSourceLocationsAndSubsetsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivist_add_source_locations_and_subsets);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public void confirmClick(View v){

        setResult(
                ArchivistAddSourceLocationEntryActivity.REQUEST_RESULT_LOCATIONS_AND_SUBSETS,
                new Intent());

        finish();
    }

    public void cancelClick(View v){

        setResult(RESULT_CANCELED);
        finish();
    }


    public void addSubset(View view) {
        Utils.toast(this, "add subset goes here");
    }

    public void addLocation(View view) {
        Utils.toast(this,"add location goes here");
    }
}
