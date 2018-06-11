package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class ArchivistAddSourceLocationEntryActivity extends AppCompatActivity {

    public static final int REQUEST_RESULT_LOCATIONS_AND_SUBSETS = 1;

    private FloatingActionButton fabAddSourceLocationsAndSubsets;

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

    }

    private void refreshLocationsAndSubsets(){
        Utils.toast(this, "locations refreshed");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQUEST_RESULT_LOCATIONS_AND_SUBSETS && data != null){

            refreshLocationsAndSubsets();
        }

        if(requestCode== RESULT_CANCELED){

            //do nothing
            //it crashes on back button without this, see:
            //http://stackoverflow.com/questions/20782619/failure-delivering-result-resultinfo

            Utils.toast(this, "cancelled");
        }
    }


    public void confirmClick(View v){


        Intent intent = new Intent();
//
//        EditText etTitle = (EditText)findViewById(R.id.etSourceTitle);
//        EditText etAuthor = (EditText)findViewById(R.id.etSourceAuthor);
//        EditText etDirector = (EditText)findViewById(R.id.etSourceDirector);
//        EditText etYear = (EditText)findViewById(R.id.etSourceYear);
//        EditText etUrl = (EditText)findViewById(R.id.etSourceUrl);
//        EditText etRetrievalDate = (EditText)findViewById(R.id.etSourceRetrievalDate);
//
//        String sourceTitle = etTitle != null ? etTitle.getText().toString() : null;
//        String sourceAuthor = etAuthor != null ? etAuthor.getText().toString() : null;
//        String sourceDirector = etDirector != null ? etDirector.getText().toString() : null;
//        String sourceYear = etYear != null ? etYear.getText().toString() : null;
//        String sourceUrl = etUrl != null ? etUrl.getText().toString() : null;
//        String sourceRetrievalDate = etRetrievalDate != null ? etRetrievalDate.getText().toString() : null;
//
//        intent.putExtra(Extras.INT_ARCHIVIST_SOURCE_TYPE_ID, mCurrentSourceType.getSourceTypeId());
//        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_TYPE_NAME, mCurrentSourceType.getSourceTypeName());
//        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_TITLE, sourceTitle);
//        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_AUTHOR, sourceAuthor);
//        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_DIRECTOR, sourceDirector);
//        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_YEAR, sourceYear);
//        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_URL, sourceUrl);
//        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_RETRIEVAL_DATE, sourceRetrievalDate);
//
        setResult(ArchivistSourceDetailsActivity.REQUEST_RESULT_SOURCE_LOCATION_ENTRY, intent);

        finish();
    }

    public void cancelClick(View v){

        setResult(RESULT_CANCELED);
        finish();
    }

}
