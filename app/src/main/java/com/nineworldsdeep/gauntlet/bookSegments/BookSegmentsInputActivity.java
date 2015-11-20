package com.nineworldsdeep.gauntlet.bookSegments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nineworldsdeep.gauntlet.DisplayMode;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class BookSegmentsInputActivity extends AppCompatActivity {

    public static final String EXTRA_INPUT_MODE =
            "com.nineworldsdeep.gauntlet.bookSegments.BOOKSEGMENTS_INPUT_MODE";

    public static final String EXTRA_PR =
            "com.nineworldsdeep.gauntlet.bookSegments.BOOKSEGMENTS_PR";
    public static final String EXTRA_SEG =
            "com.nineworldsdeep.gauntlet.bookSegments.BOOKSEGMENTS_SEG";
    public static final String EXTRA_KW =
            "com.nineworldsdeep.gauntlet.bookSegments.BOOKSEGMENTS_KW";
    public static final String EXTRA_CF =
            "com.nineworldsdeep.gauntlet.bookSegments.BOOKSEGMENTS_CF";
    public static final String EXTRA_NOTES =
            "com.nineworldsdeep.gauntlet.bookSegments.BOOKSEGMENTS_NOTES";

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

        DisplayMode mode =
                (DisplayMode) intent.getSerializableExtra(
                        EXTRA_INPUT_MODE);

        Utils.toast(this, mode.toString());

        //set in all modes
        setTextByViewId(R.id.etAlias, aliasStr);

        if(mode == DisplayMode.View){
            Button btn = (Button)findViewById(R.id.btnAdd);
            btn.setVisibility(View.GONE);

            String prStr = intent.getStringExtra(EXTRA_PR);
            String segStr = intent.getStringExtra(EXTRA_SEG);
            String kwStr = intent.getStringExtra(EXTRA_KW);
            String cfStr = intent.getStringExtra(EXTRA_CF);
            String notesStr = intent.getStringExtra(EXTRA_NOTES);

            setTextByViewId(R.id.etPageRange, prStr);
            setTextByViewId(R.id.etSegment, segStr);
            setTextByViewId(R.id.etKeywords, kwStr);
            setTextByViewId(R.id.etConferre, cfStr);
            setTextByViewId(R.id.etNotes, notesStr);
        }

    }

    private boolean validateInput(String strAlias,
                                  String strPageRange,
                                  String strSegment,
                                  String strKeywords,
                                  String strConferre,
                                  String strNotes){

        if(Utils.stringIsNullOrWhitespace(strAlias) ||
                Utils.stringIsNullOrWhitespace(strPageRange)){

            Utils.toast(this, "Alias and PageRange are required fields");
            return false;
        }

        if(Utils.stringIsNullOrWhitespace(strSegment) &&
                Utils.stringIsNullOrWhitespace(strKeywords) &&
                Utils.stringIsNullOrWhitespace(strConferre) &&
                Utils.stringIsNullOrWhitespace(strNotes)){

            Utils.toast(this, "At least one more field must be entered");
            return false;
        }

        return true;
    }

    public void onAddItem(View view) {

        String strAlias = getTextByViewId(R.id.etAlias);
        String strPageRange = getTextByViewId(R.id.etPageRange);
        String strSegment = getTextByViewId(R.id.etSegment);
        String strKeywords = getTextByViewId(R.id.etKeywords);
        String strConferre = getTextByViewId(R.id.etConferre);
        String strNotes = getTextByViewId(R.id.etNotes);

        if(validateInput(strAlias, strPageRange, strSegment,
                strKeywords, strConferre, strNotes)){

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
