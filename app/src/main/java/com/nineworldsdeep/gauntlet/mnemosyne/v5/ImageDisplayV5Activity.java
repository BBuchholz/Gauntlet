package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Tags;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.mnemosyne.FileListItem;
import com.nineworldsdeep.gauntlet.mnemosyne.MnemoSyneUtils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ImageDisplayV5Activity extends AppCompatActivity {

    private MediaListItem currentMediaListItem;

    public static final String EXTRA_IMAGE_PATH =
            "com.nineworldsdeep.gauntlet.IMAGE_DISPLAY_IMAGE_PATH";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_image_display_v5, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.action_set_tag_string:

                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.prompt, null);

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(this);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                String currentValue = currentMediaListItem.getTags();

                if(!Utils.stringIsNullOrWhitespace(currentValue)){
                    userInput.setText(currentValue);
                }

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        try {

                                            NwdDb db =
                                                    NwdDb.getInstance(ImageDisplayV5Activity.this);

                                            currentMediaListItem.setTagsFromTagString(
                                                    userInput.getText().toString());

                                            //db.sync(currentMediaListItem.getMedia());
                                            MnemosyneRegistry.sync(currentMediaListItem, db);

                                        } catch (Exception e) {

                                            Utils.toast(ImageDisplayV5Activity.this,
                                                    "error setting tag string: " +
                                            e.getMessage());
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                return true;

            case R.id.action_go_to_home_screen:

                NavigateActivityCommand.navigateTo(
                        HomeListActivity.class, this
                );

                return true;

            case R.id.action_mark_with_tag_done:

                try {

                    String currentTags = currentMediaListItem.getTags();

                    if(currentTags.length() > 0){

                        currentTags += ", ";
                    }

                    currentTags += "done";

                    NwdDb db =
                            NwdDb.getInstance(ImageDisplayV5Activity.this);

                    currentMediaListItem.setTagsFromTagString(currentTags);

                    MnemosyneRegistry.sync(currentMediaListItem, db);

                    Utils.toast(ImageDisplayV5Activity.this,
                            "tagged \"done\" successfully");

                } catch (Exception e) {

                    Utils.toast(ImageDisplayV5Activity.this,
                            "error setting tag string: " +
                    e.getMessage());
                }

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        final String path = i.getStringExtra(EXTRA_IMAGE_PATH);

        if (path != null) {

            try {
                NwdDb db = NwdDb.getInstance(this);
                db.open();

//                setCurrentMediaListItem(new MediaListItem(path));
                setCurrentMediaListItem(
                        MnemosyneRegistry.tryGetMediaListItem(new File(path)));

                //db.sync(currentMediaListItem.getMedia());
                MnemosyneRegistry.sync(currentMediaListItem, db);

                Bitmap bmp = BitmapFactory.decodeFile(path);
                ImageView img = (ImageView) findViewById(R.id.ivImage);
                img.setImageBitmap(bmp);

                img.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //from:http://stackoverflow.com/questions/19422075/open-a-selected-file-image-pdf-programmatically-from-my-android-applicat
                        Uri uri = Uri.fromFile(new File(path));
                        Intent imageIntent = new Intent(Intent.ACTION_VIEW);
                        imageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        imageIntent.setDataAndType(uri, "image/*");

                        try {
                            startActivity(imageIntent);
                        } catch (ActivityNotFoundException ex) {
                            Utils.toast(v.getContext(), "error opening image: " + ex.getMessage());
                        }

                        return true;
                    }
                });

            }catch(Exception ex){

                Utils.toast(this,
                        "error loading media list item: " + ex.getMessage());
            }

        } else {

            Utils.toast(this, "ivImage path null");
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        NwdDb.getInstance(this).open();
    }

    @Override
    protected void onPause() {

        super.onPause();

        NwdDb.getInstance(this).close();
    }

    /**
     * will hash media after set
     * @param mli
     * @throws Exception
     */
    public void setCurrentMediaListItem(MediaListItem mli) throws Exception {

        currentMediaListItem = mli;
        MnemosyneRegistry.register(currentMediaListItem);
        //currentMediaListItem.hashMedia();
    }
}
