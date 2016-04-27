package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageListV2Activity extends AppCompatActivity {

    private File mCurrentDir;
    List<FileListItem> mFileListItems;


    private static final int MENU_CONTEXT_SHA1_HASH_ID = 1;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_IMAGES = 2;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_CAMERA = 3;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_SCREENSHOTS = 4;

    public static final String EXTRA_CURRENT_PATH =
            "com.nineworldsdeep.gauntlet.IMAGELIST_CURRENT_PATH";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list_v2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String s = i.getStringExtra(EXTRA_CURRENT_PATH);

        mCurrentDir = null;

        if(s != null){

            mCurrentDir = new File(s);

            if(!mCurrentDir.exists()){
                mCurrentDir = null;
            }
        }

        if(mCurrentDir != null){

            setTitle(mCurrentDir.getName());

        }else{

            setTitle("NWD Images");
        }

        refreshLayout();
    }

    private void refreshLayout() {

        ListView lvItems =
                (ListView) findViewById(R.id.lvItems);

        loadItems();
        setupListViewListener();
        registerForContextMenu(lvItems);
    }

    private void setupListViewListener() {

        final ListView lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int idx,
                                    long id) {

                FileListItem fli = (FileListItem) mFileListItems.get(idx);
                File f = fli.getFile();

                if(f.exists() && f.isFile()){

                    Intent intent = new Intent(view.getContext(),
                            ImageDisplayActivity.class);
                    intent.putExtra(
                            ImageDisplayActivity.EXTRA_IMAGEPATH,
                            f.getAbsolutePath()
                    );
                    startActivity(intent);

                }else if(f.exists() && f.isDirectory()){

                    Intent intent = new Intent(view.getContext(),
                            ImageListV2Activity.class);
                    intent.putExtra(
                            ImageListV2Activity.EXTRA_CURRENT_PATH,
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

        ArrayList<HashMap<String, String>> lstItems =
                new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map;

        mFileListItems = MnemoSyneUtils.getImageListItems(mCurrentDir);

        for(FileListItem fli : mFileListItems){

            map = new HashMap<>();
            map.put("displayName", fli.getDisplayName());
            map.put("tags", fli.getTags());

            if(fli.getFile().isDirectory()){

                map.put("img", String.valueOf(R.mipmap.ic_nwd_multinode));

            }else{

                map.put("img", String.valueOf(R.mipmap.ic_nwd_singlenode));
            }

            lstItems.add(map);
        }

        SimpleAdapter saItems =
                new SimpleAdapter(
                        this.getBaseContext(),
                        lstItems,
                        R.layout.file_list_item,
                        new String[] {"img",
                                      "displayName",
                                      "tags"},
                        new int[] {R.id.img,
                                   R.id.display_name,
                                   R.id.tags});

        lvItems.setAdapter(saItems);
    }

}
