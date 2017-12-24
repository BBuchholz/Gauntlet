package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;

public class ArchivistAddSourceTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivist_add_source_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    public void confirmClick(View v){

        Intent intent = new Intent();

        EditText et = (EditText)findViewById(R.id.etSourceTypeName);

        String typeName = et.getText().toString();

        intent.putExtra(Extras.STRING_ARCHIVIST_SOURCE_TYPE_NAME, typeName);

        setResult(ArchivistActivity.REQUEST_RESULT_SOURCE_TYPE_NAME, intent);

        finish();
    }

    public void cancelClick(View v){

        setResult(RESULT_CANCELED);
        finish();
    }

}
