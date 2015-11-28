package com.nineworldsdeep.gauntlet.muse.keys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.muse.MuseUtils;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyListFile;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

public class MuseKeyboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muse_keyboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_muse_keyboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_keyboard_session_notes) {

            promptConfirmSessionNotes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptConfirmSessionNotes() {

        DynamicHeightKeyboard kb =
                (DynamicHeightKeyboard) findViewById(R.id.dynamicKeyboard);

        //TODO: get notes from kb and convert to string (MuseUtils?)
        //then add to session notes timestamped list (after prompt)


        final String sessionNote = MuseUtils.toNoteString(kb.getNoteArray()) +
               " " + SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss(true);

        final String currentSession = MuseUtils.getCurrentSessionName();

        Utils.toast(this, sessionNote);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Archive Tasks")
                .setMessage("Add [" + sessionNote + "] to " + currentSession + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SynergyListFile slf = new SynergyListFile(currentSession);
                        slf.loadItems();
                        slf.add(sessionNote);
                        slf.save();
                        Utils.toast(getApplicationContext(), "note array stored");
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
