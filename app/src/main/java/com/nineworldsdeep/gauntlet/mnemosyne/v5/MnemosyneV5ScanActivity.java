package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;

import java.util.ArrayList;

public class MnemosyneV5ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemosyne_v5_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateSpinnerMediaDevice();
        populateSpinnerMediaRoot();
        populateSpinnerSourceSelectDbFs();
        populateSpinnerFileTypes();
    }

    private void populateSpinnerMediaDevice() {

        Spinner sp = (Spinner)this.findViewById(R.id.spMediaDevice);

        ArrayList<String> lst = new ArrayList<>();
        lst.add("logos");
        lst.add("realm");
        lst.add("galaxy-a");
        lst.add("main laptop");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
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

            Utils.toast(this, "Selected Path: media/root/path/goes/here/");

            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
