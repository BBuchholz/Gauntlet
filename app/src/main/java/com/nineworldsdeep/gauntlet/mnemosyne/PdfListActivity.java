package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PdfListActivity extends AppCompatActivity {

    private File mCurrentDir;
    List<FileListItem> mFileListItems;

    public static final String EXTRA_CURRENT_PATH =
            "com.nineworldsdeep.gauntlet.IMAGELIST_CURRENT_PATH";

    // http://stackoverflow.com/questions/3014089/maintain-save-restore-scroll-position-when-returning-to-a-listview
    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;

    private NwdDb db;

    private void assignDb(){

        if(db == null || db.needsTestModeRefresh()){

            if(Configuration.isInTestMode()){

                //use external db in folder NWD/sqlite
                db = new NwdDb(this, "test");

            }else {

                //use internal app db
                db = new NwdDb(this);
            }
        }

        db.open();
    }

    @Override
    protected void onPause() {

        super.onPause();
        db.close();
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mListState = state.getParcelable(LIST_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        assignDb();
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

    private ListView getListView() {

        return (ListView)findViewById(R.id.lvItems);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assignDb();

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

            setTitle("NWD Pdfs");
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                FileListItem fli = mFileListItems.get(position);
                File f = fli.getFile();

                if(f.exists() && f.isDirectory()){

                    Intent intent = new Intent(view.getContext(),
                            PdfListActivity.class);

                    intent.putExtra(
                            PdfListActivity.EXTRA_CURRENT_PATH,
                            f.getAbsolutePath()
                    );

                    startActivity(intent);

                }else if(f.exists() && f.isFile()){

                    Utils.toast(PdfListActivity.this,
                            "long press to open file externally");
                }
            }
        });

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view,
                                           int idx,
                                           long id) {

                FileListItem fli = mFileListItems.get(idx);
                File f = fli.getFile();

                if(f.exists() && f.isFile()){

                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.setDataAndType(Uri.fromFile(f),"application/pdf");
                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    try{

                        startActivity(target);

                    }catch (ActivityNotFoundException ex){

                        Utils.toast(PdfListActivity.this,
                                "error opening file: " +
                                f.getAbsolutePath());
                    }

                }

                return true;
            }
        });
    }

    private void loadItems() {

        ListView lvItems = (ListView) findViewById(R.id.lvItems);

        ArrayList<HashMap<String, String>> lstItems =
                new ArrayList<>();

        HashMap<String, String> map;

        mFileListItems = MnemoSyneUtils.getDocumentListItems(db, mCurrentDir);

        for(FileListItem fli : mFileListItems){

            map = new HashMap<>();
            map.put("displayName", fli.getDisplayName());
            map.put("tags", fli.getTags());

            if(fli.getFile().isDirectory()){

                map.put("img", String.valueOf(R.mipmap.ic_nwd_junction));

            }else{

                map.put("img", String.valueOf(R.mipmap.ic_nwd_synergy_list));
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
