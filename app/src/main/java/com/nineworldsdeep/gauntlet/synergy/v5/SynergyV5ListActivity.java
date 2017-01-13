package com.nineworldsdeep.gauntlet.synergy.v5;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.ListBaseActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.mnemosyne.AudioListV2Activity;
import com.nineworldsdeep.gauntlet.mnemosyne.ImageListV2Activity;
import com.nineworldsdeep.gauntlet.mnemosyne.MnemoSyneUtils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SynergyV5ListActivity
        extends ListBaseActivity{

    private static final int MENU_CONTEXT_COMPLETION_STATUS_ID = 1;
    private static final int MENU_CONTEXT_SHELVE_ID = 2;
    private static final int MENU_CONTEXT_QUEUE_ID = 3;
    private static final int MENU_CONTEXT_MOVE_TO_TOP_ID = 4;
    private static final int MENU_CONTEXT_MOVE_TO_BOTTOM_ID = 5;
    private static final int MENU_CONTEXT_SPLIT_ITEM_ID = 6;
    public static final int REQUEST_RESULT_SPLIT_ITEM = 7;
    private static final int MENU_CONTEXT_MOVE_TO_LIST_ID = 8;
    private static final int MENU_CONTEXT_MOVE_TO_LYRICS_ID = 9;
    private static final int MENU_CONTEXT_MOVE_TO_FRAGMENTS_ID = 10;
    private static final int MENU_CONTEXT_EDIT_ITEM = 11;
    public static final int REQUEST_RESULT_EDIT_ITEM = 12;
    private static final int MENU_CONTEXT_MOVE_UP_ID = 13;
    private static final int MENU_CONTEXT_MOVE_DOWN_ID = 14;
    private static final int MENU_CONTEXT_OPEN_MEDIA_AUDIO = 15;
    private static final int MENU_CONTEXT_OPEN_VM_AUDIO = 16;
    private static final int MENU_CONTEXT_OPEN_MEDIA_IMAGES = 17;
    private static final int MENU_CONTEXT_OPEN_VM_SCREENSHOTS = 18;
    private static final int MENU_CONTEXT_COPY_NAME_TO_CLIPBOARD = 19;
    private static final int MENU_CONTEXT_ACTIVATE = 20;
    private static final int MENU_CONTEXT_ARCHIVE = 21;

    private SynergyV5List mSynLst;
    private ArrayList<SynergyV5ListItem> mCurrentItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synergy_list_v5);

        populateStatusSpinner();

        //set listName
        Intent intent = getIntent();

        String listName =
                intent.getStringExtra(
                        SynergyV5MainActivity.EXTRA_SYNERGYMAIN_LISTNAME);

        setTitle(listName + "(V5)");

        mSynLst = new SynergyV5List(listName);
        mCurrentItems = new ArrayList<>();

        refreshLayout();
    }

    private void populateStatusSpinner() {

        Spinner spListItemStatus = (Spinner)this.findViewById(R.id.spListItemStatus);

        ArrayList<String> lst = new ArrayList<>();
        lst.add("Active");
        lst.add("Archived");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, lst);

        spListItemStatus.setAdapter(adapter);
    }

    @Override
    protected ListView getListView(){

        return (ListView)findViewById(R.id.lvItems);
    }

    @Override
    protected void setupListViewListener(final ListView lvItems) {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int idx,
                                    long id) {

                //get selected list name
                SynergyV5ListItem selectedItem =
                        (SynergyV5ListItem) lvItems.getItemAtPosition(idx);

                if(selectedItem.getItemValue().toLowerCase().startsWith("see ")){

                    String listName = null;

                    if(selectedItem.getItemValue()
                            .toLowerCase().startsWith("see list ")){

                        listName =
                                selectedItem.getItemValue().toLowerCase()
                                        .replace("see list ", "").trim();
                    }else{

                        listName = selectedItem.getItemValue().toLowerCase()
                                .replace("see ", "").trim();
                    }

                    if(SynergyV5Utils.listExists(listName)){

                        Intent intent = new Intent(view.getContext(),
                                SynergyV5ListActivity.class);

                        intent.putExtra(SynergyV5MainActivity.EXTRA_SYNERGYMAIN_LISTNAME,
                                        listName);

                        startActivity(intent);

                    }else{

                        Utils.toast(SynergyV5ListActivity.this,
                                "Unable to navigate to list name (not found): "
                                        + listName);
                    }
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        String title = mCurrentItems.get(info.position).getItemValue();

        menu.setHeaderTitle(title);

        menu.add(Menu.NONE, MENU_CONTEXT_COMPLETION_STATUS_ID, Menu.NONE, "Toggle Completed Status");


        if(getSelectedStatus().equalsIgnoreCase("archived")){

            menu.add(Menu.NONE, MENU_CONTEXT_ACTIVATE, Menu.NONE, "Activate");

        }else{

            menu.add(Menu.NONE, MENU_CONTEXT_ARCHIVE, Menu.NONE, "Archive");
        }

        menu.add(Menu.NONE, MENU_CONTEXT_COPY_NAME_TO_CLIPBOARD, Menu.NONE, "Copy itemValue to Clipboard");

        Matcher vmMatcher = Pattern.compile("\\(\\bhas VoiceMemo: \\d{4,14}\\b\\)")
                .matcher(mCurrentItems.get(info.position).getItemValue());

        if (vmMatcher.find()) {

            menu.add(Menu.NONE, MENU_CONTEXT_OPEN_MEDIA_AUDIO,
                    Menu.NONE, "Open MEDIA audio");

            menu.add(Menu.NONE, MENU_CONTEXT_OPEN_VM_AUDIO,
                    Menu.NONE, "Open VM Audio");
        }

        Matcher imgMatcher = Pattern.compile("\\(\\bhas Image: \\d{4,14}\\b\\)")
            .matcher(mCurrentItems.get(info.position).getItemValue());

        if (imgMatcher.find()) {

            menu.add(Menu.NONE, MENU_CONTEXT_OPEN_MEDIA_IMAGES,
                    Menu.NONE, "Open MEDIA images");

            menu.add(Menu.NONE, MENU_CONTEXT_OPEN_VM_SCREENSHOTS,
                    Menu.NONE, "Open Screenshots");
        }

        if(mSynLst.getListName().startsWith("Fragments")){

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_LYRICS_ID,
                    Menu.NONE, "Move To Lyrics");

        }else{

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_TOP_ID,
                    Menu.NONE, "Move To Top");

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_BOTTOM_ID,
                    Menu.NONE, "Move To Bottom");

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_UP_ID,
                    Menu.NONE, "Move Up");

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_DOWN_ID,
                    Menu.NONE, "Move Down");

        }

        if(mSynLst.getListName().startsWith("Lyric")){

            menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_FRAGMENTS_ID,
                    Menu.NONE, "Move To Fragments");
        }

        menu.add(Menu.NONE, MENU_CONTEXT_MOVE_TO_LIST_ID,
                Menu.NONE, "Move To List");

//        menu.add(Menu.NONE, MENU_CONTEXT_SPLIT_ITEM_ID,
//                Menu.NONE, "Split Item");
//
//        menu.add(Menu.NONE, MENU_CONTEXT_EDIT_ITEM,
//                Menu.NONE, "Edit Item");
    }

    //adapted from:
    // http://stackoverflow.com/questions/18632331/using-contextmenu-with-listview-in-android
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case MENU_CONTEXT_COMPLETION_STATUS_ID:

                toggleCompletionStatusAtPosition(info.position);

                return true;

            case MENU_CONTEXT_ACTIVATE:

                activate(info.position);

                return true;

            case MENU_CONTEXT_ARCHIVE:

                archive(info.position);

                return true;

            case MENU_CONTEXT_QUEUE_ID:

                queuePosition(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_LYRICS_ID:

                moveToLyrics(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_FRAGMENTS_ID:

                moveToFragments(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_TOP_ID:

                moveToTop(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_BOTTOM_ID:

                moveToBottom(info.position);

                return true;

            case MENU_CONTEXT_MOVE_UP_ID:

                moveUp(info.position);

                return true;

            case MENU_CONTEXT_MOVE_DOWN_ID:

                moveDown(info.position);

                return true;

            case MENU_CONTEXT_MOVE_TO_LIST_ID:

                moveToList(info.position);

                return true;

            case MENU_CONTEXT_SPLIT_ITEM_ID:

                splitItem(info.position);

                return true;

            case MENU_CONTEXT_EDIT_ITEM:

                editItem(info.position);

                return true;

            case MENU_CONTEXT_OPEN_MEDIA_AUDIO:

                openAudio(info.position, true);

                return true;

            case MENU_CONTEXT_OPEN_VM_AUDIO:

                openAudio(info.position, false);

                return true;

            case MENU_CONTEXT_OPEN_MEDIA_IMAGES:

                openImage(info.position, true);

                return true;

            case MENU_CONTEXT_OPEN_VM_SCREENSHOTS:

                openImage(info.position, false);

                return true;

            case MENU_CONTEXT_COPY_NAME_TO_CLIPBOARD:

                copyListNameToClipboard(info.position);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private String getSelectedStatus(){

        Spinner spStatus = (Spinner)findViewById(R.id.spListItemStatus);

        return spStatus.getSelectedItem().toString();
    }

    private void copyListNameToClipboard(int position) {

        String text = mCurrentItems.get(position).getItemValue();
        String label = "synergy-list-name";

        SynergyV5Utils.copyToClipboard(this, label, text);

        Utils.toast(this, "[" + text + "] copied to clipboard.");
    }

    private void editItem(int position) {

        Utils.toast(this, "in progress");
//        Intent intent = new Intent(this, SynergyV5EditItemActivity.class);
//
//        intent.putExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, position);
//        intent.putExtra(SynergyV5EditItemActivity.STRING_SYNERGY_V5_ITEM_VALUE,
//                mSynLst.get(position).getItemValue());
//
//        startActivityForResult(intent, REQUEST_RESULT_EDIT_ITEM);
    }

    private void openAudio(int position, boolean mediaNotVoiceMemos){

        String timeStampFilters =
                MnemoSyneUtils
                        .extractTimeStampFilters(mCurrentItems.get(position).getItemValue());

        Intent intent = new Intent(this, AudioListV2Activity.class);

        String pathForAudio;

        if(mediaNotVoiceMemos){

            pathForAudio = Configuration.getAudioDirectory().getAbsolutePath();

        }else{

            pathForAudio = Configuration.getVoicememosDirectory().getAbsolutePath();
        }

        intent.putExtra(AudioListV2Activity.EXTRA_CURRENT_PATH, pathForAudio);

        intent.putExtra(AudioListV2Activity.EXTRA_TIMESTAMP_FILTER,
                timeStampFilters);

        Utils.toast(this, "opening NWD-MEDIA/audio");

        startActivity(intent);
    }

    private void openImage(int position, boolean mediaNotScreenshots){

        String timeStampFilters =
                MnemoSyneUtils
                        .extractTimeStampFilters(mCurrentItems.get(position).getItemValue());

        Intent intent = new Intent(this, ImageListV2Activity.class);

        String pathForImage;

        if(mediaNotScreenshots){

            pathForImage = Configuration.getImagesDirectory().getAbsolutePath();

        }else{

            pathForImage = Configuration.getScreenshotDirectory().getAbsolutePath();
        }

        intent.putExtra(ImageListV2Activity.EXTRA_CURRENT_PATH, pathForImage);

        intent.putExtra(ImageListV2Activity.EXTRA_TIMESTAMP_FILTER,
                timeStampFilters);

        Utils.toast(this, "opening images...");

        startActivity(intent);
    }

    private void splitItem(int position) {

        Utils.toast(this, "in progress");
//        Intent intent = new Intent(this, SynergyV5SplitItemActivity.class);
//
//        intent.putExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, position);
//        intent.putExtra(Extras.STRING_SYNERGY_LIST_ITEM_TEXT,
//                mSynLst.get(position).getItemValue());
//
//        startActivityForResult(intent, REQUEST_RESULT_SPLIT_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQUEST_RESULT_SPLIT_ITEM && data != null)
        {
            int pos = data.getIntExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, -1);

            if(pos > -1) {

                ArrayList<String> lst = (ArrayList<String>)
                        data.getSerializableExtra(Extras.ARRAYLIST_STRING_LIST_ITEMS);

                if(lst != null) {

                    ArrayList<SynergyV5ListItem> sliList = new ArrayList<>();

                    for(String s : lst){
                        sliList.add(new SynergyV5ListItem(s));
                    }

                    // need to specify false for archiveOne() or else
                    // remove gets called twice if sliList has only one item
                    mSynLst.archiveOne(mSynLst.replace(pos, sliList), false);
                    mSynLst.sync(this, NwdDb.getInstance(this));
                    refreshListItems();

                }
            }
        }

        if(requestCode== REQUEST_RESULT_EDIT_ITEM && data != null){

            int pos = data.getIntExtra(Extras.INT_SYNERGY_LIST_ITEM_POS, -1);

            if(pos > -1){

                String newRawText =
                        data.getStringExtra(Extras.STRING_SYNERGY_LINE_ITEM_RAW_TEXT);

                Utils.toast(this, pos + " : [" + newRawText + "]");

                // just a hack cuz i don't feel like
                // overloading the replace method right now
                ArrayList<SynergyV5ListItem> lst = new ArrayList<>();
                lst.add(new SynergyV5ListItem(newRawText));

                // need to specify false for archiveOne() or else
                // remove gets called twice if sliList has only one item
                mSynLst.archiveOne(mSynLst.replace(pos, lst), false);
                mSynLst.sync(this, NwdDb.getInstance(this));
                refreshListItems();
            }
        }

        if(requestCode== RESULT_CANCELED){
            //do nothing
            //it crashes on back button without this, see:
            //http://stackoverflow.com/questions/20782619/failure-delivering-result-resultinfo
        }
    }

    private void moveToBottom(int pos) {

        mSynLst.moveToBottom(mCurrentItems.get(pos).getItemValue());
        mSynLst.sync(this, NwdDb.getInstance(this));

        refreshListItems();
    }

    private void moveDown(int pos) {

        mSynLst.moveDown(mCurrentItems.get(pos).getItemValue());
        mSynLst.sync(this, NwdDb.getInstance(this));

        refreshListItems();
    }

    private void moveToTop(int pos) {

        mSynLst.moveToTop(mCurrentItems.get(pos).getItemValue());
        mSynLst.sync(this, NwdDb.getInstance(this));

        refreshListItems();
    }

    private void moveUp(int pos) {

        mSynLst.moveUp(mCurrentItems.get(pos).getItemValue());
        mSynLst.sync(this, NwdDb.getInstance(this));

        refreshListItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_synergy_list_v5, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_archive){

            promptConfirmArchive();
            return true;
        }

        if (id == R.id.action_go_to_home_screen){

            NavigateActivityCommand.navigateTo(
                    HomeListActivity.class, this
            );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptUpdateTemplate() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String trimmedName = Utils.trimTimeStamp_yyyyMMdd(mSynLst.getListName());

        String msg = "Update Template: " + trimmedName + "?";

        builder.setTitle("UpdateTemplate")
                .setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        SynergyV5Utils.updateTemplate(trimmedName, mSynLst);
                        Utils.toast(getApplicationContext(), "template updated");
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void promptConfirmArchive(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //disabling archive all expired, causing usability issues, this is the easiest way
        //final boolean expired = Utils.isTimeStampExpired_yyyyMMdd(mSynLst.getListName());
        final boolean expired = false;

        String msg;

        if(expired){

            msg = "Archive all tasks?";

        }else{

            msg = "Archive completed tasks?";
        }

        builder.setTitle("Archive Tasks")
                .setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Context ctx = getApplicationContext();
                        mSynLst.archiveCompleted();
                        mSynLst.sync(ctx, NwdDb.getInstance(ctx));

                        //Utils.toast(getApplicationContext(), "in progress");
                        //readItems(mSynLst.getListName());
                        readItems();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void queuePosition(int position) {

        //let's try to allow queing from timestamped lists, as active queue isn't one of them anymore
//        if(!Utils.containsTimeStamp(mSynLst.getListName()) && !mSynLst.getListName().startsWith("000-")){

        if(!SynergyV5Utils.isActiveQueue(mSynLst.getListName())){

            //Utils.toast(this, "queueToActive position " + position);
            mSynLst.queueToActive(position);
            Utils.toast(this, "queued");
            refreshListItems();

        }else{

            Utils.toast(this, "Queue only applies to non-timestamped lists");
        }
    }

    private void moveToLyrics(final int position){

        SynergyV5ListItem itemAtPosition =
                mCurrentItems.get(position);

        if(itemAtPosition != null) {

            SynergyV5ListItem sli =
                    mSynLst.getCopyForItemValue(itemAtPosition.getItemValue());

            SynergyV5Utils.move(mSynLst,
                    sli,
                    "Lyrics",
                    NwdDb.getInstance(this),
                    this);

            Utils.toast(getApplicationContext(), "moved to Lyrics");

            refreshLayout();
        }
    }

    private void moveToFragments(final int position){

        SynergyV5ListItem itemAtPosition =
                mCurrentItems.get(position);

        if(itemAtPosition != null) {

            SynergyV5ListItem sli =
                    mSynLst.getCopyForItemValue(itemAtPosition.getItemValue());

            SynergyV5Utils.move(mSynLst,
                    sli,
                    "Fragments",
                    NwdDb.getInstance(this),
                    this);

            Utils.toast(getApplicationContext(), "moved to Fragments");

            refreshLayout();
        }
    }

    private void moveToList(final int position) {

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt, null);

        TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
        tv.setText("Enter listName: ");

        android.app.AlertDialog.Builder alertDialogBuilder =
                new android.app.AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        userInput.setText(Configuration.getMostRecentMoveToList());
        userInput.setSelection(userInput.getText().length());

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                // get list name from userInput and move
                                String processedName =
                                        Utils.processName(
                                                userInput.getText().toString());

                                Configuration.setMostRecentMoveToList(processedName);

                                Context context = getApplicationContext();

                                SynergyV5ListItem itemAtPosition =
                                        mCurrentItems.get(position);

                                if(itemAtPosition != null) {

                                    SynergyV5ListItem sli =
                                            mSynLst.getCopyForItemValue(
                                                    itemAtPosition.getItemValue());

                                    SynergyV5Utils.move(mSynLst,
                                            sli,
                                            processedName,
                                            NwdDb.getInstance(context),
                                            context);

                                    Utils.toast(getApplicationContext(),
                                            "moved to " + processedName);

                                    refreshLayout();

                                }else{

                                    Utils.toast(getApplicationContext(),
                                            "item null");
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
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void toggleCompletionStatusAtPosition(int position){

        SynergyV5ListItem item = mCurrentItems.get(position);

        if(item != null && item.isCompleted()){

            item.activate();

        }else{

            item.complete();
        }

        mSynLst.sync(this, NwdDb.getInstance(this));

        refreshLayout();

    }

    private void activate(int position){

        SynergyV5ListItem item = mCurrentItems.get(position);

        item.activate();

        mSynLst.sync(this, NwdDb.getInstance(this));

        refreshLayout();

    }

    private void archive(int position){

        SynergyV5ListItem item = mCurrentItems.get(position);

        item.archive();

        mSynLst.sync(this, NwdDb.getInstance(this));

        refreshLayout();

    }

    private void writeItems() {

        if(mSynLst != null){

            mSynLst.sync(this, NwdDb.getInstance(this));
        }
    }

    public ListView getLvItems(){

        return (ListView)findViewById(R.id.lvItems);
    }

    @Override
    protected void readItems(ListView lvItems) {

        mSynLst.sync(this, NwdDb.getInstance(this));

        setListViewAdapter(lvItems);
        setupSpinnerListener();
    }

    private void setupSpinnerListener() {

        final Spinner spStatus =
                (Spinner) findViewById(R.id.spListItemStatus);

        spStatus.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                refreshLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    private void readItems(){

        readItems(getListView());
    }

    private void setListViewAdapter(ListView lvItems) {

        String currentStatus = getSelectedStatus();

        if(currentStatus.equalsIgnoreCase("archived")){

            mCurrentItems = mSynLst.getArchivedItems();

        }else{

            mCurrentItems = mSynLst.getActiveItems();
        }

        final ArrayAdapter adapter =
                new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1,
                        mCurrentItems){

            public View getView(int position, View convertView, ViewGroup parent){

                SynergyV5ListItem currentItem = mCurrentItems.get(position);

                TextView txtView = new TextView(getContext());
                txtView.setText(currentItem.getItemValue());
                txtView.setTextSize(18);
                txtView.setPadding(15,15,15,15);

                if(currentItem.isCompleted()){

                    txtView.setPaintFlags(
                            Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                }

                return txtView;
            }
        };

        lvItems.setAdapter(adapter);

//        lvItems.setAdapter(new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1,
//                mSynLst.getActiveItems()));
    }

    private void refreshListItems(){

        setListViewAdapter(getLvItems());
    }

    public void onAddItemClick(View view) {

        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        //itemText = Utils.removeHardReturns(itemText);

        if(!Utils.stringIsNullOrWhitespace(itemText)){

            SynergyV5ListItem newItem = new SynergyV5ListItem(itemText);
            //activate in case its already in the database in a different status
            newItem.activate();
            mSynLst.add(newItem);
            etNewItem.setText("");
            writeItems();
            refreshListItems();

        }else{

            Utils.toast(this, "item cannot be empty or whitespace");
        }
    }

}
