package com.nineworldsdeep.gauntlet.muse.guitar;

import android.app.Dialog;
import android.content.DialogInterface;

import com.nineworldsdeep.gauntlet.muse.legacy.LegacyGuitarActivity;

/**
 * Created by brent on 10/13/15.
 */
public class StringNumberChangeListener implements DialogInterface.OnClickListener {

    private LegacyGuitarActivity _guitarActivity;
    private Dialog _sender;

    public StringNumberChangeListener(LegacyGuitarActivity ga) {
        this._guitarActivity = ga;
    }

    public void onClick(DialogInterface dialog, int numStringsSelection) {
        // TODO Auto-generated method stub
        int numStrings = Integer.parseInt((String)Fretboard.stringNumOpts[numStringsSelection]);
        this._guitarActivity.setNumberStrings(numStrings);
        this._sender.dismiss();
    }

    public void setSender(Dialog dialog) {
        this._sender = dialog;
    }

}
