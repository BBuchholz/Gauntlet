package com.nineworldsdeep.gauntlet.muse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;

import java.io.File;

public class SessionListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadItems();
        setupListViewListener();
    }

    private void setupListViewListener() {
        final ListView lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int idx,
                                    long id) {

                String sessionName = (String) lvItems.getItemAtPosition(idx);

                Intent intent = new Intent(view.getContext(),
                        SessionDisplayActivity.class);
                intent.putExtra(
                        SessionDisplayActivity.EXTRA_SESSION_NAME,
                        sessionName
                );
                startActivity(intent);

            }
        });
    }

    private void loadItems() {

        ListView lvItems = (ListView) findViewById(R.id.lvItems);

        lvItems.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        MuseUtils.getSessionNames())
        );
    }

}
