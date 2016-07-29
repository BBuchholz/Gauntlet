package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.nineworldsdeep.gauntlet.sqlite.FileHashDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageListV2Activity extends AppCompatActivity {

    private File mCurrentDir;
    private List<String> mTimeStampFilters;
    private ListAdapter mCurrentAdapter;
    List<FileListItem> mFileListItems;

    private static final int MENU_CONTEXT_SHA1_HASH_ID = 1;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_IMAGES = 2;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_CAMERA = 3;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_SCREENSHOTS = 4;

    public static final String EXTRA_CURRENT_PATH =
            "com.nineworldsdeep.gauntlet.IMAGELIST_CURRENT_PATH";
    public static final String EXTRA_TIMESTAMP_FILTER =
            "com.nineworldsdeep.gauntlet.IMAGELIST_TIMESTAMP_FILTER";

    // http://stackoverflow.com/questions/3014089/maintain-save-restore-scroll-position-when-returning-to-a-listview
    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;

    //private NwdDb db;

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

//    @Override
//    protected void onPause() {
//
//        super.onPause();
//        NwdDb.getInstance(this).close();
//    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mListState = state.getParcelable(LIST_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //assignDb();
        NwdDb.getInstance(this).open();
        refreshLayout();
//        if (mListState != null)
//            getListView().onRestoreInstanceState(mListState);
//        mListState = null;
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
        setContentView(R.layout.activity_image_list_v2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //assignDb();

        NwdDb.getInstance(this).open();

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

            setTitle("NWD Images");
        }

        //refreshLayout();
    }

//    @Override
//    protected void onRestart(){
//        super.onRestart();
//        refreshLayout();
//    }

    private void refreshLayout() {

        AsyncItemLoader ail = new AsyncItemLoader();
        ail.execute();
    }


    private class AsyncItemLoader extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {

            String result;

            try{

                long start = System.nanoTime();

                publishProgress("loading items...");
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

            //Utils.toast(ImageListV2Activity.this, result);
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

            if(text.length > 0)
            updateStatus(text[0]);
        }

    }

    private void updateStatus(String status){

        TextView tv = (TextView)findViewById(R.id.tvStatus);
        tv.setText(status);
    }

    private void restoreInstanceState(){
        if (mListState != null)
            getListView().onRestoreInstanceState(mListState);
        mListState = null;
    }

    private void storeInstanceState(){
        mListState = getListView().onSaveInstanceState();
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

    private ListAdapter loadItems() {

        //ListView lvItems = (ListView) findViewById(R.id.lvItems);

        ArrayList<HashMap<String, String>> lstItems =
                new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map;

        NwdDb db = NwdDb.getInstance(this);

        db.open();

        HashMap<String, String> pathToTagString =
                TagDbIndex.importExportPathToTagStringMap(db);

        mFileListItems =
                MnemoSyneUtils.getImageListItems(pathToTagString,
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

        //lvItems.setAdapter(saItems);

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

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_IMAGES, Menu.NONE, "Move to images");
            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_CAMERA, Menu.NONE, "Move to Camera");
            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_SCREENSHOTS, Menu.NONE, "Move to Screenshots");
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

            case MENU_CONTEXT_MOVE_TO_FOLDER_IMAGES:

                moveToImages(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_CAMERA:

                moveToCamera(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_SCREENSHOTS:

                moveToScreenShots(info.position);

                return true;

            default:

                return super.onContextItemSelected(item);
        }
    }

    private void moveToScreenShots(int position) {

        File f = Configuration.getScreenshotDirectory();

        if(f.exists()){

            moveFile(position, Configuration.getScreenshotDirectory());

        }else{

            String msg = "screenshots folder not found at: " +
                    f.getAbsolutePath() +
                    " you should manually configure the path in your ConfigFile";

            Utils.toast(this, msg);
        }
    }

    private void moveToCamera(int position) {

        moveFile(position, Configuration.getCameraDirectory());
    }

    private void moveToImages(int position) {

        moveFile(position, Configuration.getImagesDirectory());
    }


    private void moveFile(int position, File destinationDirectory){

        FileListItem fli = mFileListItems.get(position);
        File f = fli.getFile();

        String msg = "";

        if(f.exists()){

            try{

                File destination =
                        new File(destinationDirectory,
                                FilenameUtils.getName(f.getAbsolutePath()));

                MnemoSyneUtils.copyTags(f.getAbsolutePath(),
                        destination.getAbsolutePath(), NwdDb.getInstance(this));

                MnemoSyneUtils.copyDisplayName(f.getAbsolutePath(),
                        destination.getAbsolutePath(), NwdDb.getInstance(this));

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

    private void computeSHA1Hash(int position) {

        FileListItem fli = mFileListItems.get(position);
        File f = fli.getFile();

        String msg = "";

        if(f.exists()){

            //FileHashIndex fhi = FileHashIndex.getInstance();

            try{

                NwdDb db = NwdDb.getInstance(this);

                //we specifically call this with "ignorePreviouslyHashed"
                //as false, because image files get marked up regularily
                //enough that their hashes change often.
                int count = //fhi.countAndStoreSHA1Hashes(f, 0, false);
                        FileHashDbIndex.countAndStoreSHA1Hashes(f, false, db);
//                fhi.save();

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

        //COMMENTED OUT SINCE AT LEAST 20160419
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
    }

}
