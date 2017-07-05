package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.mnemosyne.ImageGridAdapter;
import com.nineworldsdeep.gauntlet.mnemosyne.ImageGridItem;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.io.File;
import java.util.ArrayList;

public class ImageGridV5Activity extends AppCompatActivity {

    private GridView mImageGrid;
    private ImageGridAdapter mImageGridAdapter;

    private static final String GRID_STATE = "gridState";
    private Parcelable mGridState = null;

    private Handler mHandler = new Handler();
    private boolean mFinishedStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mGridState = state.getParcelable(GRID_STATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        mGridState = getGridView().onSaveInstanceState();
        state.putParcelable(GRID_STATE, mGridState);
    }

    private GridView getGridView(){

        return (GridView)findViewById(R.id.gvImageGrid);
    }

    @Override
    protected void onResume() {
        super.onResume();

        NwdDb.getInstance(this).open();
        refreshLayout();

    }
//
//    private class AsyncItemLoader extends AsyncTask<Void, String, String> {
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            String result;
//
//            try{
//
//                long start = System.nanoTime();
//
//                publishProgress("loading items...");
//                mImageGridAdapter = loadItems();
//
//                long elapsedTime = System.nanoTime() - start;
//                long milliseconds = elapsedTime / 1000000;
//
//                String elapsedTimeStr = Long.toString(milliseconds);
//
//                result = "finished loading: " + elapsedTimeStr + "ms";
//
//            }catch (Exception e){
//
//                result = e.getMessage();
//            }
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            updateStatus(result);
//
//            if(mImageGridAdapter != null){
//
//                mImageGrid = (GridView) findViewById(R.id.gvImageGrid);
//
//                mImageGrid.setAdapter(mImageGridAdapter);
//
//                setupClickListener();
//
//                restoreInstanceState();
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            storeInstanceState();
//        }
//
//        @Override
//        protected void onProgressUpdate(String... text) {
//
//            if(text.length > 0)
//            updateStatus(text[0]);
//        }
//
//    }
//
    class ProgressWrapper{

        private ImageGridItem igi;
        private String status;

        public ProgressWrapper(ImageGridItem igi, String status){

            this.igi = igi;
            this.status = status;
        }

        public ImageGridItem getMediaListItem() {
            return igi;
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

                NwdDb db = NwdDb.getInstance(ImageGridV5Activity.this);

                db.open();

                ArrayList<MediaListItem> items =
                        UtilsMnemosyneV5.getMediaListItemsImage(
                                directories[0]);

                total = items.size();

                for (MediaListItem mli : items) {

                    count++;

                    if(mli.isFile()) {

                        mli.hashMedia();
                        db.sync(mli.getMedia());
                    }


                    String msg = "Still loading... (" + count + " of " +
                            total + " items loaded)";

                    publishProgress(
                            new ProgressWrapper(ImageGridItem.From(mli), msg));
                }

            }catch (Exception ex){

                result = "Error loading items: " + ex.toString();
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(ProgressWrapper... items) {

            ProgressWrapper pw = items[0];
            getListAdapter().add(
                    pw.getMediaListItem());

            updateStatus(pw.getStatus());
        }

        @Override
        protected void onPostExecute(String result) {

            updateStatus(result);

        }
    }

    private void restoreInstanceState(){
        if (mGridState != null)
            getGridView().onRestoreInstanceState(mGridState);
        mGridState = null;
    }

    private void storeInstanceState(){
        mGridState = getGridView().onSaveInstanceState();
    }

    private void updateStatus(String status){

        TextView tv = (TextView)findViewById(R.id.tvStatus);
        tv.setText(status);
    }

    private void refreshLayout(){

//        AsyncItemLoader ail = new AsyncItemLoader();
//        ail.execute();

        setGridAdapter(new ImageGridAdapter(
                            this,
                            android.R.layout.simple_list_item_1,
                            new ArrayList<ImageGridItem>()){

            @Override
            public View getView(int pos, View convertView, ViewGroup parent){

                if(convertView==null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView=vi.inflate(R.layout.media_list_item, null);
                }

                TextView tvDisplayName = (TextView)convertView.findViewById(R.id.display_name);
                TextView tvTags = (TextView)convertView.findViewById(R.id.tags);
                ImageView ivImage = (ImageView)convertView.findViewById(R.id.img);

                ImageGridItem igi = getItem(pos);

                tvDisplayName.setText(igi.getFile().getName());
                tvTags.setText(igi.getTags());

                if(igi.getFile().isDirectory()){

                    ivImage.setImageResource(R.mipmap.ic_nwd_junction);

                }else{

                    ivImage.setImageResource(R.mipmap.ic_nwd_media);
                }

                return convertView;
            }
        });

        new AsyncLoadItems().execute(Configuration.getScreenshotDirectory());
    }

//    private ImageGridAdapter loadItems(){
//
//        return new ImageGridAdapter(this,
//                        R.layout.image_item, getImages());
//    }

    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
        {
            onImageGridItemClick((GridView)parent, v, position, id);
        }
    };

    protected void onImageGridItemClick(GridView parent, View view, int position, long id) {

        ImageGridItem igi =
                (ImageGridItem) parent.getItemAtPosition(position);

        File f = igi.getFile();

        if(f.exists() && f.isFile()){

            Intent intent = new Intent(view.getContext(),
                    ImageDisplayV5Activity.class);
            intent.putExtra(
                    ImageDisplayV5Activity.EXTRA_IMAGE_PATH,
                    f.getAbsolutePath()
            );
            startActivity(intent);
        }
    }

    private void setupClickListener(){

        mImageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                ImageGridItem igi =
                        (ImageGridItem) parent.getItemAtPosition(position);

                File f = igi.getFile();

                if(f.exists() && f.isFile()){

                    Intent intent = new Intent(view.getContext(),
                            ImageDisplayV5Activity.class);
                    intent.putExtra(
                            ImageDisplayV5Activity.EXTRA_IMAGE_PATH,
                            f.getAbsolutePath()
                    );
                    startActivity(intent);
                }
            }
        });
    }

//    private ArrayList<ImageGridItem> getImages() {
//
//        NwdDb db = NwdDb.getInstance(this);
//
//        db.open();
//
//        HashMap<String, String> pathToTagString =
//                TagDbIndex.importExportPathToTagStringMap(db);
//
//        File dir = Configuration.getScreenshotDirectory();
//
//        return MnemoSyneUtils.getImageGridItems(pathToTagString, dir);
//    }

    public void setGridAdapter(ImageGridAdapter adapter) {
        synchronized (this) {
            ensureGrid();
            mImageGridAdapter = adapter;
            mImageGrid.setAdapter(adapter);
        }
    }

    private void ensureGrid() {
        if (mImageGrid != null) {
            return;
        }
        setContentView(R.layout.activity_image_grid);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        //View emptyView = findViewById(com.android.internal.R.id.empty);
        mImageGrid = (GridView) findViewById(R.id.gvImageGrid);
        if (mImageGrid == null) {
            throw new RuntimeException(
                    "Image Grid Not Found");
        }

        mImageGrid.setOnItemClickListener(mOnClickListener);
        if (mFinishedStart) {
            setGridAdapter(mImageGridAdapter);
        }
        mHandler.post(mRequestFocus);
        mFinishedStart = true;
    }

    private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mImageGrid.focusableViewAvailable(mImageGrid);
        }
    };

    public ImageGridAdapter getListAdapter() {
        return mImageGridAdapter;
    }

//    private ArrayList<ImageGridItem> getImages() {
//
//        File dir = Configuration.getScreenshotDirectory();
//
//        asdf; //this needs to become async like image list v5
//        return UtilsMnemosyneV5.getImageGridItems(dir);
//    }
}
