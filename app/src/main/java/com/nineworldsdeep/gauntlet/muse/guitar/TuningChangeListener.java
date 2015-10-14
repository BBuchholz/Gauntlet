package com.nineworldsdeep.gauntlet.muse.guitar;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.muse.legacy.LegacyGuitarActivity;

/**
 * Created by brent on 10/13/15.
 */
public class TuningChangeListener implements DialogInterface.OnClickListener {

    private LegacyGuitarActivity _guitarActivity;
    private EditText _input;

    public TuningChangeListener(LegacyGuitarActivity guitarActivity, EditText input) {

        this._guitarActivity = guitarActivity;
        this._input = input;
    }

    public void onClick(DialogInterface dialog, int which) {

        Context context = this._guitarActivity.getApplicationContext();
        CharSequence text = "undefined";

        text = this._guitarActivity.changeTuning(this._input.getText().toString());

        Utils.toast(context, text.toString());
    }

}