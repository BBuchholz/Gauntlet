package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

public class ImageDisplayActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGEPATH =
            "com.nineworldsdeep.gauntlet.IMAGEDISPLAY_IMAGE_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String s = i.getStringExtra(EXTRA_IMAGEPATH);

        if(s != null){

//            Utils.toast(this, s);

            Bitmap bmp = BitmapFactory.decodeFile(s);
            ImageView img = (ImageView) findViewById(R.id.ivImage);
            img.setImageBitmap(bmp);

        }else{

            Utils.toast(this, "image path null");
        }
    }

}
