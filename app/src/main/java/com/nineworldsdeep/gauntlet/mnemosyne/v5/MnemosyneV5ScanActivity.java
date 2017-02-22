package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.MultiMapString;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.async.AsyncOpGetFileSystemExtensionEntries;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MnemosyneV5ScanActivity extends AppCompatActivity implements IStatusActivity {

    private static final int SELECT_DIRECTORY_CODE = 1;
    private MultiMapString extensionsToPaths;
    private ArrayList<String> mCurrentPaths;
    private String currentExtension = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemosyne_v5_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extensionsToPaths = new MultiMapString();
        mCurrentPaths = new ArrayList<>();
        NwdDb.getInstance(this).open();

        if(Configuration.getLocalMediaDevice(this,
                        NwdDb.getInstance(this)) == null){

            promptSetLocalDeviceDescription();

        }else{

            //refreshLayout();
            populateSpinnerMediaDevice();
        }
    }

    public void onFilterButtonClick(View view){

        //Utils.toast(this, "filter button pressed");

        populateListViewFilePaths();

        closeKeyboard();
    }

    private void closeKeyboard(){

        if(this.getCurrentFocus() == null){

            return;
        }

        InputMethodManager inputManager =
            (InputMethodManager)
                    this.getSystemService(
                            Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(
            this.getCurrentFocus().getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void promptSetLocalDeviceDescription(){

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt, null);

        TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
        tv.setText("Enter Local Device Name: ");

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

                                // get list name from userInput and move
                                String deviceName =
                                        userInput.getText().toString();

                                deviceName = deviceName.trim().toLowerCase();

                                if(!Utils.stringIsNullOrWhitespace(deviceName)) {

                                    Configuration.ensureLocalMediaDevice(
                                            MnemosyneV5ScanActivity.this,
                                            NwdDb.getInstance(MnemosyneV5ScanActivity.this),
                                            deviceName);

                                    Utils.toast(getApplicationContext(), "stored.");

                                    refreshLayout();

                                }else{

                                    Utils.toast(getApplicationContext(), "empty name, ignored.");
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();

                                refreshLayout();
                            }
                        });

        // create alert dialog
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void refreshLayout(){

        //commenting out because
        //spinner selections should populate other spinners

//        populateSpinnerMediaDevice();
//        populateSpinnerMediaRoot();
//        populateSpinnerSourceSelectDbFs();
//        populateSpinnerFileTypes();

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

    private void populateSpinnerMediaDevice() {

        Spinner spMediaDevices = (Spinner)this.findViewById(R.id.spMediaDevice);

//        ArrayList<MediaDevice> lst = new ArrayList<>();
//
//        MediaDevice md = Configuration.getLocalMediaDevice(
//                this, NwdDb.getInstance(this));
//
//        if(md != null){
//
//            lst.add(md);
//        }

        ArrayList<MediaDevice> lst =
                NwdDb.getInstance(this).v5GetAllMediaDevices(this);

        ArrayAdapter<MediaDevice> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lst);

        spMediaDevices.setAdapter(adapter);

        spMediaDevices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                populateSpinnerSourceSelectDbFs();
                //populateSpinnerMediaRoot();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

                clearSpinner(getSourceSelectSpinner());
                //clearSpinner(getMediaRootsSpinner());
            }

        });


    }

    private void clearSpinner(Spinner sp){

        if(sp != null) {

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, new ArrayList<String>());

            sp.setAdapter(adapter);
        }
    }

    private void clearListView(ListView lv){

        if(lv != null){

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,
                    new ArrayList<String>());

            lv.setAdapter(adapter);
        }
    }

    private Spinner getMediaRootsSpinner(){

        return (Spinner)findViewById(R.id.spMediaRoot);
    }

    private Spinner getMediaDeviceSpinner(){

        return (Spinner)findViewById(R.id.spMediaDevice);
    }

    private Spinner getSourceSelectSpinner(){

        return (Spinner)findViewById(R.id.spSourceSelectDbFs);
    }

    private Spinner getFileTypesSpinner(){

        return (Spinner)findViewById(R.id.spFileTypes);
    }

    private ListView getFilePathsListView(){

        return (ListView)findViewById(R.id.lvItems);
    }

    private MediaDevice getSelectedMediaDevice(){

        Spinner spMediaDevices = getMediaDeviceSpinner();

        MediaDevice md = null;

        if(spMediaDevices != null){

            MediaDevice selected =
                    (MediaDevice)spMediaDevices.getSelectedItem();

            if(selected != null){

                md = selected;
            }
        }

        return md;
    }

    private MediaRoot getSelectedMediaRoot(){

        Spinner spMediaRoot = getMediaRootsSpinner();

        MediaRoot mediaRoot = null;

        if(spMediaRoot != null){

            MediaRoot selected =
                    (MediaRoot)spMediaRoot.getSelectedItem();

            if(selected != null){

                mediaRoot = selected;
            }
        }

        return mediaRoot;
    }

    private ExtensionEntry getSelectedExtensionEntry(){

        Spinner spFileTypes = getFileTypesSpinner();

        ExtensionEntry entry = null;

        if(spFileTypes != null){

            ExtensionEntry selected =
                    (ExtensionEntry)spFileTypes.getSelectedItem();

            if(selected != null){

                entry = selected;
            }
        }

        return entry;
    }

    private MediaScanSourceSelect getSelectedSource(){

        Spinner spSource = getSourceSelectSpinner();

        if(spSource != null){

            return (MediaScanSourceSelect)spSource.getSelectedItem();

        }else{

            return null;
        }
    }

    private void populateSpinnerMediaRoot() {

        MediaDevice md = getSelectedMediaDevice();

        if(md != null) {

            Spinner spMediaRoots = getMediaRootsSpinner();

//        ArrayList<String> lst = new ArrayList<>();
//        lst.add("/NWD/");
//        lst.add("/NWD-AUX/");
//        lst.add("/NWD-MEDIA/");
//        lst.add("/NWD-SYNC/");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_item, lst);
//
//        sp.setAdapter(adapter);
            NwdDb.getInstance(this).open();
            ArrayList<MediaRoot> lst =
                    NwdDb.getInstance(this)
                            .v5GetMediaRootsForDeviceId(
                                    md.getMediaDeviceId(),
                                    this);

            ArrayAdapter<MediaRoot> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, lst);

            spMediaRoots.setAdapter(adapter);

            spMediaRoots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    populateSpinnerFileTypes();
                    //populateSpinnerSourceSelectDbFs();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                    //clearSpinner(getSourceSelectSpinner());
                    clearSpinner(getFileTypesSpinner());
                }

            });
        }
    }

    private void populateSpinnerSourceSelectDbFs() {

        Spinner spSourceSelect = (Spinner)this.findViewById(R.id.spSourceSelectDbFs);

//        ArrayList<String> lst = new ArrayList<>();
//        lst.add("DB");
//        lst.add("FS");

        ArrayList<MediaScanSourceSelect> lst = new ArrayList<>();
        lst.add(MediaScanSourceSelect.FileSystem);
        lst.add(MediaScanSourceSelect.Database);

        ArrayAdapter<MediaScanSourceSelect> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        lst);

        spSourceSelect.setAdapter(adapter);

        spSourceSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                populateSpinnerMediaRoot();
                //populateSpinnerFileTypes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

                clearSpinner(getMediaRootsSpinner());
                //clearSpinner(getFileTypesSpinner());
            }

        });
    }

    private void clearFilter(){

        EditText etFilter = (EditText)findViewById(R.id.etFilter);

        if(etFilter != null){

            etFilter.setText("");
        }
    }

    private void populateSpinnerFileTypes() {

        Spinner spFileTypes = (Spinner)this.findViewById(R.id.spFileTypes);

//        ArrayList<String> lst = new ArrayList<>();
//        lst.add(".wav");
//        lst.add(".png");
//        lst.add(".txt");
//        lst.add(".xml");

        getStatusView().setText("");

        clearListView(getFilePathsListView());

        MediaRoot mediaRoot = getSelectedMediaRoot();

        if(mediaRoot != null) {

            MediaScanSourceSelect selectedSource = getSelectedSource();

            if(selectedSource != null) {

                extensionsToPaths = new MultiMapString();
                ArrayList<ExtensionEntry> entries = new ArrayList<>();

                try{

                    switch (selectedSource){

                        case FileSystem:

                            extensionsToPaths =
                                new AsyncOpGetFileSystemExtensionEntries(
                                        this, mediaRoot.getMediaRootPath())
                                    .executeAsync().get();

                            break;

                        case Database:

                            //not yet implemented, see desktop
                            //MediaMasterDisplay.GetDataBaseExtToPath(...);

                            Utils.toast(this, "not implemented for db yet");

                            break;
                    }

                    for(String ext : extensionsToPaths.keySet()){

                        int count = extensionsToPaths.get(ext).size();
                        entries.add(new ExtensionEntry(ext, count));
                    }

                    ArrayAdapter<ExtensionEntry> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, entries);

                    spFileTypes.setAdapter(adapter);

                }catch (Exception ex){

                    Utils.toast(this, "error scanning files: "
                            + ex.getMessage());
                }
            }

            spFileTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    populateListViewFilePaths();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                    clearListView(getFilePathsListView());
                }

        });
        }
    }

    private void populateListViewFilePaths() {

        ExtensionEntry entry = getSelectedExtensionEntry();

        mCurrentPaths = new ArrayList<>();

        if (entry != null) {

            if(currentExtension != entry.getExtension()){

                currentExtension = entry.getExtension();
                clearFilter();
            }

            ListView lvFilePaths = getFilePathsListView();

            if(lvFilePaths != null) { //never true

                NwdDb.getInstance(this).open();
                mCurrentPaths =
                        extensionsToPaths.getAsArrayList(entry.getExtension());

                mCurrentPaths = applyFilter(mCurrentPaths);

                Collections.sort(mCurrentPaths);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        R.layout.single_line_ellipsize_textview, mCurrentPaths);

                lvFilePaths.setAdapter(adapter);
                setupListViewListener();
            }
        }
    }

    private ArrayList<String> applyFilter(ArrayList<String> pathList) {

        String filter = getFilter();
        ArrayList<String> filteredList = new ArrayList<>();

        for(String path : pathList){

            if(path.toLowerCase().contains(filter.toLowerCase())){

                filteredList.add(path);
            }
        }

        return filteredList;
    }

    private String getFilter() {

        EditText etFilter = (EditText)findViewById(R.id.etFilter);
        String filter = etFilter.getText().toString();
        filter = filter.trim();

        return filter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mnemosyne_v5_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){


        int id = item.getItemId();

        if (id == R.id.action_go_to_home_screen){

            NavigateActivityCommand.navigateTo(
                    HomeListActivity.class, this
            );
            return true;
        }

        if (id == R.id.action_add_media_root){

            String path = "media/root/path/goes/here/";

            Intent i = new Intent(this, FilePickerActivity.class);

            i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
            i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);

            i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory());

            startActivityForResult(i, SELECT_DIRECTORY_CODE);

            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_DIRECTORY_CODE &&
                resultCode == Activity.RESULT_OK) {

            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {

                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    ClipData clip = data.getClipData();

                    if (clip != null) {

                        for (int i = 0; i < clip.getItemCount(); i++) {

//                            Uri uri = clip.getItemAt(i).getUri();
//
//                            //handle multiples when and if implemented
                        }
                    }
                }

            } else {

                Uri uri = data.getData();

                File f = new File(uri.getPath());

                int localDeviceId =
                        Configuration.getLocalMediaDevice(
                                this,
                                NwdDb.getInstance(this))
                        .getMediaDeviceId();

                NwdDb.getInstance(this).open();
                NwdDb.getInstance(this).insertMediaRoot(this, localDeviceId, f);

                Utils.toast(this, "inserted media root: " + f.getAbsolutePath());

                populateSpinnerMediaRoot();
            }

        } else {

            Utils.toast(this, "Nothing selected...");
        }

    }

    private void setupListViewListener() {

        final ListView lvItems = getFilePathsListView();
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int idx,
                                    long id) {

                String path = mCurrentPaths.get(idx);
                Utils.toast(MnemosyneV5ScanActivity.this,
                        "selected: " + path);
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView,
                                           View view,
                                           int idx,
                                           long l) {

                File f = new File(mCurrentPaths.get(idx));

                if(f.exists() && f.isFile()){

                    Intent target = new Intent(Intent.ACTION_VIEW);

                    String mimeType = UtilsMnemosyneV5.getMimeType(f);

                    //target.setDataAndType(Uri.fromFile(f),"*/*");
                    target.setDataAndType(Uri.fromFile(f), mimeType);
                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    try{

                        startActivity(target);

                    }catch (ActivityNotFoundException ex){

                        Utils.toast(MnemosyneV5ScanActivity.this,
                                "error opening file: " +
                                f.getAbsolutePath());
                    }

                }

                return true;
            }
        });
    }

    public void updateStatus(String status){

        TextView tv = getStatusView();
        tv.setText(status);
    }

    protected TextView getStatusView() {

        return (TextView)findViewById(R.id.tvStatus);
    }

    @Override
    public Activity getAsActivity() {
        return this;
    }
}
