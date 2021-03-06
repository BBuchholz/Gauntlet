package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class ArchivistAddSourceExcerptActivity extends AppCompatActivity {

    //private int mSourceId;
    //private String mSourceDescription;

    private ArchivistSource mCurrentSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivist_add_source_excerpt);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        Intent intent = getIntent();
//
//        mSourceId = intent.getIntExtra(
//                Extras.INT_ARCHIVIST_SOURCE_ID, -1);
//
//        //NOTE: this is a "description", not necessarily the title, it's what gets passed to the activity, whatever works best, could be the Url, or the "author" if it's a quote
//        mSourceDescription = intent.getStringExtra(
//                Extras.STRING_ARCHIVIST_SOURCE_DESCRIPTION);
//
//        if (mSourceId < 1 ||
//                Utils.stringIsNullOrWhitespace(mSourceDescription)) {
//
//            Utils.toast(this, "Invalid Source Info");
//            Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
//            btnConfirm.setEnabled(false);
//        }

        mCurrentSource = ArchivistWorkspace.getCurrentSource();

        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
        TextView tvSourceDescription = (TextView)findViewById(R.id.tvSourceDescription);

        if(mCurrentSource != null &&
                mCurrentSource.getSourceId() > 0){

            if(tvSourceDescription != null && btnConfirm != null){
                tvSourceDescription.setText(mCurrentSource.getShortDescription());
                btnConfirm.setEnabled(true);
            }

        }else{

            Utils.toast(this, "Invalid Source Info");

            if (btnConfirm != null) {
                btnConfirm.setEnabled(false);
            }
        }
    }

    public void confirmClick(View v){

        Intent intent = new Intent();

        EditText etSourceExcerptPages = (EditText)findViewById(R.id.etSourceExcerptPages);
        EditText etSourceExcerptBeginTime = (EditText)findViewById(R.id.etSourceExcerptBeginTime);
        EditText etSourceExcerptEndTime = (EditText)findViewById(R.id.etSourceExcerptEndTime);
        EditText etSourceExcerptValue = (EditText)findViewById(R.id.etSourceExcerptValue);

        String sourceExcerptPages = etSourceExcerptPages != null ? etSourceExcerptPages.getText().toString() : null;
        String sourceExcerptBeginTime = etSourceExcerptBeginTime != null ? etSourceExcerptBeginTime.getText().toString() : null;
        String sourceExcerptEndTime = etSourceExcerptEndTime != null ? etSourceExcerptEndTime.getText().toString() : null;
        String sourceExcerptValue = etSourceExcerptValue != null ? etSourceExcerptValue.getText().toString() : null;

        intent.putExtra(Extras.INT_ARCHIVIST_SOURCE_ID, mCurrentSource.getSourceId());
        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_PAGES, sourceExcerptPages);
        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_BEGIN_TIME, sourceExcerptBeginTime);
        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_END_TIME, sourceExcerptEndTime);
        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_VALUE, sourceExcerptValue);

        setResult(ArchivistActivity.REQUEST_RESULT_SOURCE_EXCERPT, intent);

        finish();
    }

    public void cancelClick(View v){

        setResult(RESULT_CANCELED);
        finish();
    }
}
