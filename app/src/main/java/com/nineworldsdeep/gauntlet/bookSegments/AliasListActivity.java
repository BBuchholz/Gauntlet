package com.nineworldsdeep.gauntlet.bookSegments;

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
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyListActivity;

public class AliasListActivity extends AppCompatActivity {

    public static final String EXTRA_ALIASLIST_ALIAS =
            "com.nineworldsdeep.gauntlet.ALIASLIST_ALIAS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alias_list);
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

                //get selected list name
                Alias selectedAlias = (Alias)lvItems.getItemAtPosition(idx);

                Intent intent = new Intent(view.getContext(),
                        BookSegmentsActivity.class);
                intent.putExtra(EXTRA_ALIASLIST_ALIAS, selectedAlias.getAlias());
                startActivity(intent);

            }
        });
    }

    private void loadItems(){

        ListView lvItems = (ListView)findViewById(R.id.lvItems);

        AliasListFile alf = new AliasListFile();
        alf.loadItems();

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                alf.getAliases()));
    }

    public void onAddItemClick(View view) {

        startActivity(new Intent(this, AliasInputActivity.class));
    }
}
