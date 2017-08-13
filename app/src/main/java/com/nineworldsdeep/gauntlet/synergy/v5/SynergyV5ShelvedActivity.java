package com.nineworldsdeep.gauntlet.synergy.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;

public class SynergyV5ShelvedActivity extends AppCompatActivity {

    public static final String EXTRA_ARCHIVENAME =
            "com.nineworldsdeep.gauntlet.SYNERGY_ARCHIVENAME";

    private static final int MENU_CONTEXT_ACTIVATE_LIST = 1;

    private ArrayList<String> mArchivedListNames =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_archives);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView lvItems =
            (ListView)findViewById(R.id.lvItems);
        registerForContextMenu(lvItems);

        readItems();
        //setupListViewListener();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        String title = mArchivedListNames.get(info.position);

        menu.setHeaderTitle(title);

        menu.add(Menu.NONE, MENU_CONTEXT_ACTIVATE_LIST,
                    Menu.NONE, "Activate List");


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case MENU_CONTEXT_ACTIVATE_LIST:

                activateList(info.position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void activateList(int position) {

        if(position < mArchivedListNames.size()){

            SynergyV5List synLst =
                    new SynergyV5List(mArchivedListNames.get(position));

            synLst.activate();

            synLst.save(this, NwdDb.getInstance(this));
            //synLst.sync(this, NwdDb.getInstance(this));

            readItems();
        }
    }


//    private void setupListViewListener() {
//
//        final ListView lvItems =
//                (ListView)findViewById(R.id.lvItems);
//
//        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent,
//                                    View view,
//                                    int idx,
//                                    long id) {
//
//                //get selected archive name
//                String selectedArchive =
//                        (String)lvItems.getItemAtPosition(idx);
//
//                Intent intent = new Intent(view.getContext(),
//                        SynergyV5ArchiveActivity.class);
//                intent.putExtra(EXTRA_ARCHIVENAME, selectedArchive);
//                startActivity(intent);
//
//            }
//        });
//    }

    private void readItems() {

        mArchivedListNames =
                SynergyV5Utils.getAllArchiveNames(this,
                        NwdDb.getInstance(this));

        ListView lvItems =
                (ListView)findViewById(R.id.lvItems);

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                mArchivedListNames));
    }

}
