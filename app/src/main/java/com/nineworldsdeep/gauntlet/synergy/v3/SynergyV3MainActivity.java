package com.nineworldsdeep.gauntlet.synergy.v3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.ListEntry;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyListFile;
//import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SynergyV3MainActivity extends AppCompatActivity {

    public static final String EXTRA_SYNERGYMAIN_LISTNAME =
            "com.nineworldsdeep.gauntlet.SYNERGYMAINACTIVITY_LISTNAME";
    //public static boolean ORDER_BY_COUNT = false;
    public SynergyListOrdering ordering;
    private List<ListEntry> currentListEntries;

    //list state logic from: http://stackoverflow.com/questions/3014089/maintain-save-restore-scroll-position-when-returning-to-a-listview
    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mListState = state.getParcelable(LIST_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout();
        if (mListState != null)
            getListView().onRestoreInstanceState(mListState);
        mListState = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        mListState = getListView().onSaveInstanceState();
        state.putParcelable(LIST_STATE, mListState);
    }

    public void setOrdering(SynergyListOrdering ordering) {
        this.ordering = ordering;
        String orderName = "";

        switch (ordering){

            case ByNameDescending:
                orderName = "NameDesc";
                break;

            case ByNameAscending:
                orderName = "NameAsc";
                break;

            case ByItemCountDescending:
                orderName = "CountDesc";
                break;

            case ByItemCountAscending:
                orderName = "CountAsc";
                break;
        }

        setTitle("Synergy (" + orderName + ")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_synergy_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id == R.id.action_show_archive){
            startActivity(new Intent(this, SynergyArchivesActivity.class));
            return true;
        }

        if (id == R.id.action_show_templates){
            startActivity(new Intent(this, SynergyTemplatesActivity.class));
            return true;
        }

        if (id == R.id.action_toggle_sort){

            incrementOrderingSelection();
            refreshLayout();

            return true;
        }

        if(id == R.id.action_push_all){

            pushAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void incrementOrderingSelection() {

        switch (ordering){

            case ByNameDescending:
                setOrdering(SynergyListOrdering.ByNameAscending);
                break;

            case ByNameAscending:
                setOrdering(SynergyListOrdering.ByItemCountDescending);
                break;

            case ByItemCountDescending:
                setOrdering(SynergyListOrdering.ByItemCountAscending);
                break;

            case ByItemCountAscending:
                setOrdering(SynergyListOrdering.ByNameDescending);
                break;
        }
    }

    private void pushAll() {

        List<String> listNames = new ArrayList<>();

        for(ListEntry le : currentListEntries){

            if(Utils.containsTimeStamp(le.getListName())){
                listNames.add(le.getListName());
            }
        }

        SynergyUtils.pushAll(listNames);

        refreshLayout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Synergy V3");
        setOrdering(SynergyListOrdering.ByNameAscending);

        refreshLayout();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        refreshLayout();
    }

    private void refreshLayout(){

        ListView lvItems = getListView();

        readItems(lvItems);
        setupListViewListener(lvItems);
    }

    private ListView getListView() {

        return (ListView)findViewById(R.id.lvItems);
    }

    public void onAddItemClick(View view) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemText = itemText.trim();

        itemText = Utils.processName(itemText);

        if(!Utils.stringIsNullOrWhitespace(itemText)){

            SynergyListFile slf =
                    new SynergyListFile(itemText);
            slf.loadItems(); //just in case it already exists
            slf.save();
            etNewItem.setText("");
            readItems();
        }
    }

    private void setupListViewListener(final ListView lvItems) {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int idx,
                                    long id) {

                //get selected list name
                ListEntry selectedList = (ListEntry) lvItems.getItemAtPosition(idx);

                Intent intent = new Intent(view.getContext(),
                        SynergyListActivity.class);
                intent.putExtra(EXTRA_SYNERGYMAIN_LISTNAME,
                                selectedList.getListName());
                startActivity(intent);

            }
        });
    }

    private void readItems(){
        readItems((ListView)findViewById(R.id.lvItems));
    }

    private void readItems(ListView lvItems) {

        List<ListEntry> lst = SynergyUtils.getAllListEntries();

        if(ordering == SynergyListOrdering.ByItemCountDescending){

            Collections.sort(lst, new Comparator<ListEntry>() {

                public int compare(ListEntry one, ListEntry other) {

                    if(one.getItemCount() == other.getItemCount()){
                        return 0;
                    }

                    if (one.getItemCount() > other.getItemCount()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        }

        if(ordering == SynergyListOrdering.ByItemCountAscending){

            Collections.sort(lst, new Comparator<ListEntry>() {

                public int compare(ListEntry one, ListEntry other) {

                    if(one.getItemCount() == other.getItemCount()){
                        return 0;
                    }

                    if (one.getItemCount() < other.getItemCount()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        }

        if(ordering == SynergyListOrdering.ByNameDescending){

            //just invert the default
            Collections.reverse(lst);
        }

        if(ordering == SynergyListOrdering.ByNameAscending){

            //do nothing, it's the default order
        }


        currentListEntries = lst;

        lvItems.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        currentListEntries));

//        lvItems.setAdapter(
//                new ArrayAdapter<>(this,
//                        android.R.layout.simple_list_item_1,
//                        SynergyUtils.getAllListNames()));
    }

}
