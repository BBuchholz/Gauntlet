package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;

public class ArchivistAddSourceLocationsAndSubsetsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivist_add_source_locations_and_subsets);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        refreshLocations();
    }

    private void refreshLocations(){

        Spinner spSourceLocation = (Spinner)findViewById(R.id.spSourceLocation);

        ArrayList<ArchivistSourceLocation> locations =
                ArchivistWorkspace.getAllLocations(this);

        ArrayAdapter<ArchivistSourceLocation> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, locations);

        spSourceLocation.setAdapter(adapter);

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
        //Utils.toast(this, "add subset goes here");

        Spinner spSourceLocation = (Spinner)findViewById(R.id.spSourceLocation);

        if(spSourceLocation != null){

            ArchivistSourceLocation asl =
                    (ArchivistSourceLocation)spSourceLocation.getSelectedItem();

            if(asl != null){

                EditText etSubset =
                        (EditText)findViewById(R.id.etSourceLocationSubset);

                if(etSubset != null){

                    String subsetName = etSubset.getText().toString().trim();

                    if(!Utils.stringIsNullOrWhitespace(subsetName)){

                        NwdDb db = NwdDb.getInstance(this);

                        db.ensureArchivistSourceLocationSubset(
                                asl.getSourceLocationId(),
                                subsetName
                        );

                        Utils.toast(this, "subset ensured");

                        etSubset.setText("");
                    }
                }
            }
        }
    }

    public void addLocation(View view) {
        //Utils.toast(this,"add location goes here");

        EditText etLocation = (EditText)findViewById(R.id.etLocationName);

        if (etLocation != null) {

            String locationName = etLocation.getText().toString().trim();

            if(!Utils.stringIsNullOrWhitespace(locationName)){

                NwdDb db = NwdDb.getInstance(this);

                db.ensureArchivistSourceLocationValue(locationName);

                refreshLocations();

                Utils.toast(this, "location ensured");

                etLocation.setText("");
            }
        }
    }


}
