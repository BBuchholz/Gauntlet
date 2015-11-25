package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.io.File;

public class AudioListActivity extends AppCompatActivity {

    private File currentDir;

    public static final String EXTRA_CURRENTPATH =
            "com.nineworldsdeep.gauntlet.AUDIOLIST_CURRENT_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i = getIntent();
        String s = i.getStringExtra(EXTRA_CURRENTPATH);

        currentDir = null;

        if(s != null){

            currentDir = new File(s);

            if(!currentDir.exists()){
                currentDir = null;
            }
        }

        if(currentDir == null){

            currentDir = Configuration.getAudioDirectory();
        }

        Utils.toast(this, currentDir.getAbsolutePath());

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

                FileListItem fli = (FileListItem) lvItems.getItemAtPosition(idx);
                File f = fli.getFile();

                if(f.exists() && f.isFile()){

                    Intent intent = new Intent(view.getContext(),
                            AudioDisplayActivity.class);
                    intent.putExtra(
                            AudioDisplayActivity.EXTRA_AUDIOPATH,
                            f.getAbsolutePath()
                    );
                    startActivity(intent);

                }else if(f.exists() && f.isDirectory()){

                    Intent intent = new Intent(view.getContext(),
                            AudioListActivity.class);
                    intent.putExtra(
                            AudioListActivity.EXTRA_CURRENTPATH,
                            f.getAbsolutePath()
                    );
                    startActivity(intent);
                }else{

                    Utils.toast(view.getContext(), f.getAbsolutePath());
                }
            }
        });
    }

    private void loadItems() {

        ListView lvItems = (ListView) findViewById(R.id.lvItems);

        lvItems.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        MnemoSyneUtils.getAudioListItems(currentDir))
        );
    }
}
