package com.nineworldsdeep.gauntlet.mnemosyne;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageGridActivity extends AppCompatActivity {

    private GridView mImageGrid;
    private ImageGridAdapter mImageGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageGrid = (GridView) findViewById(R.id.gvImageGrid);
        mImageGridAdapter =
                new ImageGridAdapter(this,
                        R.layout.image_item, getImages());

        mImageGrid.setAdapter(mImageGridAdapter);
    }

    private ArrayList<ImageGridItem> getImages() {

        NwdDb db = NwdDb.getInstance(this);

        db.open();

        HashMap<String, String> pathToTagString =
                TagDbIndex.importExportPathToTagStringMap(db);

        File dir = Configuration.getScreenshotDirectory();

        return MnemoSyneUtils.getImageGridItems(pathToTagString, dir);
    }

}
