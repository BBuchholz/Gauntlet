package com.nineworldsdeep.gauntlet.tapestry;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;
import java.util.List;

public class TapestryNodeActivity extends AppCompatActivity {

    private String mCurrentNodeName;
    private ArrayList<TapestryNodeLink> mCurrentNodeLinks;

    public static final String EXTRA_CURRENT_NODE_NAME =
            "com.nineworldsdeep.gauntlet.tapestry.CURRENT_NODE_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapestry_node);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        mCurrentNodeName = i.getStringExtra(EXTRA_CURRENT_NODE_NAME);

        if(Utils.stringIsNullOrWhitespace(mCurrentNodeName)){

            mCurrentNodeName = "MotherNode";
        }

        setTitle(mCurrentNodeName);

        refreshLayout();
    }

    private void refreshLayout() {

        ListView lvItems =
                (ListView) findViewById(R.id.lvItems);

        loadItems();
        setupListViewListener();
        registerForContextMenu(lvItems);
    }

    private void setupListViewListener() {

        ListView lvItems = (ListView)findViewById(R.id.lvItems);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                TapestryNodeLink link =
                        mCurrentNodeLinks.get(position);

                Intent intent = new Intent(view.getContext(),
                        TapestryNodeActivity.class);
                intent.putExtra(
                        TapestryNodeActivity.EXTRA_CURRENT_NODE_NAME,
                        link.getNodeName());
                startActivity(intent);
            }
        });
    }

    private void loadItems() {

        ListView lvItems = (ListView)findViewById(R.id.lvItems);

        mCurrentNodeLinks=
                TapestryUtils.getNodeLinks(mCurrentNodeName);

        SimpleAdapter saLinks =
                new SimpleAdapter(
                        this.getBaseContext(),
                        mCurrentNodeLinks,
                        TapestryNodeLink.getLayout(),
                        TapestryNodeLink.getMapKeysForView(),
                        TapestryNodeLink.getIdsForViewElements());

        lvItems.setAdapter(saLinks);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        refreshLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tapestry_node, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_link) {

            promptAddLink();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptAddLink() {

        final String fromNodeName = mCurrentNodeName;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Link Type")
                .setItems(getLinkTypes(),
                          new DialogInterface.OnClickListener(){

                              @Override
                              public void onClick(DialogInterface dialog, int which) {

                                  final int idx = which;

                                  LayoutInflater li = LayoutInflater.from(TapestryNodeActivity.this);
                                  View promptsView = li.inflate(R.layout.prompt, null);

                                  TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
                                  tv.setText("Enter Node Name: ");

                                  android.app.AlertDialog.Builder alertDialogBuilder =
                                          new android.app.AlertDialog.Builder(TapestryNodeActivity.this);

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

                                                          TapestryUtils.link(
                                                                  TapestryNodeActivity.this,
                                                                  fromNodeName,
                                                                  processedName,
                                                                  LinkType.valueOf(
                                                                          getLinkTypes()[idx]));

                                                          refreshLayout();

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
                          })
                .show();
    }

    private String[] getLinkTypes() {

        return new String[] {
                LinkType.ParentLink.toString(),
                LinkType.ChildLink.toString(),
                LinkType.PeerLink.toString()
        };
    }
}
