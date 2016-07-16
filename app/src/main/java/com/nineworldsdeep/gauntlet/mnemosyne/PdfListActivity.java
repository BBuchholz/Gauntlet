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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.DisplayNameDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.FileHashDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PdfListActivity extends AppCompatActivity {

    private File mCurrentDir;
    List<FileListItem> mFileListItems;

    private static final int MENU_CONTEXT_SHA1_HASH_ID = 1;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_PDFS = 2;
    private static final int MENU_CONTEXT_OPEN_EXTERNALLY = 3;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS = 4;

    public static final String EXTRA_CURRENT_PATH =
            "com.nineworldsdeep.gauntlet.IMAGELIST_CURRENT_PATH";

    // http://stackoverflow.com/questions/3014089/maintain-save-restore-scroll-position-when-returning-to-a-listview
    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;

    @Override
    protected void onPause() {

        super.onPause();

        NwdDb.getInstance(this).close();
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        mListState = state.getParcelable(LIST_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        NwdDb.getInstance(this).open();

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

        Intent i = getIntent();
        String s = i.getStringExtra(EXTRA_CURRENT_PATH);

        NwdDb.getInstance(this).open();

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

//        lvItems.setOnItemLongClickListener(
//                new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent,
//                                           View view,
//                                           int idx,
//                                           long id) {
//
//                FileListItem fli = mFileListItems.get(idx);
//                File f = fli.getFile();
//
//                if(f.exists() && f.isFile()){
//
//                    Intent target = new Intent(Intent.ACTION_VIEW);
//                    target.setDataAndType(Uri.fromFile(f),"application/pdf");
//                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//                    try{
//
//                        startActivity(target);
//
//                    }catch (ActivityNotFoundException ex){
//
//                        Utils.toast(PdfListActivity.this,
//                                "error opening file: " +
//                                f.getAbsolutePath());
//                    }
//
//                }
//
//                return true;
//            }
//        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        String name = mFileListItems.get(info.position).getDisplayName();

        boolean isDirectory =
                mFileListItems.get(info.position).getFile().isDirectory();

        menu.setHeaderTitle(name);

        menu.add(Menu.NONE, MENU_CONTEXT_SHA1_HASH_ID, Menu.NONE, "SHA1 Hash");

        if(!isDirectory) {

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_PDFS, Menu.NONE, "Move to pdfs");
            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS, Menu.NONE, "Move to Downloads");
            menu.add(Menu.NONE, MENU_CONTEXT_OPEN_EXTERNALLY, Menu.NONE, "Open externally");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case MENU_CONTEXT_SHA1_HASH_ID:

                computeSHA1Hash(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_PDFS:

                moveToPdfs(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS:

                moveToDownloads(info.position);

                return true;

            case MENU_CONTEXT_OPEN_EXTERNALLY:

                openExternally(info.position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void openExternally(int position){

        FileListItem fli = mFileListItems.get(position);
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
    }

    private void moveToPdfs(int position){

        moveFile(position, Configuration.getPdfDirectory());
    }

    private void moveToDownloads(int position){

        moveFile(position, Configuration.getDownloadDirectory());
    }

    private void moveFile(int position, File destinationDirectory){

        FileListItem fli = mFileListItems.get(position);
        File f = fli.getFile();

        String msg = "";

        if(f.exists()){

            try{

                NwdDb db = NwdDb.getInstance(this);

                File destination =
                        new File(destinationDirectory,
                                FilenameUtils.getName(f.getAbsolutePath()));

                MnemoSyneUtils.copyTags(f.getAbsolutePath(),
                        destination.getAbsolutePath(), db);

                MnemoSyneUtils.copyDisplayName(f.getAbsolutePath(),
                        destination.getAbsolutePath(), db);

                f.renameTo(destination);

                msg = "file moved";

            }catch (Exception ex){

                msg = "Error moving file: " + ex.getMessage();
            }

        }else{

            msg = "non existant path: " + f.getAbsolutePath();
        }

        Utils.toast(this, msg);
        refreshLayout();
    }


    private void loadItems() {

        ListView lvItems = (ListView) findViewById(R.id.lvItems);

        ArrayList<HashMap<String, String>> lstItems =
                new ArrayList<>();

        NwdDb db = NwdDb.getInstance(this);

        HashMap<String, String> map;

        HashMap<String,String> pathToTagString =
                TagDbIndex.importExportPathToTagStringMap(db);

        mFileListItems =
                MnemoSyneUtils.getDocumentListItems(pathToTagString,
                        mCurrentDir);

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

        /**
     * Computes and stores SHA1 hash for selected item if item is a file.
     * If item is a directory, computes and stores hashes for
     * all files within selected directory and all subfolders of the selected directory
     * @param position
     */
    private void computeSHA1Hash(int position) {

        FileListItem fli = mFileListItems.get(position);
        File f = fli.getFile();

        String msg = "";

        if(f.exists()){

            //FileHashIndex fhi = FileHashIndex.getInstance();

            try{

                NwdDb db = NwdDb.getInstance(this);

                //we call the count and store version that
                //ignores previously hashed files as
                //our pdf files are not likely to change
                int count =
                        FileHashDbIndex.countAndStoreSHA1Hashes(f, true, db);

                if(count != 1){

                    msg = count + " hashes stored";

                }else{

                    msg = count + " hash stored";
                }

            }catch(Exception ex){

                msg = ex.getMessage();
            }

        }else{

            msg = "NonExistantPath: " + f.getAbsolutePath();
        }

        Utils.toast(this, msg);
    }
}
