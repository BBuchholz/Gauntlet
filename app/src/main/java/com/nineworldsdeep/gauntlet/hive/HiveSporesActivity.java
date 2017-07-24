package com.nineworldsdeep.gauntlet.hive;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.mnemosyne.MnemoSyneUtils;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.AudioDisplayV5Activity;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaListItem;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaPlayerSingletonV5;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.UtilsMnemosyneV5;
import com.nineworldsdeep.gauntlet.sqlite.FileHashDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static com.nineworldsdeep.gauntlet.hive.HiveLobesActivity.EXTRA_HIVE_LOBE_TYPE;
import static com.nineworldsdeep.gauntlet.hive.HiveRootsActivity.EXTRA_HIVE_ROOT_ID;
import static com.nineworldsdeep.gauntlet.hive.HiveRootsActivity.EXTRA_HIVE_ROOT_NAME;

public class HiveSporesActivity extends AppCompatActivity {

    Lobe mLobe;


    /**
     * This field should be made private, so it is hidden from the SDK.
     *
     * adapted from ListActivity source
     * {@hide}
     */
    protected ListAdapter mAdapter;

    /**
     * This field should be made private, so it is hidden from the SDK.
     *
     * adapted from ListActivity source
     * {@hide}
     */
    protected ListView mList;

    private Handler mHandler = new Handler();
    private boolean mFinishedStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list_v52);

        String idString = getIntent().getStringExtra(
                EXTRA_HIVE_ROOT_ID
        );

        String nameString = getIntent().getStringExtra(
                EXTRA_HIVE_ROOT_NAME
        );

        String lobeTypeString = getIntent().getStringExtra(
                EXTRA_HIVE_LOBE_TYPE
        );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Spores");

        mLobe = new Lobe();

        mLobe.setHiveRootId(Integer.parseInt(idString));
        mLobe.setHiveRootName(nameString);
        mLobe.setLobeType(LobeType.valueOf(lobeTypeString));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                HashMap<String, String> extraKeyToValue = new HashMap<>();

                extraKeyToValue.put(EXTRA_HIVE_ROOT_ID,
                        Integer.toString(mLobe.getHiveRootId()));

                extraKeyToValue.put(EXTRA_HIVE_ROOT_NAME,
                        mLobe.getHiveRootName());

                NavigateActivityCommand.navigateTo(
                        extraKeyToValue,
                        HiveLobesActivity.class,
                        HiveSporesActivity.this);
            }
        });
    }

    private void refreshLayout() {

        setListAdapter(new ArrayAdapter<MediaListItem>(
                            this,
                            android.R.layout.simple_list_item_1,
                            new ArrayList<MediaListItem>()){

            @Override
            public View getView(int pos, View convertView, ViewGroup parent){

                if(convertView==null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView=vi.inflate(R.layout.media_list_item, null);
                }

                TextView tvDisplayName = (TextView)convertView.findViewById(R.id.display_name);
                TextView tvTags = (TextView)convertView.findViewById(R.id.tags);
                ImageView ivImage = (ImageView)convertView.findViewById(R.id.img);

                MediaListItem mli = getItem(pos);

                if(mli.getFile() != null){

                    tvDisplayName.setText(mli.getFile().getName());
                    tvTags.setText(mli.getTags());

                    if(mli.getFile().isDirectory()){

                        ivImage.setImageResource(R.mipmap.ic_nwd_junction);

                    }else{

                        ivImage.setImageResource(R.mipmap.ic_nwd_media);
                    }

                }else{

                    tvDisplayName.setText("dummy file name");
                    tvTags.setText("test tags, testing, test dummy item");

                    ivImage.setImageResource(R.mipmap.ic_nwd_media);
                }

                return convertView;
            }
        });

        new AsyncLoadItems().execute(mLobe);
    }

    /**
     * Provide the cursor for the list view.
     *
     * adapted from ListActivity source
     * @param adapter
     */
    public void setListAdapter(ListAdapter adapter) {
        synchronized (this) {
            ensureList();
            mAdapter = adapter;
            mList.setAdapter(adapter);
        }
    }

    /**
     * Get the ListAdapter associated with this activity's ListView.
     *
     * adapted from ListActivity source
     */
    public ListAdapter getListAdapter() {
        return mAdapter;
    }

    /**
     * adapted from ListActivity source
     */
    private void ensureList() {
        if (mList != null) {
            return;
        }
        setContentView(R.layout.activity_audio_list_v52);
    }

    /**
     * Ensures the list view has been created before Activity restores all
     * of the view states.
     *
     * adapted from ListActivity source
     *
     */
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        ensureList();
        super.onRestoreInstanceState(state);
    }


    /**
     * Updates the screen state (current list and other views) when the
     * content changes.
     *
     * adapted from ListActivity source
     */
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        //View emptyView = findViewById(com.android.internal.R.id.empty);
        mList = (ListView)findViewById(R.id.lvList);
        if (mList == null) {
            throw new RuntimeException(
                    "Your content must have a ListView whose id attribute is " +
                    "'android.R.id.list'");
        }

        mList.setOnItemClickListener(mOnClickListener);
        if (mFinishedStart) {
            setListAdapter(mAdapter);
        }
        mHandler.post(mRequestFocus);
        mFinishedStart = true;
    }

    /**
     * adapted from ListActivity source
     */
    private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mList.focusableViewAvailable(mList);
        }
    };

    /**
     * adapted from ListActivity source
     */
    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRequestFocus);
        super.onDestroy();
    }

    /**
     * Get the activity's list view widget.
     *
     * adapted from ListActivity source
     */
    public ListView getListView() {
        ensureList();
        return mList;
    }

    class ProgressWrapper{

        private MediaListItem mli;
        private String status;

        public ProgressWrapper(MediaListItem mli, String status){

            this.mli = mli;
            this.status = status;
        }

        public MediaListItem getMediaListItem() {
            return mli;
        }

        public String getStatus() {
            return status;
        }
    }

    class AsyncLoadItems extends AsyncTask<Lobe, ProgressWrapper, String> {


        @Override
        protected void onPreExecute(){

            updateStatus("loading...");
        }

        @Override
        protected String doInBackground(Lobe... lobes) {

            int count = 0;
            int total;

            String result = "finished loading successfully";

            try{

                NwdDb db = NwdDb.getInstance(HiveSporesActivity.this);

                db.open();

                ArrayList<MediaListItem> items =
                        UtilsHive.getMediaListItems(lobes[0]);

                total = items.size();

                for (MediaListItem mli : items) {

                    count++;

                    if(mli.isFile()) {

                        mli.hashMedia();
                        db.sync(mli.getMedia());
                    }


                    String msg = "Still loading... (" + count + " of " + total + " items loaded)";

                    publishProgress(new ProgressWrapper(mli, msg));
                }

            }catch (Exception ex){

                result = "Error loading items: " + ex.toString();
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(ProgressWrapper... items) {

            ProgressWrapper pw = items[0];
            ((ArrayAdapter<MediaListItem>)getListAdapter()).add(
                    pw.getMediaListItem());

            updateStatus(pw.getStatus());
        }

        @Override
        protected void onPostExecute(String result) {

            updateStatus(result);

            registerForContextMenu(getListView());
        }
    }

    private TextView getTextViewStatus() {

        return (TextView)findViewById(R.id.tvStatus);
    }

    private void updateStatus(String statusText){

        getTextViewStatus().setText(statusText);
    }

    /**
     * This method will be called when an item in the list is selected.
     * Subclasses should override. Subclasses can call
     * getListView().getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * adapted from ListActivity source
     *
     * @param l The ListView where the click happened
     * @param view The view that was clicked within the ListView
     * @param idx The position of the view in the list
     * @param id The row id of the item that was clicked
     */
    protected void onListItemClick(ListView l, View view, int idx, long id) {

        MediaListItem mli = getItem(idx);
        File f = mli.getFile();

        if(f != null && f.exists() && f.isFile()){

            Intent intent = new Intent(view.getContext(),
                    AudioDisplayV5Activity.class);

            intent.putExtra(
                    AudioDisplayV5Activity.EXTRA_AUDIO_PATH,
                    f.getAbsolutePath()
            );

            startActivity(intent);

        }else{

            Utils.toast(this, "not found");
        }
    }

    private MediaListItem getItem(int idx) {

        return ((ArrayAdapter<MediaListItem>)getListAdapter()).getItem(idx);
    }

    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
        {
            onListItemClick((ListView)parent, v, position, id);
        }
    };

    @Override
    public void onBackPressed() {

        HashMap<String, String> extraKeyToValue = new HashMap<>();

        extraKeyToValue.put(EXTRA_HIVE_ROOT_ID,
                Integer.toString(mLobe.getHiveRootId()));

        extraKeyToValue.put(EXTRA_HIVE_ROOT_NAME,
                mLobe.getHiveRootName());

        NavigateActivityCommand.navigateTo(
                extraKeyToValue,
                HiveLobesActivity.class,
                HiveSporesActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio_list_v5, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){


        //int id = item.getItemId();

        switch (item.getItemId()){

            case R.id.action_go_to_home_screen:

                NavigateActivityCommand.navigateTo(
                        HomeListActivity.class, this
                );

                return true;

            case R.id.action_queue_folder_to_playlist:

                MediaPlayerSingletonV5 player =
                        MediaPlayerSingletonV5.getInstance();

                ArrayAdapter<MediaListItem> adapter =
                        (ArrayAdapter<MediaListItem>)getListAdapter();

                ArrayList<MediaListItem> lst = new ArrayList<>();

                for(int i = 0; i < adapter.getCount(); i++){

                    lst.add(adapter.getItem(i));
                }

                player.queue(lst);

                NavigateActivityCommand.navigateTo(
                        AudioDisplayV5Activity.class, this);

                return true;

            case R.id.action_export_all_to_xml:

                exportAllToXml();

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        NwdDb.getInstance(this).open();
        refreshLayout();
    }

    private void exportAllToXml() {

        NwdDb db = NwdDb.getInstance(this);
        db.open();

        ArrayList<Media> lst = new ArrayList<>();

        ArrayAdapter<MediaListItem> itemsAdapter =
                (ArrayAdapter<MediaListItem>)getListAdapter();

        for(int i = 0; i < itemsAdapter.getCount(); i++){

            MediaListItem mli = itemsAdapter.getItem(i);

            File f = mli.getFile();

            if(f.exists() && f.isFile()){

                lst.add(mli.getMedia());
            }
        }

        try {

            UtilsMnemosyneV5.exportToXml(lst, db);

        }catch (Exception ex){

            Utils.toast(this, "Error exporting all to xml: " + ex.toString());
        }

        Utils.toast(this, "exported.");
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);


//        menu.add(Menu.NONE, MENU_CONTEXT_SHA1_HASH_ID, Menu.NONE, "SHA1 Hash");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

//            case MENU_CONTEXT_SHA1_HASH_ID:
//
//                computeSHA1Hash(info.position);
//
//                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Computes and stores SHA1 hash for selected item if item is a file.
     * If item is a directory, computes and stores hashes for
     * all files within selected directory and all subfolders of the selected directory
     * @param position
     */
    private void computeSHA1Hash(int position) {

        MediaListItem mli = getItem(position);
        File f = mli.getFile();

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
