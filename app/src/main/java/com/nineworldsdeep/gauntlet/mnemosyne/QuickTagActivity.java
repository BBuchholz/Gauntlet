package com.nineworldsdeep.gauntlet.mnemosyne;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListFile;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListItem;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyUtils;

public class QuickTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void launchQuickTagSongsActivity(View view) {


    }

    public void quickTagDailyToDo(View view) {

        String listName = SynergyUtils.getTimeStampedListName("DailyToDo");

        SynergyListFile slf = new SynergyListFile(listName);

        slf.loadItems();

        slf.add(new SynergyListItem(QuickTag.voiceMemo()));

        slf.save();

        Utils.toast(this, "Quick Tagged DailyToDo");
    }

}
