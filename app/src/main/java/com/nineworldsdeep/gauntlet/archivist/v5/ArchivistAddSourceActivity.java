package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class ArchivistAddSourceActivity extends AppCompatActivity {

//    private int mSourceTypeId;
//    private String mSourceTypeName;

    private ArchivistSourceType mCurrentSourceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivist_add_source);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mCurrentSourceType = ArchivistWorkspace.getCurrentSourceType();

        Button btnConfirm = (Button)findViewById(R.id.btnConfirm);
        TextView tvSourceTypeName = (TextView)findViewById(R.id.tvSourceTypeName);

        if(mCurrentSourceType != null && mCurrentSourceType.getSourceTypeId() > 0) {

            if (tvSourceTypeName != null && btnConfirm != null) {
                tvSourceTypeName.setText(mCurrentSourceType.getSourceTypeName());
                btnConfirm.setEnabled(true);
            }

        }else{

            Utils.toast(this, "Invalid Source Type Info");
            if (btnConfirm != null) {
                btnConfirm.setEnabled(false);
            }
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

        String sourceTitle = etTitle != null ? etTitle.getText().toString() : null;
        String sourceAuthor = etAuthor != null ? etAuthor.getText().toString() : null;
        String sourceDirector = etDirector != null ? etDirector.getText().toString() : null;
        String sourceYear = etYear != null ? etYear.getText().toString() : null;
        String sourceUrl = etUrl != null ? etUrl.getText().toString() : null;
        String sourceRetrievalDate = etRetrievalDate != null ? etRetrievalDate.getText().toString() : null;

        intent.putExtra(Extras.INT_ARCHIVIST_SOURCE_TYPE_ID, mCurrentSourceType.getSourceTypeId());
        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_TYPE_NAME, mCurrentSourceType.getSourceTypeName());
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
