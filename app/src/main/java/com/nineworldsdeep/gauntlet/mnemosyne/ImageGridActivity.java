package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.ImageDisplayV5Activity;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageGridActivity extends AppCompatActivity {

    private GridView mImageGrid;
    private ImageGridAdapter mImageGridAdapter;

    private static final String GRID_STATE = "gridState";
    private Parcelable mGridState = null;

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

    private class AsyncItemLoader extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {

            String result;

            try{

                long start = System.nanoTime();

                publishProgress("loading items...");
                mImageGridAdapter = loadItems();

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

            updateStatus(result);

            if(mImageGridAdapter != null){

                mImageGrid = (GridView) findViewById(R.id.gvImageGrid);

                mImageGrid.setAdapter(mImageGridAdapter);

                setupClickListener();

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

        AsyncItemLoader ail = new AsyncItemLoader();
        ail.execute();
    }

    private ImageGridAdapter loadItems(){

        return new ImageGridAdapter(this,
                        R.layout.image_item, getImages());
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

    private ArrayList<ImageGridItem> getImages() {

        NwdDb db = NwdDb.getInstance(this);

        db.open();

        HashMap<String, String> pathToTagString =
                TagDbIndex.importExportPathToTagStringMap(db);

        File dir = Configuration.getScreenshotDirectory();

        asdf; //return MnemoSyneV5Utils.getImageMediaListItems(dir);
        return MnemoSyneUtils.getImageGridItems(pathToTagString, dir);
    }

}
