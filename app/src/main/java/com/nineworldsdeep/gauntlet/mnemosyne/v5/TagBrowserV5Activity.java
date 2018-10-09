package com.nineworldsdeep.gauntlet.mnemosyne.v5;

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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;

public class TagBrowserV5Activity extends AppCompatActivity {

    //private File mCurrentDir;

    //for submenus, holds current info between menus (trust me, otherwise it's null for the submenu)
    private AdapterView.AdapterContextMenuInfo lastMenuInfo = null;

    public static final String EXTRA_CURRENT_TAG_FILTER =
            "com.nineworldsdeep.gauntlet.TAG_BROWSER_CURRENT_TAG_FILTER";



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
        setContentView(R.layout.activity_tag_browser_v5);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void onTagFilterButtonClick(View view){

        refreshLayout();
    }

    public void refreshLayout() {

        setListAdapter(new ArrayAdapter<TagBrowserTagItem>(
                            this,
                            android.R.layout.simple_list_item_1,
                            new ArrayList<TagBrowserTagItem>()){

            @Override
            public View getView(int pos, View convertView, ViewGroup parent){

                if(convertView==null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView=vi.inflate(R.layout.tag_browser_v5_tag_list_item, null);
                }

                TextView tvTagDisplayName = (TextView)convertView.findViewById(R.id.tvTagDisplayName);
                //TextView tvTaggedCount = (TextView)convertView.findViewById(R.id.tvTaggedCount);

                TagBrowserTagItem tagBrowserTagItem = getItem(pos);

                tvTagDisplayName.setText(tagBrowserTagItem.getTagDisplayName());
                //tvTaggedCount.setText(String.valueOf(tagBrowserTagItem.getTaggedCount()));

                return convertView;
            }
        });

        new AsyncLoadItems().execute();
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
        setContentView(R.layout.activity_tag_browser_v5);
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

    private EditText getEditTextForTagFilter(){
        return findViewById(R.id.etFilter);
    }

    class ProgressWrapper{

        private TagBrowserTagItem tagBrowserTagItem;
        private String status;

        public ProgressWrapper(TagBrowserTagItem tagBrowserTagItem, String status){

            this.tagBrowserTagItem = tagBrowserTagItem;
            this.status = status;
        }

        public TagBrowserTagItem getTagBrowserTagItem() {
            return tagBrowserTagItem;
        }

        public String getStatus() {
            return status;
        }
    }

    class AsyncLoadItems extends AsyncTask<Void, ProgressWrapper, String> {

        @Override
        protected void onPreExecute(){

            updateStatus("loading...");
        }

        @Override
        protected String doInBackground(Void... nothings) {


            int count = 0;
            int total = 0;

            String result = "finished loading successfully";

            try{

                NwdDb db = NwdDb.getInstance(TagBrowserV5Activity.this);

                db.open();

                if(!TagBrowserV5Repository.isLoaded()){
                    TagBrowserV5Repository.refreshTagItems(db,
                            TagBrowserV5Activity.this);
                }

                String tagFilter = getEditTextForTagFilter().getText().toString();

                ArrayList<TagBrowserTagItem> tagBrowserTagItems =
                        TagBrowserV5Repository.getTagItems(tagFilter);

                total = tagBrowserTagItems.size();

                for (TagBrowserTagItem tagBrowserTagItem : tagBrowserTagItems) {

                    count++;

                    // only load if filter is entered, open filter
                    // is WAY TOO MANY tags to preload
                    if(tagFilter.trim().length() > 0 && !tagBrowserTagItem.isLoaded()) {

                        TagBrowserV5Repository.loadFileItems(tagBrowserTagItem, db);
                    }

                    String msg = "Still loading... (" + count + " of " + total + " items loaded)";

                    publishProgress(new ProgressWrapper(tagBrowserTagItem, msg));
                }

            }catch (Exception ex){

                result = "Error loading items: " + ex.toString();
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(ProgressWrapper... items) {

            ProgressWrapper pw = items[0];
            ((ArrayAdapter<TagBrowserTagItem>)getListAdapter()).add(
                    pw.getTagBrowserTagItem());

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

        TagBrowserTagItem tagBrowserTagItem = getItem(idx);

        // file items are not preloaded until filter is applied
        // this check is required in case a filter was never
        // applied for a given tag item
        if(!tagBrowserTagItem.isLoaded()){

            NwdDb db = NwdDb.getInstance(TagBrowserV5Activity.this);

            db.open();

            TagBrowserV5Repository.loadFileItems(tagBrowserTagItem, db);
        }


        Intent intent = new Intent(view.getContext(),
                TagBrowserFileListV5Activity.class);

        intent.putExtra(
                TagBrowserFileListV5Activity.EXTRA_CURRENT_TAG_FILTER,
                tagBrowserTagItem.getTagName()
        );

        startActivity(intent);

    }

    private TagBrowserTagItem getItem(int idx) {

        return ((ArrayAdapter<TagBrowserTagItem>)getListAdapter()).getItem(idx);
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
        getMenuInflater().inflate(R.menu.menu_tag_browser_v5, menu);
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

            case R.id.action_refresh_tag_repository:

                NwdDb db = NwdDb.getInstance(TagBrowserV5Activity.this);

                db.open();

                TagBrowserV5Repository.refreshTagItems(db,
                        TagBrowserV5Activity.this);

                refreshLayout();

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

        //holding for submenus
        lastMenuInfo = info;

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

            default:
                return super.onContextItemSelected(item);
        }
    }

}
