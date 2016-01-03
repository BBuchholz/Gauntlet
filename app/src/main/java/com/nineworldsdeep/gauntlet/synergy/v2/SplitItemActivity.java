package com.nineworldsdeep.gauntlet.synergy.v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;

public class SplitItemActivity extends AppCompatActivity {

    private int itemPosition;
    private String itemText;
    private ArrayList<String> splitItems = new ArrayList<>();

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

            splitItems.add(itemText);
            refreshListItems();
        }
    }

    private void refreshListItems(){

        ListView lvItems = (ListView) findViewById(R.id.lvItems);

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                splitItems));
    }

    public void moveWordNext(View view) {
        Utils.toast(this, "move next");
    }

    public void moveWordPrev(View view) {
        Utils.toast(this, "move prev");
    }

    public void addSplit(View view) {

        int lastPos = splitItems.size() - 1;

        if(lastPos > -1){

            String currentText = splitItems.get(lastPos);
            String[] split = currentText.split("\\s+", 2);
            String firstWord = split[0];
            String restOfText = "";

            //this should be true unless it is a single word, in which case no split
            if(split.length == 2){
                restOfText = split[1];

                if(!Utils.stringIsNullOrWhitespace(restOfText)){

                    splitItems.set(lastPos, firstWord);
                    splitItems.add(restOfText);
                    refreshListItems();
                }
            }
        }
    }

    public void removeSplit(View view) {

        int lastPos = splitItems.size() - 1;
        int secondToLastPos = lastPos - 1;

        if(lastPos > 0){

            String cat =
                    splitItems.get(secondToLastPos)
                            + " " + splitItems.get(lastPos);

            splitItems.set(secondToLastPos, cat);
            splitItems.remove(lastPos);
            refreshListItems();
        }
    }

    public void performSplit(View view) {

        Utils.toast(this, "perform split");

        Intent intent=new Intent();

//        ArrayList<String> lst = new ArrayList<>();
//
//        lst.add("test 1");
//        lst.add("test 2");
//        lst.add("test 3");

        intent.putExtra(Extras.ARRAYLIST_STRING_LIST_ITEMS, splitItems);
        intent.putExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, itemPosition);

        setResult(SynergyListActivity.REQUEST_RESULT_SPLIT_ITEM, intent);

        finish();
    }
}
