package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.sqlite.FileHashDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyV3MainActivity;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AudioListV2Activity extends AppCompatActivity {

    private File mCurrentDir;
    private List<String> mTimeStampFilters;
    private ListAdapter mCurrentAdapter;
    List<FileListItem> mFileListItems;

    private static final int MENU_CONTEXT_SHA1_HASH_ID = 1;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_AUDIO = 2;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_VOICEMEMOS = 3;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS = 4;

    public static final String EXTRA_CURRENT_PATH =
            "com.nineworldsdeep.gauntlet.AUDIOLIST_CURRENT_PATH";
    public static final String EXTRA_TIMESTAMP_FILTER =
            "com.nineworldsdeep.gauntlet.AUDIOLIST_TIMESTAMP_FILTER";

    // http://stackoverflow.com/questions/3014089/maintain-save-restore-scroll-position-when-returning-to-a-listview
    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;

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
//        if (mListState != null)
//            getListView().onRestoreInstanceState(mListState);
//        mListState = null;
    }

    private void restoreInstanceState(){
        if (mListState != null)
            getListView().onRestoreInstanceState(mListState);
        mListState = null;
    }

    private void storeInstanceState(){
        mListState = getListView().onSaveInstanceState();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        //mListState = getListView().onSaveInstanceState();
        storeInstanceState();
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
        String s = null;

        if(i.hasExtra(EXTRA_CURRENT_PATH)) {
            s = i.getStringExtra(EXTRA_CURRENT_PATH);
        }

        if(i.hasExtra(EXTRA_TIMESTAMP_FILTER)) {

            mTimeStampFilters =
                    MnemoSyneUtils.toTimeStampFilterList(
                            i.getStringExtra(EXTRA_TIMESTAMP_FILTER));
        }

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio_list_v2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.action_go_to_synergy:

                NavigateActivityCommand.navigateTo(
                        SynergyV3MainActivity.class, this
                );

                return true;

            case R.id.action_go_to_pdfs:

                NavigateActivityCommand.navigateTo(
                        PdfListActivity.class, this
                );

                return true;

            case R.id.action_go_to_images:

                NavigateActivityCommand.navigateTo(
                        ImageListV2Activity.class, this
                );

                return true;

            case R.id.action_go_to_audio_player:

                NavigateActivityCommand.navigateTo(
                        AudioDisplayActivity.class, this);

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

    }

    private ListView getListView(){

        return (ListView) findViewById(R.id.lvItems);
    }

    private void refreshLayout() {

        AsyncItemLoader ail = new AsyncItemLoader();
        //ail.execute();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            ail.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            ail.execute();
    }

    private class AsyncItemLoader extends AsyncTask<Void, String, String>{

        @Override
        protected String doInBackground(Void... params) {

            String result;

            try{

                //ListView lvItems = getListView();

                long start = System.nanoTime();

                publishProgress("loading...");
                mCurrentAdapter = loadItems();

                long elapsedTime = System.nanoTime() - start;
                long milliseconds = elapsedTime / 1000000;

                String elapsedTimeStr = Long.toString(milliseconds);

                result = "finished loading: " + elapsedTimeStr + "ms";

            }catch (Exception e){

                result = e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            //Utils.toast(AudioListV2Activity.this, result);
            updateStatus(result);

            if(mCurrentAdapter != null){

                ListView lvItems = (ListView) findViewById(R.id.lvItems);
                lvItems.setAdapter(mCurrentAdapter);
                setupListViewListener();
                registerForContextMenu(lvItems);

                restoreInstanceState();
            }
        }

        @Override
        protected void onPreExecute() {


            storeInstanceState();

        }

        @Override
        protected void onProgressUpdate(String... text) {

            //Utils.toast(AudioListV2Activity.this, text[0]);
            if(text.length > 0)
            updateStatus(text[0]);
        }
    }

    private void updateStatus(String status){

        TextView tv = (TextView)findViewById(R.id.tvStatus);
        tv.setText(status);
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

    private ListAdapter loadItems() {

        ArrayList<HashMap<String, String>> lstItems =
                new ArrayList<HashMap<String, String>>();

        NwdDb db = NwdDb.getInstance(this);

        db.open();

        HashMap<String, String> map;

        HashMap<String, String> pathToTagString =
                TagDbIndex.importExportPathToTagStringMap(db);

        mFileListItems =
                MnemoSyneUtils.getAudioListItems(pathToTagString,
                        mCurrentDir, mTimeStampFilters);

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

        return saItems;
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
            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS, Menu.NONE, "Move to Downloads");
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

            case MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS:

                moveToDownloads(info.position);

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void moveToDownloads(int position){

        moveFile(position, Configuration.getDownloadDirectory());
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
