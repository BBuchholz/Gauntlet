package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class ArchivistAddSourceActivity extends AppCompatActivity {

    private int mSourceTypeId;
    private String mSourceTypeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivist_add_source);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        mSourceTypeId = intent.getIntExtra(
                Extras.INT_ARCHIVIST_SOURCE_TYPE_ID, -1);

        mSourceTypeName = intent.getStringExtra(
                Extras.STRING_ARCHIVIST_SOURCE_TYPE_NAME);

        if(mSourceTypeId < 1 ||
                Utils.stringIsNullOrWhitespace(mSourceTypeName)){

            Utils.toast(this, "Invalid Source Type Info");
            Button btnConfirm = (Button)findViewById(R.id.btnConfirm);
            btnConfirm.setEnabled(false);
        }
    }

    public void confirmClick(View v){

        Intent intent = new Intent();

        EditText etTitle = (EditText)findViewById(R.id.etSourceTitle);
        EditText etAuthor = (EditText)findViewById(R.id.etSourceAuthor);
        EditText etDirector = (EditText)findViewById(R.id.etSourceDirector);
        EditText etYear = (EditText)findViewById(R.id.etSourceYear);
        EditText etUrl = (EditText)findViewById(R.id.etSourceUrl);
        EditText etRetrievalDate = (EditText)findViewById(R.id.etSourceRetrievalDate);

        String sourceTitle = etTitle.getText().toString();
        String sourceAuthor = etAuthor.getText().toString();
        String sourceDirector = etDirector.getText().toString();
        String sourceYear = etYear.getText().toString();
        String sourceUrl = etUrl.getText().toString();
        String sourceRetrievalDate = etRetrievalDate.getText().toString();

        intent.putExtra(Extras.INT_ARCHIVIST_SOURCE_TYPE_ID, mSourceTypeId);
        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_TYPE_NAME, mSourceTypeName);
        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_TITLE, sourceTitle);
        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_AUTHOR, sourceAuthor);
        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_DIRECTOR, sourceDirector);
        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_YEAR, sourceYear);
        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_URL, sourceUrl);
        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_RETRIEVAL_DATE, sourceRetrievalDate);

        setResult(ArchivistActivity.REQUEST_RESULT_SOURCE, intent);

        finish();
    }

    public void cancelClick(View v){

        setResult(RESULT_CANCELED);
        finish();
    }
}
