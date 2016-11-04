package com.nineworldsdeep.gauntlet.synergy.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class SynergyV5EditItemActivity extends AppCompatActivity {

    public static final String STRING_SYNERGY_V5_ITEM_VALUE =
            "com.nineworldsdeep.gauntlet.STRING_SYNERGY_V5_ITEM_VALUE";

    int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_v5_edit_item);

        Intent intent = getIntent();

        itemPosition =
                intent.getIntExtra(
                Extras.INT_SYNERGY_LIST_ITEM_POS, -1);
        String rawText =
                intent.getStringExtra(STRING_SYNERGY_V5_ITEM_VALUE);

//        keyVals.put("item", "some item text");
//        keyVals.put("tags", "testing, test");
//        keyVals.put("filePath", "/NWD-SNDBX/test.txt");

    }

    public void voiceMemoTag(View view) {

        EditText et = (EditText)findViewById(R.id.txtEdit);
        String currentText = et.getText().toString();
        String timeStamp = " (has VoiceMemo: " +
                Utils.getCurrentTimeStamp_yyyyMMdd() + ")";

        if(!currentText.endsWith(timeStamp)){

            currentText += timeStamp;
            et.setText(currentText);
        }
    }


    public void imageTag(View view) {

        EditText et = (EditText)findViewById(R.id.txtEdit);
        String currentText = et.getText().toString();
        String timeStamp = " (has Image: " +
                Utils.getCurrentTimeStamp_yyyyMMdd() + ")";

        if(!currentText.endsWith(timeStamp)){

            currentText += timeStamp;
            et.setText(currentText);
        }
    }

    public void confirmClick(View v){

        Utils.toast(this, "todo");
//
//        Intent intent=new Intent();
//
//        intent.putExtra(Extras.STRING_SYNERGY_LINE_ITEM_RAW_TEXT, newRawText);
//        intent.putExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, itemPosition);
//
//        setResult(SynergyV5ListActivity.REQUEST_RESULT_SPLIT_ITEM, intent);
//
//        finish();
    }

    public void cancelClick(View v){

        setResult(RESULT_CANCELED);
        finish();
    }

}
