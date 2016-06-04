package com.nineworldsdeep.gauntlet.muse.legacy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.muse.guitar.FretRangeChangeListener;
import com.nineworldsdeep.gauntlet.muse.guitar.Fretboard;
import com.nineworldsdeep.gauntlet.muse.guitar.StringNumberChangeListener;
import com.nineworldsdeep.gauntlet.muse.guitar.TuningChangeListener;

public class LegacyGuitarActivity extends Activity {

    static final int DIALOG_NUM_STRINGS = 0;
    static final int DIALOG_TUNING = 1;
    static final int DIALOG_FRET_RANGE = 2;

    private Fretboard _fretboard;

    @Override
    public void onSaveInstanceState(Bundle sis){

        sis.putInt("numStrings", this._fretboard.getNumberOfStrings());
        sis.putInt("firstFret", this._fretboard.getFirstDisplayFret());
        sis.putString("tuning", this._fretboard.getTuning());
        //TODO: erring out
        //sis.putIntArray("posMap", this._fretboard.getSelectedPositions());
        super.onSaveInstanceState(sis);


    }

    @Override
    public void onRestoreInstanceState(Bundle sis){

        super.onRestoreInstanceState(sis);



        if(sis != null){
            int numStrings = sis.getInt("numStrings");
            int firstFret = sis.getInt("firstFret");
            String tuning = sis.getString("tuning");
            int[] posMap = sis.getIntArray("posMap");

            this._fretboard.setNumberOfStringsNoDraw(numStrings);
            this._fretboard.setFirstDisplayFretNoDraw(firstFret);
            this._fretboard.setTuningNoDraw(tuning);
            //TODO: erring out
            //this._fretboard.setSelectedPositionsNoDraw(posMap);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this._fretboard = new Fretboard(this);
        setContentView(this._fretboard);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fretboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //handle item selection
        switch(item.getItemId()){
            case R.id.change_tuning:
                showDialog(DIALOG_TUNING);
                return true;
            case R.id.set_num_strings:
                showDialog(DIALOG_NUM_STRINGS);
                return true;
            case R.id.set_fret_range:
                showDialog(DIALOG_FRET_RANGE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected Dialog onCreateDialog(int id){
        Dialog dialog = null;

        switch(id){
            case DIALOG_NUM_STRINGS:
                //define dialog

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Number of Strings");
                StringNumberChangeListener listener = new StringNumberChangeListener(this);
                builder.setSingleChoiceItems(Fretboard.stringNumOpts, -1, listener);

                dialog = builder.create();
                listener.setSender(dialog);

                break;
            case DIALOG_TUNING:
                //define dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Enter Six String Tuning");

                final EditText input = new EditText(this);
                input.setText(this._fretboard.getTuning());
                TuningChangeListener listener1 = new TuningChangeListener(this, input);

                builder1.setView(input);

                builder1.setPositiveButton("OK", listener1);
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //cancelled
                    }
                });

                dialog = builder1.create();
                break;
            case DIALOG_FRET_RANGE:
                //define dialog
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Select First Display Fret");
                FretRangeChangeListener listener2 = new FretRangeChangeListener(this);
                builder2.setSingleChoiceItems(Fretboard.fretRangeOpts, -1, listener2);

                dialog = builder2.create();
                listener2.setSender(dialog);
                break;
        }

        return dialog;
    }

    public void setNumberStrings(int numStrings) {

        this._fretboard.setNumberOfStrings(numStrings);
    }

    public void setFretRange(int firstFret) {

        this._fretboard.setFirstDisplayFret(firstFret);
    }

    public String changeTuning(String tuningValues) {
        return this._fretboard.changeTuning(tuningValues);
    }

    public void setSelectedPositions(int[] selectedPositions){
        this._fretboard.setSelectedPositions(selectedPositions);
    }
}
