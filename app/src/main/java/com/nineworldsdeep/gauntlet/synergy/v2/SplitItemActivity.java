package com.nineworldsdeep.gauntlet.synergy.v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;

public class SplitItemActivity extends AppCompatActivity {

    public static final String TEST_LIST =
            "com.nineworldsdeep.gauntlet.EXTRA_TEST_LIST";

    private int itemPosition;
    private String itemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        itemPosition = intent.getIntExtra(
                Extras.INT_SYNERGY_LIST_ITEM_POS, -1);

        itemText = intent.getStringExtra(
                Extras.STRING_SYNERGY_LIST_ITEM_TEXT);

        if(itemPosition > -1 && !Utils.stringIsNullOrWhitespace(itemText)){
            Utils.toast(this, "insert [" + itemText + "] here for split");
        }
    }

    public void moveWordNext(View view) {
        Utils.toast(this, "move next");
    }

    public void moveWordPrev(View view) {
        Utils.toast(this, "move prev");
    }

    public void addSplit(View view) {
        Utils.toast(this, "add split");
    }

    public void removeSplit(View view) {
        Utils.toast(this, "remove split");
    }

    public void performSplit(View view) {

        Utils.toast(this, "perform split");

        Intent intent=new Intent();

        ArrayList<String> lst = new ArrayList<>();

        lst.add("test 1");
        lst.add("test 2");
        lst.add("test 3");

        intent.putExtra(TEST_LIST, lst);
        intent.putExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, itemPosition);

        setResult(SynergyListActivity.REQUEST_RESULT_SPLIT_ITEM, intent);

        finish();
    }
}
