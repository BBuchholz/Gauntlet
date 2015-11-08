package com.nineworldsdeep.gauntlet.bookSegments;

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
import com.nineworldsdeep.gauntlet.Utils;

public class BookSegmentsActivity extends AppCompatActivity {

    private String aliasStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_segments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        aliasStr =
                intent.getStringExtra(
                        AliasListActivity.EXTRA_ALIASLIST_ALIAS);

        loadItems();
    }

    private void loadItems() {

        ListView lvItems = (ListView)findViewById(R.id.lvItems);

        BookSegmentsFile bsf = new BookSegmentsFile(aliasStr);
        bsf.loadItems();

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                bsf.getBookSegments()));
    }

    public void onAddItemClick(View view) {

        Intent intent = new Intent(view.getContext(),
                BookSegmentsInputActivity.class);
        intent.putExtra(AliasListActivity.EXTRA_ALIASLIST_ALIAS,
                aliasStr);
        startActivity(intent);

    }

}
