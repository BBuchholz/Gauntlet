package com.nineworldsdeep.gauntlet.mnemosyne;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.archivist.v5.async.AsyncCommandHiveImportArchivistV5FromXml;
import com.nineworldsdeep.gauntlet.archivist.v5.async.AsyncCommandHiveExportArchivistV5ToXml;
import com.nineworldsdeep.gauntlet.core.AsyncListBasedActivity;
import com.nineworldsdeep.gauntlet.core.AsyncCommand;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;

import java.util.ArrayList;

public class TransferActivity extends AsyncListBasedActivity
        implements IStatusResponsive {

    private ArrayList<AsyncCommand> cmds =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected ListView getListView() {

        return (ListView)findViewById(R.id.lvItems);
    }

    @Override
    protected void setupListViewListener(ListView lvItems) {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id) {

                AsyncCommand cmd = cmds.get(position);
                cmd.executeCommand();
            }
        });
    }

    @Override
    protected ListAdapter loadItems() {

        //SEE AudioListV2Activity FOR EXAMPLE OF MORE COMPLEX IMPLEMENTATION
        cmds.clear();

//        cmds.add(new AsyncCommandImportSynergyV5FromXml(this));
//        cmds.add(new AsyncCommandImportMnemosyneV5FromXml(this));

        cmds.add(new AsyncCommandHiveImportSynergyV5FromXml(this));
        cmds.add(new AsyncCommandHiveImportMnemosyneV5FromXml(this));
        cmds.add(new AsyncCommandHiveImportArchivistV5FromXml(this));

//        cmds.add(new AsyncCommandImportMnemosyneV3toV4(this));
//        cmds.add(new AsyncCommandImportMnemosyneV4toV5(this));
//        cmds.add(new AsyncCommandImportSynergyV3ToV5(this));

        cmds.add(new AsyncCommandExportDb(this));

//        cmds.add(new AsyncCommandBridgeTables(this));

//        cmds.add(new AsyncCommandExportSynergyV5ToXml(this));
//        cmds.add(new AsyncCommandExportMnemosyneV5ToXml(this));

        cmds.add(new AsyncCommandHiveExportSynergyV5ToXml(this));
        cmds.add(new AsyncCommandHiveExportMnemosyneV5ToXml(this));
        cmds.add(new AsyncCommandHiveExportArchivistV5ToXml(this));

        return new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, cmds);
    }

    @Override
    protected TextView getStatusView() {

        return (TextView)findViewById(R.id.tvStatus);
    }

    @Override
    public Activity getAsActivity() {
        return this;
    }

    @Override
    public void onPostExecute() {

    }
}
