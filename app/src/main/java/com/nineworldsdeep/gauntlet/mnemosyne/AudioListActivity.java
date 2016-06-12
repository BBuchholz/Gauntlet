package com.nineworldsdeep.gauntlet.mnemosyne;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.nineworldsdeep.gauntlet.sqlite.DisplayNameDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Deprecated
public class AudioListActivity extends AppCompatActivity {

//    private File currentDir;
//
//    private static final int MENU_CONTEXT_SHA1_HASH_ID = 1;
//    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_AUDIO = 2;
//    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_VOICEMEMOS = 3;
//
//    public static final String EXTRA_CURRENTPATH =
//            "com.nineworldsdeep.gauntlet.AUDIOLIST_CURRENT_PATH";
//
//    //private NwdDb db;
//
//    private void promptRemoveMarkedAudio() {
//
//        //find any lists in playlists folder that begin with "to be removed"
//        List<String> playlistNamesToBeRemoved = Utils.getToBeRemovedPlaylists();
//
//        final List<String> entriesToBeRemoved = new ArrayList<>();
//
//        for(String playlistName : playlistNamesToBeRemoved){
//
//            File playlistFile = Configuration.getPlaylistFile(playlistName);
//
//            List<String> entries;
//
//            try {
//
//                entries = FileUtils.readLines(playlistFile);
//
//            }catch(IOException ex){
//
//                entries = new ArrayList<>();
//            }
//
//            for(String entry : entries){
//
//                entriesToBeRemoved.add(entry);
//            }
//        }
//
//        String msg = "Found " + playlistNamesToBeRemoved.size() +
//                " playlists containing " + entriesToBeRemoved.size() +
//                " audio files to be removed, would you like to proceed?";
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("Remove Marked Audio")
//                .setMessage(msg)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        int count = 0;
//
//                        for(String path : entriesToBeRemoved){
//
//                            File f = new File(path);
//                            if(f.exists()){
//
//                                f.delete();
//
//                                count++;
//                            }
//                        }
//
//                        Utils.toast(getApplicationContext(), count + " files removed");
//
//                    }
//                })
//                .setNegativeButton("No", null)
//                .show();
//    }
//
//    @Override
//    protected void onResume() {
//
//        super.onResume();
//
//        NwdDb.getInstance(this).open();
//
//        //assignDb();
//
//        //moved to assignDb() //db.open();
//    }
//
////    private void assignDb(){
////
////        if(db == null || db.needsTestModeRefresh()){
////
////            if(Configuration.isInTestMode()){
////
////                //use external db in folder NWD/sqlite
////                db = new NwdDb(this, "test");
////
////            }else {
////
////                //use internal app db
////                db = new NwdDb(this);
////            }
////        }
////
////        db.open();
////    }
//
//    @Override
//    protected void onPause() {
//
//        super.onPause();
//
//        NwdDb.getInstance(this).close();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_audio_list);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        //assignDb();
//
//        Intent i = getIntent();
//        String s = i.getStringExtra(EXTRA_CURRENTPATH);
//
//        currentDir = null;
//
//        if(s != null){
//
//            currentDir = new File(s);
//
//            if(!currentDir.exists()){
//                currentDir = null;
//            }
//        }
//
//        if(currentDir != null){
//
//            setTitle(currentDir.getName());
//
//        }else{
//
//            setTitle("NWD Audio");
//        }
//
//        refreshLayout();
//    }
//
//    private void refreshLayout(){
//
//        ListView lvItems =
//                (ListView) findViewById(R.id.lvItems);
//
//        loadItems();
//        setupListViewListener();
//        registerForContextMenu(lvItems);
//    }
//
//    @Override
//    protected void onRestart(){
//        super.onRestart();
//        refreshLayout();
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu,
//                                    View v,
//                                    ContextMenu.ContextMenuInfo menuInfo){
//        super.onCreateContextMenu(menu, v, menuInfo);
//
//        AdapterView.AdapterContextMenuInfo info =
//                (AdapterView.AdapterContextMenuInfo) menuInfo;
//
//        String name =
//                getItemAtPosition(info.position).getFile().getName();
//
//        boolean isDirectory =
//                getItemAtPosition(info.position).getFile().isDirectory();
//
//        menu.setHeaderTitle(name);
//
//        menu.add(Menu.NONE, MENU_CONTEXT_SHA1_HASH_ID, Menu.NONE, "SHA1 Hash");
//
//        if(!isDirectory) {
//
//            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_AUDIO, Menu.NONE, "Move to audio");
//            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_VOICEMEMOS, Menu.NONE, "Move to voicememos");
//        }
//
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        AdapterView.AdapterContextMenuInfo info =
//                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        switch (item.getItemId()) {
//            case MENU_CONTEXT_SHA1_HASH_ID:
//
//                //computeSHA1Hash(info.position);
//                computeSHA1HashNew(info.position);
//
//                return true;
//
//            case MENU_CONTEXT_MOVE_TO_FOLDER_AUDIO:
//
//                moveToAudio(info.position);
//
//                return true;
//
//            case MENU_CONTEXT_MOVE_TO_FOLDER_VOICEMEMOS:
//
//                moveToVoiceMemos(info.position);
//
//                return true;
//
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }
//
//    private void moveToVoiceMemos(int position) {
//
//        moveFile(position, Configuration.getVoicememosDirectory());
//    }
//
//    private void moveToAudio(int position) {
//
//        moveFile(position, Configuration.getAudioDirectory());
//    }
//
//
//    private void moveFile(int position, File destinationDirectory){
//
//        FileListItem fli = getItemAtPosition(position);
//        File f = fli.getFile();
//
//        String msg = "";
//
//        if(f.exists()){
//
//            try{
//
//                NwdDb db = NwdDb.getInstance(this);
//
//                File destination =
//                        new File(destinationDirectory,
//                                FilenameUtils.getName(f.getAbsolutePath()));
//
//                MnemoSyneUtils.copyTags(f.getAbsolutePath(),
//                        destination.getAbsolutePath(), db);
//
//                MnemoSyneUtils.copyDisplayName(f.getAbsolutePath(),
//                        destination.getAbsolutePath(), db);
//
//                f.renameTo(destination);
//
//                msg = "file moved";
//
//            }catch (Exception ex){
//
//                msg = "Error moving file: " + ex.getMessage();
//            }
//
//        }else{
//
//            msg = "non existant path: " + f.getAbsolutePath();
//        }
//
//        Utils.toast(this, msg);
//        refreshLayout();
//    }
//
//    /**
//     * Computes and stores SHA1 hash for selected item if item is a file.
//     * If item is a directory, computes and stores hashes for
//     * all files within selected directory and all subfolders of the selected directory
//     * @param position
//     */
//    private void computeSHA1HashNew(int position) {
//
//        FileListItem fli = getItemAtPosition(position);
//        File f = fli.getFile();
//
//        String msg = "";
//
//        if(f.exists()){
//
//            FileHashIndex fhi = FileHashIndex.getInstance();
//
//            try{
//
//                //we call the count and store version that
//                //ignores previously hashed files as
//                //our audio files are not likely to change
//                //and many in number (800+ with mp3's on my
//                //test device), so this is a costly operation
//                int count = fhi.countAndStoreSHA1Hashes(f, 0, true);
//
//                fhi.save();
//
//                if(count != 1){
//
//                    msg = count + " hashes stored";
//
//                }else{
//
//                    msg = count + " hash stored";
//                }
//
//            }catch(Exception ex){
//
//                msg = ex.getMessage();
//            }
//
//        }else{
//
//            msg = "NonExistantPath: " + f.getAbsolutePath();
//        }
//
//        Utils.toast(this, msg);
//    }
//
//    /**
//     * Computes and stores SHA1 hash for selected item if item is a file.
//     * If item is a directory, computes and stores hashes for
//     * all files within selected directory and all subfolders of the selected directory
//     * @param position
//     */
//    private void computeSHA1Hash(int position) {
//
//        FileListItem fli = getItemAtPosition(position);
//        File f = fli.getFile();
//
//        if(f.exists() && f.isDirectory()){
//
//            int count = 0;
//
//            FileHashIndex fhi = FileHashIndex.getInstance();
//
//            try {
//
//                for (File f2 : f.listFiles()) {
//
//                    String hash = Utils.computeSHA1(f2.getAbsolutePath());
//                    fhi.storeHash(f2.getAbsolutePath(), hash);
//                    count++;
//                }
//
//                fhi.save();
//                String msg = count + " file hashes stored";
//
//                Utils.toast(this, msg);
//
//            }catch(Exception ex){
//
//                Utils.toast(this, ex.getMessage());
//            }
//
//        }else if(f.exists() && f.isFile()){
//
//            try {
//
//                String hash = Utils.computeSHA1(f.getAbsolutePath());
//                FileHashIndex fhi = FileHashIndex.getInstance();
//                fhi.storeHash(f.getAbsolutePath(), hash);
//                fhi.save();
//
//                Utils.toast(this, "hash stored for file");
//
//            } catch (Exception e) {
//
//                Utils.toast(this, e.getMessage());
//            }
//
//        }else{
//
//            Utils.toast(this, "NonExistantPath: " + f.getAbsolutePath());
//        }
//    }
//
//    private FileListItem getItemAtPosition(int position){
//
//        ListView lvItems =
//                (ListView) findViewById(R.id.lvItems);
//
//        return (FileListItem) lvItems.getItemAtPosition(position);
//    }
//
//    private void setupListViewListener() {
//        final ListView lvItems = (ListView)findViewById(R.id.lvItems);
//        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent,
//                                    View view,
//                                    int idx,
//                                    long id) {
//
//                FileListItem fli = (FileListItem) lvItems.getItemAtPosition(idx);
//                File f = fli.getFile();
//
//                if(f.exists() && f.isFile()){
//
//                    Intent intent = new Intent(view.getContext(),
//                            AudioDisplayActivity.class);
//                    intent.putExtra(
//                            AudioDisplayActivity.EXTRA_AUDIOPATH,
//                            f.getAbsolutePath()
//                    );
//                    startActivity(intent);
//
//                }else if(f.exists() && f.isDirectory()){
//
//                    Intent intent = new Intent(view.getContext(),
//                            AudioListActivity.class);
//                    intent.putExtra(
//                            AudioListActivity.EXTRA_CURRENTPATH,
//                            f.getAbsolutePath()
//                    );
//                    startActivity(intent);
//                }else{
//
//                    Utils.toast(view.getContext(), f.getAbsolutePath());
//                }
//            }
//        });
//    }
//
//    private void loadItems() {
//
//        ListView lvItems = (ListView) findViewById(R.id.lvItems);
//
//        NwdDb db = NwdDb.getInstance(this);
//
//        HashMap<String,String> dbPathToNameMap =
//                DisplayNameDbIndex.getPathToNameMap(db);
//
//        lvItems.setAdapter(
//                new ArrayAdapter<>(this,
//                        android.R.layout.simple_list_item_1,
//                        MnemoSyneUtils.getAudioListItems(dbPathToNameMap, currentDir))
//        );
//    }
}
