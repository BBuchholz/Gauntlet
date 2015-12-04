package com.nineworldsdeep.gauntlet.mnemosyne;

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

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class AudioListActivity extends AppCompatActivity {

    private static final int MENU_CONTEXT_SHA1_HASH_ID = 1;
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

        ListView lvItems =
                (ListView) findViewById(R.id.lvItems);

        loadItems();
        setupListViewListener();
        registerForContextMenu(lvItems);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        String name =
                getItemAtPosition(info.position).getFile().getName();

        menu.setHeaderTitle(name);

        menu.add(Menu.NONE, MENU_CONTEXT_SHA1_HASH_ID, Menu.NONE, "SHA1 Hash");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case MENU_CONTEXT_SHA1_HASH_ID:

                computeSHA1Hash(info.position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void computeSHA1Hash(int position) {

        FileListItem fli = getItemAtPosition(position);
        File f = fli.getFile();

        if(f.exists() && f.isDirectory()){

            int count = 0;

            FileHashIndex fhi = FileHashIndex.getInstance();

            try {

                for (File f2 : f.listFiles()) {

                    String hash = Utils.computeSHA1(f2.getAbsolutePath());
                    fhi.storeHash(f2.getAbsolutePath(), hash);
                    count++;
                }

                fhi.save();
                String msg = count + " file hashes stored";

                Utils.toast(this, msg);

            }catch(Exception ex){

                Utils.toast(this, ex.getMessage());
            }

        }else if(f.exists() && f.isFile()){

            try {

                String hash = Utils.computeSHA1(f.getAbsolutePath());
                FileHashIndex fhi = FileHashIndex.getInstance();
                fhi.storeHash(f.getAbsolutePath(), hash);
                fhi.save();

                Utils.toast(this, "hash stored for file");

            } catch (Exception e) {

                Utils.toast(this, e.getMessage());
            }

        }else{

            Utils.toast(this, "NonExistantPath: " + f.getAbsolutePath());
        }
    }

    private FileListItem getItemAtPosition(int position){

        ListView lvItems =
                (ListView) findViewById(R.id.lvItems);

        return (FileListItem) lvItems.getItemAtPosition(position);
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

        boolean isTopFolder =
                currentDir.equals(Configuration.getAudioDirectory());

        lvItems.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        MnemoSyneUtils.getAudioListItems(currentDir, isTopFolder))
        );
    }
}
