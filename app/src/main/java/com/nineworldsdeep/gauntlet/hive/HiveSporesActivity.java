package com.nineworldsdeep.gauntlet.hive;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.AudioDisplayV5Activity;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaListItem;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaPlayerSingletonV5;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.UtilsMnemosyneV5;
import com.nineworldsdeep.gauntlet.sqlite.FileHashDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.nineworldsdeep.gauntlet.hive.HiveLobesActivity.EXTRA_HIVE_LOBE_KEY;
import static com.nineworldsdeep.gauntlet.hive.HiveRootsActivity.EXTRA_HIVE_ROOT_KEY;

public class HiveSporesActivity extends AppCompatActivity {

    private HiveLobe mHiveLobe;

    private static final int MENU_CONTEXT_OPEN_EXTERNAL = 1;
    private static final int MENU_CONTEXT_INTAKE = 2;

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
        //setContentView(R.layout.activity_audio_list_v52);
        setContentView(R.layout.activity_hive_spores);

        String lobeKey = getIntent().getStringExtra(
                EXTRA_HIVE_LOBE_KEY
        );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Spores");

        mHiveLobe = HiveRegistry.getHiveLobe(lobeKey);
        TextView tvHierarchyAddress =
                (TextView) findViewById(R.id.tvHierarchyAddress);

        tvHierarchyAddress.setText(lobeKey);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                HashMap<String, String> extraKeyToValue = new HashMap<>();

                extraKeyToValue.put(EXTRA_HIVE_ROOT_KEY,
                        mHiveLobe.getHiveRoot().getHiveRootName());

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

        new AsyncLoadItems().execute(mHiveLobe);
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

        registerForContextMenu(mList);

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

    class AsyncLoadItems extends AsyncTask<HiveLobe, ProgressWrapper, String> {


        @Override
        protected void onPreExecute(){

            updateStatus("loading...");
        }

        @Override
        protected String doInBackground(HiveLobe... hiveLobes) {

            int count = 0;
            int total;

            String result = "finished loading successfully";

            try{

                NwdDb db = NwdDb.getInstance(HiveSporesActivity.this);

                db.open();

                HiveLobe hl = hiveLobes[0];

                UtilsHive.refreshSpores(hl);

                ArrayList<MediaListItem> items =
                        UtilsHive.getSporesAsMediaListItems(hl);

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

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        NwdDb db = NwdDb.getInstance(this);
        db.open();

        boolean isLocalRoot =
                UtilsHive.isLocalRoot(this, db, mHiveLobe.getHiveRoot());

        boolean isDirectory = getItem(info.position).getFile().isDirectory();

        if(!isDirectory) {

            menu.add(Menu.NONE, MENU_CONTEXT_OPEN_EXTERNAL,
                    Menu.NONE, "Open External");

            if(isLocalRoot) {

                menu.add(Menu.NONE, MENU_CONTEXT_INTAKE,
                        Menu.NONE, "Intake");
            }
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case MENU_CONTEXT_OPEN_EXTERNAL:

                openExternal(info.position);
                return true;

            case MENU_CONTEXT_INTAKE:

                intake(info.position);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void intake(int position) {

        try{

            ArrayList<File> intakeFiles = new ArrayList<>();
            intakeFiles.add(getItem(position).getFile());

            UtilsHive.intake(this, intakeFiles);

            Utils.toast(this, "intake successful.");

        }
        catch (Exception ex){

            Utils.toast(this,
                    "[HiveSporesActivity] intake error: " + ex.getMessage());
        }

        refreshLayout();
    }

    private void openExternal(int position) {

        MediaListItem mli = getItem(position);
        File f = mli.getFile();

        if(f.exists() && f.isFile()){

            Intent target = new Intent(Intent.ACTION_VIEW);

            String mimeType = UtilsMnemosyneV5.getMimeType(f);

            //target.setDataAndType(Uri.fromFile(f),"*/*");
            target.setDataAndType(Uri.fromFile(f), mimeType);
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            try{

                startActivity(target);

            }catch (ActivityNotFoundException ex){

                Utils.toast(HiveSporesActivity.this,
                        "error opening file: " +
                        f.getAbsolutePath());
            }

        }
    }


//    /**
//     * This method will be called when an item in the list is selected.
//     * Subclasses should override. Subclasses can call
//     * getListView().getItemAtPosition(position) if they need to access the
//     * data associated with the selected item.
//     *
//     * adapted from ListActivity source
//     *
//     * @param l The ListView where the click happened
//     * @param view The view that was clicked within the ListView
//     * @param idx The position of the view in the list
//     * @param id The row id of the item that was clicked
//     */
//    protected void onListItemClick(ListView l, View view, int idx, long id) {
////
////        MediaListItem mli = getItem(idx);
////        File f = mli.getFile();
////
////        if(f.exists() && f.isFile()){
////
////            Intent target = new Intent(Intent.ACTION_VIEW);
////
////            String mimeType = UtilsMnemosyneV5.getMimeType(f);
////
////            //target.setDataAndType(Uri.fromFile(f),"*/*");
////            target.setDataAndType(Uri.fromFile(f), mimeType);
////            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
////
////            try{
////
////                startActivity(target);
////
////            }catch (ActivityNotFoundException ex){
////
////                Utils.toast(HiveSporesActivity.this,
////                        "error opening file: " +
////                        f.getAbsolutePath());
////            }
////
////        }
//
////        if(f != null && f.exists() && f.isFile()){
////
////            Intent intent = new Intent(view.getContext(),
////                    AudioDisplayV5Activity.class);
////
////            intent.putExtra(
////                    AudioDisplayV5Activity.EXTRA_AUDIO_PATH,
////                    f.getAbsolutePath()
////            );
////
////            startActivity(intent);
////
////        }else{
////
////            Utils.toast(this, "not found");
////        }
//    }

    private MediaListItem getItem(int idx) {

        return ((ArrayAdapter<MediaListItem>)getListAdapter()).getItem(idx);
    }

//
//    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
//        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
//        {
//            onListItemClick((ListView)parent, v, position, id);
//        }
//    };

    @Override
    public void onBackPressed() {

        HashMap<String, String> extraKeyToValue = new HashMap<>();

        extraKeyToValue.put(EXTRA_HIVE_ROOT_KEY,
                mHiveLobe.getHiveRoot().getHiveRootName());

        NavigateActivityCommand.navigateTo(
                extraKeyToValue,
                HiveLobesActivity.class,
                HiveSporesActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hive_spores, menu);
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

            case R.id.action_intake_all:

                intakeAll();
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    private void intakeAll() {

        try {

            UtilsHive.intake(this, getListItemsFiles());
            Utils.toast(this, "intake successful.");
            refreshLayout();

        } catch (Exception e) {

            Utils.toast(this, "intake error: " + e.getMessage());
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

        ArrayList<Media> lst = getListItemsMedia();

        try {

            UtilsMnemosyneV5.exportToXml(lst, db);

        }catch (Exception ex){

            Utils.toast(this, "Error exporting all to xml: " + ex.toString());
        }

        Utils.toast(this, "exported.");
    }

    @NonNull
    private ArrayList<Media> getListItemsMedia() {
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
        return lst;
    }

    @NonNull
    private ArrayList<File> getListItemsFiles() {
        ArrayList<File> lst = new ArrayList<>();

        ArrayAdapter<MediaListItem> itemsAdapter =
                (ArrayAdapter<MediaListItem>)getListAdapter();

        for(int i = 0; i < itemsAdapter.getCount(); i++){

            MediaListItem mli = itemsAdapter.getItem(i);

            File f = mli.getFile();

            if(f.exists() && f.isFile()){

                lst.add(f);
            }
        }
        return lst;
    }

}
