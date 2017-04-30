package com.nineworldsdeep.gauntlet.mnemosyne.v5;

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

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Tags;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.mnemosyne.MnemoSyneUtils;
import com.nineworldsdeep.gauntlet.sqlite.FileHashDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;
import com.nineworldsdeep.gauntlet.xml.Xml;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageListV5Activity extends AppCompatActivity {

    private File mCurrentDir;
    private List<String> mTimeStampFilters;
    private ListAdapter mCurrentAdapter;
    List<MediaListItem> mMediaListItems;

    private static final int MENU_CONTEXT_SHA1_HASH_ID = 1;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_IMAGES = 2;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_CAMERA = 3;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_SCREENSHOTS = 4;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS = 5;
    private static final int MENU_CONTEXT_MOVE_TO_FOLDER_MEMES = 6;
    private static final int MENU_CONTEXT_EXPORT_XML = 7;

    public static final String EXTRA_CURRENT_PATH =
            "com.nineworldsdeep.gauntlet.IMAGELIST_CURRENT_PATH";
    public static final String EXTRA_TIMESTAMP_FILTER =
            "com.nineworldsdeep.gauntlet.IMAGELIST_TIMESTAMP_FILTER";

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
        //assignDb();
        NwdDb.getInstance(this).open();
        refreshLayout();

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
        //NwdDb.getInstance(this).open();

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

    }


    private void refreshLayout() {

        AsyncItemLoader ail = new AsyncItemLoader();
        ail.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_list_v5, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.action_go_to_home_screen:

                NavigateActivityCommand.navigateTo(
                        HomeListActivity.class, this
                );

                return true;

            case R.id.action_export_all_to_xml:

                exportAllToXml();

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

    }

    private void exportAllToXml() {

        NwdDb db = NwdDb.getInstance(this);
        db.open();

        ArrayList<Media> lst = new ArrayList<>();

        for(MediaListItem mli : mMediaListItems){

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

                MediaListItem mli = mMediaListItems.get(idx);
                File f = mli.getFile();

                if(f.exists() && f.isFile()){

                    Intent intent = new Intent(view.getContext(),
                            ImageDisplayV5Activity.class);

                    intent.putExtra(
                            ImageDisplayV5Activity.EXTRA_IMAGE_PATH,
                            f.getAbsolutePath()
                    );

//                    intent.putExtra(
//                            ImageDisplayV5Activity.EXTRA_TAG_STRING,
//                            mli.getTags()
//                    );

                    startActivity(intent);

                }else if(f.exists() && f.isDirectory()){

                    Intent intent = new Intent(view.getContext(),
                            ImageListV5Activity.class);
                    intent.putExtra(
                            ImageListV5Activity.EXTRA_CURRENT_PATH,
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

        HashMap<String, String> map;

        NwdDb db = NwdDb.getInstance(this);

        db.open();

//        HashMap<String, String> pathToTagString =
//                Tags.getPathToActiveTagStringMap(db);

        mMediaListItems =
                UtilsMnemosyneV5.getMediaListItemsImage(mCurrentDir);

        //sync each as processed
        for(MediaListItem mli : mMediaListItems){

            String tagString = "";

            try{

                if(mli.isFile()) {

                    mli.hashMedia();
                    db.sync(mli.getMedia());
                    tagString = mli.getTags();
                }

            }catch(Exception ex){

                tagString = ex.toString();
            }

            map = new HashMap<>();
            map.put("displayName", mli.getFile().getName());
            map.put("tags", tagString);

            if(mli.getFile().isDirectory()){

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

        boolean isDirectory =
                mMediaListItems.get(info.position).getFile().isDirectory();

        menu.add(Menu.NONE, MENU_CONTEXT_SHA1_HASH_ID, Menu.NONE, "SHA1 Hash");

        if(!isDirectory) {

            menu.add(Menu.NONE, MENU_CONTEXT_EXPORT_XML, Menu.NONE, "Export to XML");
            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_IMAGES, Menu.NONE, "Move to images");
            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_CAMERA, Menu.NONE, "Move to Camera");
            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_SCREENSHOTS, Menu.NONE, "Move to Screenshots");
            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS, Menu.NONE, "Move to Downloads");
            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FOLDER_MEMES, Menu.NONE, "Move to memes");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case MENU_CONTEXT_EXPORT_XML:

                try {

                    exportXml(info.position);
                    Utils.toast(this, "exported");

                }catch(Exception ex){

                    Utils.toast(this, "error exporting xml: " + ex.toString());
                }

                return true;

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

            case MENU_CONTEXT_MOVE_TO_FOLDER_DOWNLOADS:

                moveToDownloads(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FOLDER_MEMES:

                moveToMemes(info.position);

                return true;

            default:

                return super.onContextItemSelected(item);
        }
    }

    private void exportXml(int position) throws Exception {

        MediaListItem mli = mMediaListItems.get(position);
        Media media = mli.getMedia();

        NwdDb db = NwdDb.getInstance(this);
        db.open();

        ArrayList<Media> lst = new ArrayList<>();
        lst.add(media);

        UtilsMnemosyneV5.exportToXml(lst, db);

//        Document doc = Xml.createDocument(Xml.TAG_NWD);
//        Element mnemosyneSubsetEl = doc.createElement(Xml.TAG_MNEMOSYNE_SUBSET);
//        doc.getDocumentElement().appendChild(mnemosyneSubsetEl);
//
//        //get all media, just single table query, to get hash
//        //then sync all with db.populateByHash(ArrayList<Media>)
//
//        //adapted from export all code, leaving it as much the same as possible
//        ArrayList<Media> allMedia = new ArrayList<>();
//        allMedia.add(media);
//
//        db.populateTaggingsAndDevicePaths(allMedia);
//
//        Element mediaEl = doc.createElement(Xml.TAG_MEDIA);
//
//        mediaEl.setAttribute(
//                Xml.ATTR_SHA1_HASH,
//                media.getMediaHash());
//
////
////                mediaEl.setAttribute(
////                        Xml.ATTR_FILE_NAME,
////                        media.getMediaFileName());
////
////                mediaEl.setAttribute(
////                        Xml.ATTR_DESCRIPTION,
////                        media.getMediaDescription());
//
//
//        Xml.setAttributeIfNotNullOrWhitespace(
//                mediaEl,
//                Xml.ATTR_FILE_NAME,
//                media.getMediaFileName());
//
//        Xml.setAttributeIfNotNullOrWhitespace(
//                mediaEl,
//                Xml.ATTR_DESCRIPTION,
//                media.getMediaDescription());
//
//        mnemosyneSubsetEl.appendChild(mediaEl);
//
//        for(MediaTagging mt : media.getMediaTaggings()){
//
//            Element tagEl = doc.createElement(Xml.TAG_TAG);
//
//            tagEl.setAttribute(
//                Xml.ATTR_TAG_VALUE,
//                mt.getMediaTagValue());
//
//            tagEl.setAttribute(
//                Xml.ATTR_TAGGED_AT,
//                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
//                                mt.getTaggedAt()));
//
//            tagEl.setAttribute(
//                Xml.ATTR_UNTAGGED_AT,
//                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
//                                mt.getUntaggedAt()));
//
//            mediaEl.appendChild(tagEl);
//        }
//
//        for(String deviceName : media.getDevicePaths().keySet()){
//
//            Element mediaDeviceEl =
//                    doc.createElement(Xml.TAG_MEDIA_DEVICE);
//
//            mediaDeviceEl.setAttribute(
//                    Xml.ATTR_DESCRIPTION, deviceName);
//
//            mediaEl.appendChild(mediaDeviceEl);
//
//            for(DevicePath dp : media.getDevicePaths().get(deviceName)) {
//
//                Element pathEl = doc.createElement(Xml.TAG_PATH);
//
//                pathEl.setAttribute(
//                        Xml.ATTR_VALUE,
//                        dp.getPath());
//
//                pathEl.setAttribute(
//                        Xml.ATTR_VERIFIED_PRESENT,
//                        TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
//                            dp.getVerifiedPresent()));
//
//                pathEl.setAttribute(
//                        Xml.ATTR_VERIFIED_MISSING,
//                        TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
//                            dp.getVerifiedMissing()));
//
//                mediaDeviceEl.appendChild(pathEl);
//            }
//        }
//
//        File outputFile =
//            Configuration.getOutgoingXmlFile_yyyyMMddHHmmss(
//                    Xml.FILE_NAME_MNEMOSYNE_V5);
//
//            Xml.write(outputFile, doc);
    }

    private void moveToMemes(int position){

        moveFile(position, Configuration.getMemesDirectory());
    }

    private void moveToDownloads(int position){

        moveFile(position, Configuration.getDownloadDirectory());
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

        MediaListItem mli = mMediaListItems.get(position);
        File f = mli.getFile();

        String msg = "";

        if(f.exists()){

            try{

                File destination =
                        new File(destinationDirectory,
                                FilenameUtils.getName(f.getAbsolutePath()));

                NwdDb db = NwdDb.getInstance(this);

                HashMap<String,String> dbPathToTagsMap =
                    TagDbIndex.importExportPathToTagStringMap(db);

                MnemoSyneUtils.copyTags(f.getAbsolutePath(),
                        destination.getAbsolutePath(), dbPathToTagsMap, db);

                MnemoSyneUtils.copyDisplayName(f.getAbsolutePath(),
                        destination.getAbsolutePath(), dbPathToTagsMap, db);

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

        MediaListItem mli = mMediaListItems.get(position);
        File f = mli.getFile();

        String msg = "";

        if(f.exists()){

            //FileHashIndex fhi = FileHashIndex.getInstance();

            try{

                NwdDb db = NwdDb.getInstance(this);

                //we specifically call this with "ignorePreviouslyHashed"
                //as false, because ivImage files get marked up regularily
                //enough that their hashes change often.
                int count = //fhi.countAndStoreSHA1Hashes(f, 0, false);
                        FileHashDbIndex.countAndStoreSHA1Hashes(f, false, db);
//                fhi.sync();

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
//        File f = fli.getMedia();
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
//                fhi.sync();
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
//                fhi.sync();
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
