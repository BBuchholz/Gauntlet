package com.nineworldsdeep.gauntlet.hive;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class HiveRootActivity extends AppCompatActivity {

    int hiveRootId = -1;
    String hiveRootName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hive_root);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String idString = getIntent().getStringExtra(
                HiveMainActivity.EXTRA_HIVE_ROOT_ID
        );

        String nameString = getIntent().getStringExtra(
                HiveMainActivity.EXTRA_HIVE_ROOT_NAME
        );

        hiveRootId = Integer.parseInt(idString);
        hiveRootName = nameString;

        Utils.toast(this, "received extras, id: " + hiveRootId + ", name: " + hiveRootName);
    }

}
