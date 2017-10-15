package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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

                        MnemosyneRegistry.register(mli);
                        //mli.hashMedia();
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

        setGridAdapter(new ImageGridAdapter(
                            this,
                            R.layout.image_item,
                            new ArrayList<ImageGridItem>()));

        new AsyncLoadItems().execute(Configuration.getScreenshotDirectory());
    }


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

}
