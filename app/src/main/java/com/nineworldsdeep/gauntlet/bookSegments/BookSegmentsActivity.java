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

import com.nineworldsdeep.gauntlet.DisplayMode;
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
                BookSegment selected = (BookSegment)lvItems.getItemAtPosition(idx);

                Intent intent = new Intent(view.getContext(),
                        BookSegmentsInputActivity.class);

                //set view mode
                intent.putExtra(BookSegmentsInputActivity.EXTRA_INPUT_MODE,
                        DisplayMode.View);

                //put extras for view fields
                intent.putExtra(AliasListActivity.EXTRA_ALIASLIST_ALIAS,
                        selected.getAlias());

                intent.putExtra(BookSegmentsInputActivity.EXTRA_PR,
                        selected.getPageRange());
                intent.putExtra(BookSegmentsInputActivity.EXTRA_SEG,
                        selected.getSegment());
                intent.putExtra(BookSegmentsInputActivity.EXTRA_KW,
                        selected.getKeyWords());
                intent.putExtra(BookSegmentsInputActivity.EXTRA_CF,
                        selected.getConferre());
                intent.putExtra(BookSegmentsInputActivity.EXTRA_NOTES,
                        selected.getNotes());


                startActivity(intent);
            }
        });
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
        intent.putExtra(BookSegmentsInputActivity.EXTRA_INPUT_MODE,
                DisplayMode.Create);
        startActivity(intent);
    }

}
