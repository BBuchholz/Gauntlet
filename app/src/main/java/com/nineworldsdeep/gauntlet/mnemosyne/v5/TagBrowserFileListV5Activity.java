package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;
import java.util.TreeSet;

public class TagBrowserFileListV5Activity extends AppCompatActivity {

    //private File mCurrentDir;
    private String mCurrentTag;

    //for submenus, holds current info between menus (trust me, otherwise it's null for the submenu)
    private AdapterView.AdapterContextMenuInfo lastMenuInfo = null;

    public static final String EXTRA_CURRENT_TAG_FILTER =
            "com.nineworldsdeep.gauntlet.TAG_BROWSER_CURRENT_TAG_FILTER";

    private static final int MENU_CONTEXT_COPY_FILE_NAME_TO_CLIPBOARD = 1;
    private static final int MENU_CONTEXT_MARK_NOT_FOUND_IN_CLOUD = 2;


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
        setContentView(R.layout.activity_tag_browser_file_list_v5);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_CURRENT_TAG_FILTER)) {
            mCurrentTag = intent.getStringExtra(EXTRA_CURRENT_TAG_FILTER);
            if(mCurrentTag != null){
                setTitle(mCurrentTag);
            }
        }

    }

    private void refreshLayout() {



        setListAdapter(new ArrayAdapter<TagBrowserFileItem>(
                            this,
                            android.R.layout.simple_list_item_1,
                            new ArrayList<TagBrowserFileItem>()){

            @Override
            public View getView(int pos, View convertView, ViewGroup parent){

                if(convertView==null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView=vi.inflate(R.layout.tag_browser_v5_file_list_item, null);
                }

                TextView tvFilename = (TextView)convertView.findViewById(R.id.tvFilename);

                TagBrowserFileItem tagBrowserFileItem = getItem(pos);

                tvFilename.setText(tagBrowserFileItem.getFilename());

                return convertView;
            }
        });

        new AsyncLoadItems().execute(mCurrentTag);
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
        setContentView(R.layout.activity_tag_browser_file_list_v5);
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

    private EditText getEditTextForFileFilter(){
        return findViewById(R.id.etFilter);
    }

    class ProgressWrapper{

        private TagBrowserFileItem tagBrowserFileItem;
        private String status;

        public ProgressWrapper(TagBrowserFileItem tagBrowserFileItem, String status){

            this.tagBrowserFileItem = tagBrowserFileItem;
            this.status = status;
        }

        public TagBrowserFileItem getTagBrowserFileItem() {
            return tagBrowserFileItem;
        }

        public String getStatus() {
            return status;
        }
    }

    class AsyncLoadItems extends AsyncTask<String, ProgressWrapper, String> {


        @Override
        protected void onPreExecute(){

            updateStatus("loading...");
        }

        @Override
        protected String doInBackground(String... tagFilters) {


            int count = 0;
            int total = 0;

            String result = "finished loading successfully";

            try{

                NwdDb db = NwdDb.getInstance(TagBrowserFileListV5Activity.this);

                db.open();


                String fileFilter = getEditTextForFileFilter().getText().toString();

                TreeSet<TagBrowserFileItem> tagBrowserFileItems =
                        TagBrowserV5Repository.getFileItems(mCurrentTag, fileFilter);

                total = tagBrowserFileItems.size();

                for (TagBrowserFileItem tagBrowserFileItem : tagBrowserFileItems) {

                    count++;



                    String msg = "Still loading... (" + count + " of " + total + " items loaded)";

                    publishProgress(new ProgressWrapper(tagBrowserFileItem, msg));
                }

            }catch (Exception ex){

                result = "Error loading items: " + ex.toString();
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(ProgressWrapper... items) {

            ProgressWrapper pw = items[0];
            ((ArrayAdapter<TagBrowserFileItem>)getListAdapter()).add(
                    pw.getTagBrowserFileItem());

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

        TagBrowserFileItem tagBrowserFileItem = getItem(idx);

        String filename = tagBrowserFileItem.getFilename();

        Utils.copyToClipboard(TagBrowserFileListV5Activity.this,
                "tag-browser-file-item-name", filename);

        String msg = "[" + filename + "] copied to clipboard";

        Utils.toast(TagBrowserFileListV5Activity.this, msg);
    }

    private TagBrowserFileItem getItem(int idx) {

        return ((ArrayAdapter<TagBrowserFileItem>)getListAdapter()).getItem(idx);
    }

    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
        {
            onListItemClick((ListView)parent, v, position, id);
        }
    };

    public void onFileFilterButtonClick(View view){

        refreshLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag_browser_file_list_v5, menu);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        // holding for submenus
        lastMenuInfo = info;

        menu.add(Menu.NONE, MENU_CONTEXT_MARK_NOT_FOUND_IN_CLOUD,
                Menu.NONE, "Missing From Cloud");


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




            case MENU_CONTEXT_MARK_NOT_FOUND_IN_CLOUD:

                markNotFoundInCloud(info.position);

                return true;

            case MENU_CONTEXT_COPY_FILE_NAME_TO_CLIPBOARD:

                copyFileNameToClipboard(info.position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void markNotFoundInCloud(int position) {

        final TagBrowserFileItem tagBrowserFileItem = getItem(position);

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt, null);

        final String verificationText = "missing in cloud";

        String promptMsg = "about to remove all tags and " +
                "mark this [not found in the cloud] this " +
                "should only be done if the media isn't " +
                "available anywhere in the cloud, as it will " +
                "remove the tags across the entire ecosystem. " +
                "To confirm this action, type the phrase \"" + verificationText + "\"";

        TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
        tv.setText(promptMsg);

        android.app.AlertDialog.Builder alertDialogBuilder =
                new android.app.AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                                String verificationPhrase = userInput.getText().toString();

                                boolean verified =
                                        verificationPhrase.equalsIgnoreCase(
                                                verificationText);

                                if(verified){

                                    NwdDb db =
                                        NwdDb.getInstance(TagBrowserFileListV5Activity.this);

                                    Media fileItemMedia = new Media();
                                    fileItemMedia.setMediaHash(tagBrowserFileItem.getHash());

                                    try {

                                        db.sync(fileItemMedia);

                                        for(MediaTagging mediaTagging : fileItemMedia.getMediaTaggings()){

                                            mediaTagging.untag();
                                        }

                                        fileItemMedia.getTag("not found in the cloud").tag();

                                        db.sync(fileItemMedia);

                                        ArrayList<Media> lst = new ArrayList<>();
                                        lst.add(fileItemMedia);

                                        UtilsMnemosyneV5.hiveExportToXml(lst, db,
                                                TagBrowserFileListV5Activity.this);

                                    } catch (Exception e) {

                                        Utils.toast(TagBrowserFileListV5Activity.this,
                                                "error syncing media by hash: " + e.getMessage());
                                    }


                                }else{

                                    Utils.toast(
                                    TagBrowserFileListV5Activity.this,
                                            "mark missing cancelled");
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                Utils.toast(
                                    TagBrowserFileListV5Activity.this,
                                        "mark missing cancelled");

                                dialog.cancel();
                            }
                        });

        // create alert dialog
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    private void copyFileNameToClipboard(int position) {

        UtilsMnemosyneV5.copyFileNameToClipboard(this, getItem(position));
    }

}
