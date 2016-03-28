package com.nineworldsdeep.gauntlet.synergy.v3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class SynergyEditItemActivity extends AppCompatActivity {

    HashMap<String, String> keyVals = new HashMap<>();
    ArrayList<String> keys = new ArrayList<>();
    String currentKey = null;
    int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_edit_item);

        Intent intent = getIntent();

        itemPosition = intent.getIntExtra(
                Extras.INT_SYNERGY_LIST_ITEM_POS, -1);

        keyVals.put("item", "some item text");
        keyVals.put("tags", "testing, test");
        keyVals.put("filePath", "/NWD-SNDBX/test.txt");

        setupSpinner();
    }

    private void setupSpinner() {

        keys = new ArrayList<>(keyVals.keySet());

        Spinner spinner = (Spinner) findViewById(R.id.spnKeys);

        ArrayAdapter<String> adapter =
                new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                        keys);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                currentKey = keys.get(position);
                EditText et = (EditText) findViewById(R.id.txtEdit);
                et.setText(keyVals.get(currentKey));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                currentKey = null;
            }
        });
    }

    public void storeValue(View view) {
        EditText et = (EditText)findViewById(R.id.txtEdit);
        String rawText = et.getText().toString();
        String singleLineText = Utils.removeHardReturns(rawText);
        keyVals.put(currentKey, singleLineText);
        Utils.toast(this, "stored");
    }

    public void confirmClick(View v){

        String newRawText = "item={confirm hasn't been implemented}";

        Intent intent=new Intent();

        intent.putExtra(Extras.STRING_SYNERGY_LINE_ITEM_RAW_TEXT, newRawText);
        intent.putExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, itemPosition);

        setResult(SynergyListActivity.REQUEST_RESULT_SPLIT_ITEM, intent);

        finish();
    }

    public void cancelClick(View v){

        setResult(RESULT_CANCELED);
        finish();
    }

}
