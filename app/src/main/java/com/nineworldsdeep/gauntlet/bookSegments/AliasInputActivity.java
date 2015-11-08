package com.nineworldsdeep.gauntlet.bookSegments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class AliasInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alias_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void onAddItem(View view) {

        String aliasText = getTextByViewId(R.id.etAlias);
        String titleText = getTextByViewId(R.id.etTitle);
        String authorText = getTextByViewId(R.id.etAuthor);
        String yearText = getTextByViewId(R.id.etYear);

        if(Utils.stringIsNullOrWhitespace(aliasText) ||
                Utils.stringIsNullOrWhitespace(titleText) ||
                Utils.stringIsNullOrWhitespace(authorText) ||
                Utils.stringIsNullOrWhitespace(yearText)){

            Utils.toast(this, "Please Fill In All Fields");

        }else{

            AliasListFile alf = new AliasListFile();
            alf.loadItems();

            alf.addAlias(aliasText, titleText, authorText, yearText);
            alf.save();

            Utils.toast(this, "Fragment Added");
            clearAll();
        }
    }

    private String getTextByViewId(int id){

        EditText et = (EditText)findViewById(id);
        return et.getText().toString();
    }

    private void clearById(int id){

        EditText et = (EditText)findViewById(id);
        et.setText("");
    }

    private void clearAll(){

        clearById(R.id.etAlias);
        clearById(R.id.etTitle);
        clearById(R.id.etAuthor);
        clearById(R.id.etYear);
    }
}
