package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;

public class ArchivistAddSourceLocationEntryActivity extends AppCompatActivity {

    public static final int REQUEST_RESULT_LOCATIONS_AND_SUBSETS = 1;

    private FloatingActionButton fabAddSourceLocationsAndSubsets;
    private ArchivistSource currentSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivist_add_source_location_entry);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //store fab
        fabAddSourceLocationsAndSubsets =
                (FloatingActionButton) findViewById(R.id.fabAddSourceLocationsAndSubsets);

        fabAddSourceLocationsAndSubsets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent(ArchivistAddSourceLocationEntryActivity.this,
                                ArchivistAddSourceLocationsAndSubsetsActivity.class);

                startActivityForResult(intent, REQUEST_RESULT_LOCATIONS_AND_SUBSETS);

                //Utils.toast(ArchivistAddSourceLocationEntryActivity.this, "add source locations and subsets FAB clicked");
            }
        });

        ///////////////////////////////////////////
        //other display logic in onResume()
        ///////////////////////////////////////////
    }

    @Override
    public void onResume(){
        super.onResume();

        //display current source at top
        TextView tvCurrentSource =
                (TextView)findViewById(R.id.tvCurrentSourceShortDescription);

        currentSource = ArchivistWorkspace.getCurrentSource();

        if(tvCurrentSource != null && currentSource != null){

            tvCurrentSource.setText(currentSource.getShortDescription());
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

        spSourceLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                refreshSubsets();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                refreshSubsets();
            }
        });

        //Utils.toast(this, "locations refreshed");
    }

    private void refreshSubsets(){

        Spinner spSourceLocation = (Spinner)findViewById(R.id.spSourceLocation);

        if(spSourceLocation != null){

            ArchivistSourceLocation asl =
                    (ArchivistSourceLocation)spSourceLocation.getSelectedItem();

            if(asl != null){

                Spinner spSourceLocationSubset =
                        (Spinner)findViewById(R.id.spSourceLocationSubset);

                ArrayList<ArchivistSourceLocationSubset> subsets =
                        ArchivistWorkspace.getLocationSubsets(
                                this,
                                asl.getSourceLocationId());

                ArrayAdapter<ArchivistSourceLocationSubset> subsetAdapter =
                        new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item,
                                subsets);

                spSourceLocationSubset.setAdapter(subsetAdapter);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQUEST_RESULT_LOCATIONS_AND_SUBSETS && data != null){

            refreshLocations();
        }

        if(requestCode== RESULT_CANCELED){

            //do nothing
            //it crashes on back button without this, see:
            //http://stackoverflow.com/questions/20782619/failure-delivering-result-resultinfo

            Utils.toast(this, "cancelled");
        }
    }


    public void confirmClick(View v){

        Spinner spSubset = (Spinner)findViewById(R.id.spSourceLocationSubset);

        if(spSubset != null){

            ArchivistSourceLocationSubset asls =
                    (ArchivistSourceLocationSubset)spSubset.getSelectedItem();

            if(asls != null){

                EditText etEntryName =
                        (EditText)findViewById(R.id.etSourceLocationSubsetEntryName);

                if(etEntryName != null){

                    String entryName = etEntryName.getText().toString().trim();

                    if(!Utils.stringIsNullOrWhitespace(entryName)){

                        NwdDb db = NwdDb.getInstance(this);

                        db.insertOrIgnoreArchivistSourceLocationSubsetEntry(
                                asls.getSourceLocationsSubsetId(),
                                currentSource.getSourceId(),
                                entryName
                        );

                        //get back the object with the id populated
                        int sourceLocationEntryId = -1;
                        try {

                            sourceLocationEntryId = db.getSourceLocationEntryId(
                                    asls.getSourceLocationsSubsetId(),
                                    currentSource.getSourceId(),
                                    entryName);

                        } catch (Exception ex) {

                            Utils.toast(this, "error retrieving entry id: " + ex.getMessage());
                        }

                        //just instantiating to use the timestamp methods
                        ArchivistSourceLocationEntry asle =
                                new ArchivistSourceLocationEntry(
                                        currentSource.getSourceId(),
                                        asls.getSourceLocationsSubsetId(),
                                        entryName);

                        asle.verifyPresent();

                        db.updateArchivistSourceLocationSubsetEntryTimeStamps(
                                sourceLocationEntryId,
                                asle.getVerifiedPresent(),
                                asle.getVerifiedMissing()
                        );

                        Utils.toast(this, "entry ensured");

                        etEntryName.setText("");

                        setResult(
                                ArchivistSourceDetailsActivity.REQUEST_RESULT_SOURCE_LOCATION_ENTRY,
                                new Intent());

                        finish();

                    }else{
                        Utils.toast(this, "entry name cannot be empty");
                    }
                }
            }
        }
    }

    public void cancelClick(View v){

        setResult(RESULT_CANCELED);
        finish();
    }

}
