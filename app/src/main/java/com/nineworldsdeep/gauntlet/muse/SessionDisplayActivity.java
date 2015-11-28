package com.nineworldsdeep.gauntlet.muse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;

public class SessionDisplayActivity extends AppCompatActivity {

    public static final String EXTRA_SESSION_NAME =
            "com.nineworldsdeep.gauntlet.SessionDisplayActivity_SESSION_NAME";

    private SessionListFile slf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String sessionName = i.getStringExtra(EXTRA_SESSION_NAME);

        slf = new SessionListFile(sessionName);
        slf.loadItems();

        loadItems();
    }

    private void loadItems() {

        ListView lvItems = (ListView) findViewById(R.id.lvItems);

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                slf.getItems()));
    }

}
