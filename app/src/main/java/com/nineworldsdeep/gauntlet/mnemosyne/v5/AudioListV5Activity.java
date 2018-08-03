package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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
import com.nineworldsdeep.gauntlet.hive.UtilsHive;
import com.nineworldsdeep.gauntlet.mnemosyne.MnemoSyneUtils;
import com.nineworldsdeep.gauntlet.sqlite.FileHashDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5Utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AudioListV5Activity extends AppCompatActivity {

    private File mCurrentDir;

    //for submenus, holds current info between menus (trust me, otherwise it's null for the submenu)
    private AdapterView.AdapterContextMenuInfo lastMenuInfo = null;

    public static final String EXTRA_CURRENT_PATH =
            "com.nineworldsdeep.gauntlet.AUDIOLIST_CURRENT_PATH";

    private static final int MENU_CONTEXT_SHA1_HASH_ID = 1;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_AUDIO = 2;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_VOICEMEMOS = 3;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS = 4;
    private static final int MENU_CONTEXT_EXPORT_HIVE_XML = 5;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_REF_TRACKS = 6;
    private static final int MENU_CONTEXT_COPY_TO_STAGING = 7;
    private static final int MENU_CONTEXT_MOVE_TO_STAGING = 8;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_PRAXIS = 9;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_STUDY = 10;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_CANVASES = 11;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_PROJECTS = 12;
    private static final int MENU_CONTEXT_OPEN_EXTERNAL = 13;
    private static final int MENU_CONTEXT_COPY_HASH_TO_CLIPBOARD = 14;
    private static final int MENU_CONTEXT_COPY_FILE_NAME_TO_CLIPBOARD = 15;


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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String currentPathString = null;

        if(intent.hasExtra(EXTRA_CURRENT_PATH)) {
            currentPathString = intent.getStringExtra(EXTRA_CURRENT_PATH);
        }

        mCurrentDir = null;

        if(currentPathString != null){

            mCurrentDir = new File(currentPathString);

            if(!mCurrentDir.exists()){

                mCurrentDir = null;
            }
        }

//        refreshLayout();
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

                tvDisplayName.setText(mli.getFile().getName());
                tvTags.setText(mli.getTags());

                if(mli.getFile().isDirectory()){

                    ivImage.setImageResource(R.mipmap.ic_nwd_junction);

                }else{

                    ivImage.setImageResource(R.mipmap.ic_nwd_media);
                }

                return convertView;
            }
        });

        new AsyncLoadItems().execute(mCurrentDir);
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

    class AsyncLoadItems extends AsyncTask<File, ProgressWrapper, String> {


        @Override
        protected void onPreExecute(){

            updateStatus("loading...");
        }

        @Override
        protected String doInBackground(File... directories) {

            int count = 0;
            int total = 0;

            String result = "finished loading successfully";

            try{

                NwdDb db = NwdDb.getInstance(AudioListV5Activity.this);

                db.open();

                ArrayList<MediaListItem> items =
                        UtilsMnemosyneV5.getMediaListItemsAudio(
                                directories[0]);

                total = items.size();

                for (MediaListItem mli : items) {

                    count++;

                    if(mli.isFile()) {

                        MnemosyneRegistry.register(mli);
                        MnemosyneRegistry.sync(mli, db);
                        //db.sync(mli.getMedia());
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

        if(f.exists() && f.isFile()){

            Intent intent = new Intent(view.getContext(),
                    AudioDisplayV5Activity.class);

            intent.putExtra(
                    AudioDisplayV5Activity.EXTRA_AUDIO_PATH,
                    f.getAbsolutePath()
            );

            startActivity(intent);

        }else if(f.exists() && f.isDirectory()){

            Intent intent = new Intent(view.getContext(),
                    AudioListV5Activity.class);
            intent.putExtra(
                    AudioListV5Activity.EXTRA_CURRENT_PATH,
                    f.getAbsolutePath()
            );

            startActivity(intent);

        }else{

            Utils.toast(view.getContext(), f.getAbsolutePath());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio_list_v5, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.action_copy_all_to_staging:

                copyAllToStaging();
                return true;

            case R.id.action_move_all_to_staging:

                moveAllToStaging();
                return true;

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

//            //disabling old xml handling methods
//            case R.id.action_export_all_to_xml:
//
//                exportAllToXml();
//                return true;

            case R.id.action_hive_export_all_to_xml:

                hiveExportAllToXml();
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    private void copyAllToStaging() {

        UtilsHive.copyToStaging(this, getListItemsFiles());
        refreshLayout();
    }

    private void moveAllToStaging() {

        UtilsHive.moveToStaging(this, getListItemsFiles());
        refreshLayout();
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

        Utils.toast(this, "exported");
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

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        //holding for submenus
        lastMenuInfo = info;

        boolean isDirectory = getItem(info.position).getFile().isDirectory();

        menu.add(Menu.NONE, MENU_CONTEXT_SHA1_HASH_ID, Menu.NONE, "SHA1 Hash");

        if(!isDirectory) {

            menu.add(Menu.NONE, MENU_CONTEXT_EXPORT_HIVE_XML, Menu.NONE, "Export to Hive XML");

//            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_AUDIO, Menu.NONE, "Move to audio");
//            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_VOICEMEMOS, Menu.NONE, "Move to voicememos");
//            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS, Menu.NONE, "Move to Downloads");
//            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_REF_TRACKS, Menu.NONE, "Move to refTracks");
//

            SubMenu moveMenu = menu.addSubMenu(Menu.NONE, Menu.NONE, Menu.NONE,"Move To...");

            moveMenu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_AUDIO, Menu.NONE, "Move to audio");
            moveMenu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_VOICEMEMOS, Menu.NONE, "Move to voicememos");
            moveMenu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS, Menu.NONE, "Move to Downloads");
            moveMenu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_REF_TRACKS, Menu.NONE, "Move to refTracks");
            moveMenu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_PRAXIS, Menu.NONE, "Move to praxis");
            moveMenu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_PROJECTS, Menu.NONE, "Move to projects");
            moveMenu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_STUDY, Menu.NONE, "Move to study");
            moveMenu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_CANVASES, Menu.NONE, "Move to canvases");


            menu.add(Menu.NONE, MENU_CONTEXT_COPY_TO_STAGING, Menu.NONE, "Copy to staging");
            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_STAGING, Menu.NONE, "Move to staging");
            menu.add(Menu.NONE, MENU_CONTEXT_OPEN_EXTERNAL, Menu.NONE, "Open External");

            menu.add(Menu.NONE, MENU_CONTEXT_COPY_HASH_TO_CLIPBOARD, Menu.NONE, "Copy Hash");
            menu.add(Menu.NONE, MENU_CONTEXT_COPY_FILE_NAME_TO_CLIPBOARD, Menu.NONE, "Copy File Name");
        }

    }

    @Override
    public void onContextMenuClosed(Menu menu){
        //only held so we have menu info for submenus
        lastMenuInfo = null;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if(info == null){
            //submenu
            info = lastMenuInfo;
        }

        switch (item.getItemId()) {

            case MENU_CONTEXT_COPY_TO_STAGING:

                copyToStaging(info.position);
                return true;

            case MENU_CONTEXT_MOVE_TO_STAGING:

                moveToStaging(info.position);
                return true;

            case MENU_CONTEXT_EXPORT_HIVE_XML:

                try {

                    exportHiveXml(info.position);
                    Utils.toast(this, "exported");

                }catch(Exception ex){

                    Utils.toast(this, "error exporting xml: " + ex.toString());
                }

                return true;

            case MENU_CONTEXT_SHA1_HASH_ID:

                computeSHA1Hash(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_AUDIO:

                moveToAudio(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_VOICEMEMOS:

                moveToVoiceMemos(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS:

                moveToDownloads(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_REF_TRACKS:

                moveToRefTracks(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_PRAXIS:

                moveToPraxis(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_PROJECTS:

                moveToProjects(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_STUDY:

                moveToStudy(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_CANVASES:

                moveToCanvases(info.position);

                return true;

            case MENU_CONTEXT_OPEN_EXTERNAL:

                openExternal(info.position);

                return true;

            case MENU_CONTEXT_COPY_HASH_TO_CLIPBOARD:

                copyHashToClipboard(info.position);

                return true;

            case MENU_CONTEXT_COPY_FILE_NAME_TO_CLIPBOARD:

                copyFileNameToClipboard(info.position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void copyFileNameToClipboard(int position) {

        UtilsMnemosyneV5.copyFileNameToClipboard(this, getItem(position));
    }

    private void copyHashToClipboard(int position) {

        UtilsMnemosyneV5.copyHashToClipboard(this, getItem(position));
    }

    private void openExternal(int position) {

        MediaListItem mli = getItem(position);
        File f = mli.getFile();

        if(f.exists() && f.isFile()){

            //previous version (api < 24)
//            Intent target = new Intent(Intent.ACTION_VIEW);
//
//            String mimeType = UtilsMnemosyneV5.getMimeType(f);
//
//            //target.setDataAndType(Uri.fromFile(f),"*/*");
//            target.setDataAndType(Uri.fromFile(f), mimeType);
//            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            //updating for api >= 24
            Intent target = new Intent(Intent.ACTION_VIEW);

            String mimeType = UtilsMnemosyneV5.getMimeType(f);

            Uri audioUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".com.nineworldsdeep.gauntlet.provider", f);

            target.setDataAndType(audioUri, mimeType);
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try{

                startActivity(target);

            }catch (ActivityNotFoundException ex){

                Utils.toast(AudioListV5Activity.this,
                        "error opening file: " +
                                f.getAbsolutePath());
            }

        }
    }

    private void moveToStaging(int position) {

        UtilsHive.moveToStaging(this, getItem(position).getFile());
        refreshLayout();
    }

    private void copyToStaging(int position) {

        UtilsHive.copyToStaging(this, getItem(position).getFile());
        refreshLayout();
    }

    private void exportHiveXml(int position) throws Exception {

        MediaListItem mli = getItem(position);
        Media media = mli.getMedia();

        NwdDb db = NwdDb.getInstance(this);
        db.open();

        ArrayList<Media> lst = new ArrayList<>();
        lst.add(media);

        //UtilsMnemosyneV5.exportToXml(lst, db);

        UtilsMnemosyneV5.hiveExportToXml(lst, db, this);
    }

    private void hiveExportAllToXml(){

        NwdDb db = NwdDb.getInstance(this);
        db.open();

        ArrayList<Media> lst = getListItemsMedia();

        try {

            UtilsMnemosyneV5.hiveExportToXml(lst, db, this);

        }catch (Exception ex){

            Utils.toast(this, "Error exporting all to xml: " + ex.toString());
        }

        Utils.toast(this, "exported");
    }

    private void moveToDownloads(int position){

        moveFile(position, Configuration.getDownloadDirectory());
    }

    private void moveToRefTracks(int position){

        moveFile(position, Configuration.getRefTracksDirectory());
    }

    private void moveToPraxis(int position){

        moveFile(position, Configuration.getPraxisAudioDirectory());
    }

    private void moveToStudy(int position){

        moveFile(position, Configuration.getStudyAudioDirectory());
    }

    private void moveToProjects(int position){

        moveFile(position, Configuration.getProjectsAudioDirectory());
    }

    private void moveToCanvases(int position){

        moveFile(position, Configuration.getCanvasesDirectory());
    }

    private void moveToVoiceMemos(int position) {

        moveFile(position, Configuration.getVoicememosDirectory());
    }

    private void moveToAudio(int position) {

        moveFile(position, Configuration.getAudioDirectory());
    }

    private void moveFile(int position, File destinationDirectory){

        MediaListItem mli = getItem(position);
        File f = mli.getFile();

        //translate to midi file name
        String fileNameWithoutPathOrExtension =
                FilenameUtils.getBaseName(f.getAbsolutePath());

        String midiFileName = fileNameWithoutPathOrExtension + ".mid";

        File possibleMidiFile =
                new File(mCurrentDir, midiFileName);

        String msg = "";

        if(f.exists()){

            try{

                File destination =
                        new File(destinationDirectory,
                                FilenameUtils.getName(f.getAbsolutePath()));

                File destMidi =
                        new File(Configuration.getMidiDirectory(),
                                 midiFileName);

                NwdDb db = NwdDb.getInstance(this);

                HashMap<String,String> dbPathToTagsMap =
                    TagDbIndex.importExportPathToTagStringMap(db);

                MnemoSyneUtils.copyTags(f.getAbsolutePath(),
                        destination.getAbsolutePath(), dbPathToTagsMap, db);

                MnemoSyneUtils.copyDisplayName(f.getAbsolutePath(),
                        destination.getAbsolutePath(), dbPathToTagsMap, db);

                f.renameTo(destination);

                if(possibleMidiFile.exists()){

                    possibleMidiFile.renameTo(destMidi);

                    Utils.toast(this, "found and moved midi file");
                }

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
