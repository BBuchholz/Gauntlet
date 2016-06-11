package com.nineworldsdeep.gauntlet.mnemosyne;

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
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.DisplayNameDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.tapestry.ConfigFile;
import com.nineworldsdeep.gauntlet.tapestry.TapestryUtils;

import java.io.File;

public class ImageDisplayActivity extends AppCompatActivity {

    private FileListItem ili;

    //private NwdDb db;

    public static final String EXTRA_IMAGEPATH =
            "com.nineworldsdeep.gauntlet.IMAGEDISPLAY_IMAGE_PATH";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_set_display_name) {

            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.prompt, null);

            AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(this);

            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            String currentValue = "";

            //don't prepopulate if it's just returning the file name
            if(!ili.getDisplayName()
                    .equalsIgnoreCase(ili.getFile().getName())){

                currentValue = ili.getDisplayName();
            }

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

//                                        ili.setAndSaveDisplayName(
//                                                userInput.getText().toString(),
//                                                db);

                                        DisplayNameDbIndex
                                                .setDisplayNameAndExportFile(
                                                        ili.getFile().getAbsolutePath(),
                                                        userInput.getText()
                                                                .toString(),
                                                        NwdDb.getInstance(ImageDisplayActivity.this));

                                    } catch (Exception e) {

                                        Utils.toast(ImageDisplayActivity.this,
                                                "error setting display name: " +
                                        e.getMessage());
                                    }
                                    //DisplayNameIndex.getInstance().save();
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

        } else if (id == R.id.action_set_tag_string) {

            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.prompt, null);

            AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(this);

            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            String currentValue = ili.getTags();

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
                                        ili.setAndSaveTagString(
                                                userInput.getText().toString());
                                    } catch (Exception e) {

                                        Utils.toast(ImageDisplayActivity.this,
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

        } else if(id == R.id.action_seed){

            String currentDevice = TapestryUtils.getCurrentDeviceName();

            if(currentDevice == null) {
                //prompt for one
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.prompt, null);

                TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
                tv.setText("No Device Set, Enter Device Name, Then Try Again: ");

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        String name = userInput.getText().toString();

                                        //prevent hyphens, which are used for junctions
                                        name = name.replace("-", "_");

                                        ConfigFile f = new ConfigFile();
                                        f.setDeviceName(name);
                                        f.save();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                Utils.toast(this, "seed discarded");

            }else{

                String currentGardenName = TapestryUtils.getCurrentGardenName(currentDevice);

                TapestryUtils
                        .linkNodeToImagePath(currentGardenName,
                                ili.getFile().getAbsolutePath());

                Utils.toast(this, "seed planted: " + currentGardenName);
            }

            return true;

        } else if(id == R.id.action_seed_new){

            String currentDevice = TapestryUtils.getCurrentDeviceName();

            if(currentDevice == null) {
                //prompt for one
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.prompt, null);

                TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
                tv.setText("No Device Set, Enter Device Name, Then Try Again: ");

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        String name = userInput.getText().toString();

                                        //prevent hyphens, which are used for junctions
                                        name = name.replace("-", "_");

                                        ConfigFile f = new ConfigFile();
                                        f.setDeviceName(name);
                                        f.save();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                Utils.toast(this, "seed discarded");

            }else{

                String currentGardenName = TapestryUtils.getNewGardenName(currentDevice);

                TapestryUtils
                        .linkNodeToImagePath(currentGardenName,
                                ili.getFile().getAbsolutePath());

                Utils.toast(this, "seed planted: " + currentGardenName);
            }

            return true;

        } else if (id == R.id.action_link_to_node){

            LayoutInflater li = LayoutInflater.from(ImageDisplayActivity.this);
            View promptsView = li.inflate(R.layout.prompt, null);

            TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
            tv.setText("Enter Node Name: ");

            android.app.AlertDialog.Builder alertDialogBuilder =
                    new android.app.AlertDialog.Builder(ImageDisplayActivity.this);

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

                                    // get list name from userInput and move
                                    String processedName =
                                            TapestryUtils.processNodeName(
                                                    userInput.getText().toString());

                                    TapestryUtils
                                            .linkNodeToImagePath(processedName,
                                                    ili.getFile().getAbsolutePath());

                                    Utils.toast(ImageDisplayActivity.this, "linked");

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //assignDb();

        Intent i = getIntent();
        final String path = i.getStringExtra(EXTRA_IMAGEPATH);

        if(path != null){

            NwdDb db = NwdDb.getInstance(this);

            db.open();

            ili = new FileListItem(path, db.getDisplayNameForPath(path));
            Bitmap bmp = BitmapFactory.decodeFile(path);
            ImageView img = (ImageView) findViewById(R.id.ivImage);
            img.setImageBitmap(bmp);

            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //Utils.toast(v.getContext(), path);

                    //from:http://stackoverflow.com/questions/19422075/open-a-selected-file-image-pdf-programmatically-from-my-android-applicat
                    Uri uri = Uri.fromFile(new File(path));
                    Intent imageIntent = new Intent(Intent.ACTION_VIEW);
                    imageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    imageIntent.setDataAndType(uri, "image/*");

                    try{
                        startActivity(imageIntent);
                    }catch (ActivityNotFoundException ex){
                        Utils.toast(v.getContext(), "error opening image");
                    }

                    return true;
                }
            });

        }else{

            Utils.toast(this, "image path null");
        }
    }


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

    @Override
    protected void onResume() {

        super.onResume();

        NwdDb.getInstance(this).open();

        //assignDb();

        //moved to assignDb() //db.open();
    }

    @Override
    protected void onPause() {

        super.onPause();

        NwdDb.getInstance(this).close();
    }
}
