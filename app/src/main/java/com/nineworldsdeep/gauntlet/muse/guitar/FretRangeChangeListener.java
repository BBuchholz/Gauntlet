package com.nineworldsdeep.gauntlet.muse.guitar;

import android.app.Dialog;
import android.content.DialogInterface;

import com.nineworldsdeep.gauntlet.muse.legacy.LegacyGuitarActivity;

/**
 * Created by brent on 10/13/15.
 */
public class FretRangeChangeListener implements DialogInterface.OnClickListener {

    private LegacyGuitarActivity _guitarActivity;
    private Dialog _sender;

    public FretRangeChangeListener(LegacyGuitarActivity ga) {
        this._guitarActivity = ga;
    }

    public void onClick(DialogInterface dialog, int numStringsSelection) {

        int firstFret = Integer.parseInt((String)Fretboard.fretRangeOpts[numStringsSelection]);
        this._guitarActivity.setFretRange(firstFret);
        this._sender.dismiss();
    }

    public void setSender(Dialog dialog) {
        this._sender = dialog;
    }

}
