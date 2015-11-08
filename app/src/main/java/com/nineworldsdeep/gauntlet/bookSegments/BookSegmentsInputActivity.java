package com.nineworldsdeep.gauntlet.bookSegments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class BookSegmentsInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_segments_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String aliasStr =
                intent.getStringExtra(
                        AliasListActivity.EXTRA_ALIASLIST_ALIAS);

        setTextByViewId(R.id.etAlias, aliasStr);
    }

    public void onAddItem(View view) {

        String strAlias = getTextByViewId(R.id.etAlias);
        String strPageRange = getTextByViewId(R.id.etPageRange);
        String strSegment = getTextByViewId(R.id.etSegment);
        String strKeywords = getTextByViewId(R.id.etKeywords);
        String strConferre = getTextByViewId(R.id.etConferre);
        String strNotes = getTextByViewId(R.id.etNotes);

        if(Utils.stringIsNullOrWhitespace(strPageRange) ||
                Utils.stringIsNullOrWhitespace(strAlias) ||
                Utils.stringIsNullOrWhitespace(strKeywords)){

            Utils.toast(this, "PageRange, Alias and Keywords are required fields");

        }else{

            BookSegmentsFile bsf = new BookSegmentsFile(strAlias);
            bsf.loadItems();

            bsf.addBookSegment(strAlias,
                    strPageRange,
                    strSegment,
                    strKeywords,
                    strConferre,
                    strNotes);

            bsf.save();

            Utils.toast(this, "Book Segment Added");
            clearAll();
        }

    }

    private String getTextByViewId(int id){

        EditText et = (EditText)findViewById(id);
        return et.getText().toString();
    }

    private void setTextByViewId(int id, String text){

        EditText et = (EditText)findViewById(id);
        et.setText(text);
    }

    private void clearById(int id){

        EditText et = (EditText)findViewById(id);
        et.setText("");
    }

    private void clearAll(){

        clearById(R.id.etPageRange);
        clearById(R.id.etSegment);
        clearById(R.id.etKeywords);
        clearById(R.id.etConferre);
        clearById(R.id.etNotes);
    }
}
