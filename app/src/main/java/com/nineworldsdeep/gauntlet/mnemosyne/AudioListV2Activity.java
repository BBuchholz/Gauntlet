package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Intent;
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

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AudioListV2Activity extends AppCompatActivity {

    private File mCurrentDir;
    List<FileListItem> mFileListItems;

    private static final int MENU_CONTEXT_SHA1_HASH_ID = 1;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_AUDIO = 2;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_VOICEMEMOS = 3;

    public static final String EXTRA_CURRENT_PATH =
            "com.nineworldsdeep.gauntlet.AUDIOLIST_CURRENT_PATH";

    // http://stackoverflow.com/questions/3014089/maintain-save-restore-scroll-position-when-returning-to-a-listview
    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;
//
//    NwdDb db;
//
//    private void assignDb(){
//
//        if(db == null || db.needsTestModeRefresh()){
//
//            if(Configuration.isInTestMode()){
//
//                //use external db in folder NWD/sqlite
//                db = new NwdDb(this, "test");
//
//            }else {
//
//                //use internal app db
//                db = new NwdDb(this);
//            }
//        }
//
//        db.open();
//    }
//  FOR SOME WEIRD REASON THIS CRASHES THE APP IF UNCOMMENTED
//  I CANNOT FIGURE IT OUT AS IMAGE LIST ACTIVITY IS IDENTICAL
//  AND DOESN'T CRASH WITH THIS LEFT IN?
//
//    @Override
//    protected void onPause() {
//
//        super.onPause();
//        db.close();
//    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list_v2);
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

            setTitle("NWD Audio");
        }

        refreshLayout();
    }
//
//    @Override
//    protected void onRestart(){
//        super.onRestart();
//        refreshLayout();
//    }

    private ListView getListView(){

        return (ListView) findViewById(R.id.lvItems);
    }

    private void refreshLayout() {

        ListView lvItems = getListView();

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

                FileListItem fli = mFileListItems.get(idx);
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
                            AudioListV2Activity.class);
                    intent.putExtra(
                            AudioListV2Activity.EXTRA_CURRENT_PATH,
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

        NwdDb db = NwdDb.getInstance(this);

        db.open();

        HashMap<String, String> map;

//        HashMap<String,String> dbPathToNameMap =
//                DisplayNameDbIndex.getPathToNameMap(db);

        mFileListItems =
                MnemoSyneUtils.getAudioListItems(NwdDb.getInstance(this),
                        mCurrentDir);

        for(FileListItem fli : mFileListItems){

            map = new HashMap<>();
            map.put("displayName", fli.getDisplayName());
            map.put("tags", fli.getTags());

            if(fli.getFile().isDirectory()){

                map.put("img", String.valueOf(R.mipmap.ic_nwd_junction));

            }else{

                map.put("img", String.valueOf(R.mipmap.ic_nwd_media));
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

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_AUDIO, Menu.NONE, "Move to audio");
            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_VOICEMEMOS, Menu.NONE, "Move to voicememos");
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

            case MENU_CONTEXT_MOVE_TO_FOLDER_AUDIO:

                moveToAudio(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_VOICEMEMOS:

                moveToVoiceMemos(info.position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void moveToVoiceMemos(int position) {

        moveFile(position, Configuration.getVoicememosDirectory());
    }

    private void moveToAudio(int position) {

        moveFile(position, Configuration.getAudioDirectory());
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
                //our audio files are not likely to change
                //and many in number (800+ with mp3's on my
                //test device), so this is a costly operation
                int count = //fhi.countAndStoreSHA1Hashes(f, 0, true);
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
