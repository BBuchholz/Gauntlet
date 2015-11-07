package com.nineworldsdeep.gauntlet.synergy.v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.synergy.v1.SynergyMasterListActivity;

import java.util.ArrayList;

public class SynergyListActivity extends AppCompatActivity {

    private SynergyListFile slf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_list);

        //set listName
        Intent intent = getIntent();

        String listName =
                intent.getStringExtra(
                        SynergyMainActivity.EXTRA_SYNERGYMAIN_LISTNAME);

        ListView lvItems =
                (ListView)findViewById(R.id.lvItems);

        readItems(lvItems, listName);
        setupListViewListener(lvItems);
    }

    private void setupListViewListener(final ListView lvItems) {

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view,
                                                   int position,
                                                   long id) {


                        //move to bottom of list
                        String removedItem = slf.remove(position);

                        if (removedItem.startsWith("completed={") && removedItem.endsWith("}")) {
                            //completed item being changed to incomplete
                            //removedItem = removedItem.replace("completed={", "");
                            //removedItem = removedItem.replace("}", "");
                            int beginIndex = removedItem.indexOf("{") + 1;
                            int endIndex = removedItem.lastIndexOf("}");
                            removedItem = removedItem.substring(beginIndex, endIndex);
                            slf.add(0, removedItem);
                        } else {
                            //incomplete item being changed to complete
                            removedItem = "completed={" + removedItem + "}";
                            slf.add(removedItem);
                        }

                        setListViewAdapter(lvItems);

                        writeItems();
                        //return true consumes the long click event (marks it handled)
                        return true;
                    }
                });
    }

    private void writeItems() {

        if(slf != null){
            slf.save();
        }
    }

    private void readItems(ListView lvItems, String listName) {

        slf = new SynergyListFile(listName);
        slf.loadItems();

        setListViewAdapter(lvItems);
    }

    private void setListViewAdapter(ListView lvItems){

        lvItems.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                slf.getItems()));
    }
}
