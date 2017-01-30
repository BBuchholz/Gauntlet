package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;
import java.util.ArrayList;

public class MnemosyneV5ScanActivity extends AppCompatActivity {

    private static final int SELECT_DIRECTORY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemosyne_v5_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NwdDb.getInstance(this).open();

        if(Configuration.getLocalMediaDevice(this,
                        NwdDb.getInstance(this)) == null){

            promptSetLocalDeviceDescription();

        }else{

            refreshLayout();
        }
    }

    private void promptSetLocalDeviceDescription(){

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt, null);

        TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
        tv.setText("Enter Local Device Name: ");

        android.app.AlertDialog.Builder alertDialogBuilder =
                new android.app.AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                // get list name from userInput and move
                                String deviceName =
                                        userInput.getText().toString();

                                deviceName = deviceName.trim().toLowerCase();

                                if(!Utils.stringIsNullOrWhitespace(deviceName)) {

                                    Configuration.ensureLocalMediaDevice(
                                            MnemosyneV5ScanActivity.this,
                                            NwdDb.getInstance(MnemosyneV5ScanActivity.this),
                                            deviceName);

                                    Utils.toast(getApplicationContext(), "stored.");

                                    refreshLayout();

                                }else{

                                    Utils.toast(getApplicationContext(), "empty name, ignored.");
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();

                                refreshLayout();
                            }
                        });

        // create alert dialog
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void refreshLayout(){


        populateSpinnerMediaDevice();
        populateSpinnerMediaRoot();
        populateSpinnerSourceSelectDbFs();
        populateSpinnerFileTypes();
    }


    @Override
    protected void onResume() {

        super.onResume();
        NwdDb.getInstance(this).open();
    }

    @Override
    protected void onPause() {

        super.onPause();
        NwdDb.getInstance(this).close();
    }

    private void populateSpinnerMediaDevice() {

        Spinner sp = (Spinner)this.findViewById(R.id.spMediaDevice);

        ArrayList<MediaDevice> lst = new ArrayList<>();

        MediaDevice md = Configuration.getLocalMediaDevice(
                this, NwdDb.getInstance(this));

        if(md != null){

            lst.add(md);
        }

        ArrayAdapter<MediaDevice> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lst);

        sp.setAdapter(adapter);
    }

    private void populateSpinnerMediaRoot() {

        Spinner sp = (Spinner)this.findViewById(R.id.spMediaRoot);

        ArrayList<String> lst = new ArrayList<>();
        lst.add("/NWD/");
        lst.add("/NWD-AUX/");
        lst.add("/NWD-MEDIA/");
        lst.add("/NWD-SYNC/");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lst);

        sp.setAdapter(adapter);
    }

    private void populateSpinnerSourceSelectDbFs() {

        Spinner sp = (Spinner)this.findViewById(R.id.spSourceSelectDbFs);

        ArrayList<String> lst = new ArrayList<>();
        lst.add("DB");
        lst.add("FS");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lst);

        sp.setAdapter(adapter);
    }

    private void populateSpinnerFileTypes() {

        Spinner sp = (Spinner)this.findViewById(R.id.spFileTypes);

        ArrayList<String> lst = new ArrayList<>();
        lst.add(".wav");
        lst.add(".png");
        lst.add(".txt");
        lst.add(".xml");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lst);

        sp.setAdapter(adapter);
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mnemosyne_v5_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){


        int id = item.getItemId();

        if (id == R.id.action_go_to_home_screen){

            NavigateActivityCommand.navigateTo(
                    HomeListActivity.class, this
            );
            return true;
        }

        if (id == R.id.action_add_media_root){

            String path = "media/root/path/goes/here/";

            Intent i = new Intent(this, FilePickerActivity.class);

            i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
            i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);

            i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory());

            startActivityForResult(i, SELECT_DIRECTORY_CODE);

            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_DIRECTORY_CODE &&
                resultCode == Activity.RESULT_OK) {

            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {

                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    ClipData clip = data.getClipData();

                    if (clip != null) {

                        for (int i = 0; i < clip.getItemCount(); i++) {

//                            Uri uri = clip.getItemAt(i).getUri();
//
//                            //handle multiples when and if implemented
                        }
                    }
                }

            } else {

                Uri uri = data.getData();

                File f = new File(uri.getPath());
                Utils.toast(this, "Selected path: " + f.getAbsolutePath());
            }

        } else {

            Utils.toast(this, "Nothing selected...");
        }

    }

}
