package com.nineworldsdeep.gauntlet.tapestry.v2;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.datamaps.SqliteDbMapperV5;
import com.nineworldsdeep.gauntlet.datamaps.FileSystemMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClusterComparisonActivity extends AppCompatActivity {

    private List<NodeComparison> mNodeComparisons;
    private ListAdapter mCurrentAdapter;
    private FileSystemMapper mFileSystemMapper;
    private SqliteDbMapperV5 mSqliteDbMapperV5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_comparison);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateRelationSpinner();
        populateSortSpinner();
        setupRelationSpinnerListener();
    }

    private void populateSortSpinner() {

        Spinner spSortBy = (Spinner)this.findViewById(R.id.spBlueBottom);

        ArrayList<String> lst = new ArrayList<>();
        lst.add("comparison result");
        lst.add("dynamic op (eg. path)");
        lst.add("dynamic op (eg. tag)");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, lst);

        spSortBy.setAdapter(adapter);
    }

    private void populateRelationSpinner() {

        Spinner spRelation = (Spinner)this.findViewById(R.id.spYellowTop);

        ArrayList<String> lst = new ArrayList<>();
//        lst.add("PathHash");
//        lst.add("HashTag");
//        lst.add("PathTag");
//        lst.add("SynergyList");
//        lst.add("Path");
//        lst.add("Hash");
//        lst.add("Tag");

        for(NodeComparisonType nct : NodeComparisonType.values()){

            lst.add(nct.name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, lst);

        spRelation.setAdapter(adapter);
    }

    //let's make the list items (RelationComparisons) like audio and
    //image list items (with multiline text and the option for icons)

    //TODO: create a base class for this activity & (Audio/Image)ListV2Activity
    //could be similar to ListBaseActivity with support for Async Load
    //and multiline ListViewItems with icons

    private void setupRelationSpinnerListener() {

        final Spinner spRelation =
                (Spinner) findViewById(R.id.spYellowTop);

        spRelation.setOnItemSelectedListener(
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

    private List<NodeComparison> runComparisons(){

        mFileSystemMapper = new FileSystemMapper();
        mSqliteDbMapperV5 = new SqliteDbMapperV5();

        List<NodeComparison> lst =
                NodeComparer.compare(mFileSystemMapper,
                        mSqliteDbMapperV5, getSelectedComparisonType());

        return lst;
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshLayout();
    }

    private void refreshLayout() {

        AsyncItemLoader ail = new AsyncItemLoader();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            ail.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            ail.execute();
    }

    private ListAdapter loadItems(){

        ArrayList<HashMap<String, String>> lstItems =
                new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map;

        mNodeComparisons = runComparisons();

        for(NodeComparison nc : mNodeComparisons){

            map = new HashMap<>();

            map.put("comparisonResult", nc.getComparisonResult().toString());
            map.put("multilineDetails", nc.getMultilineDetails());

            //map.put("img", String.valueOf(R.mipmap.ic_nwd_media));

            map.put("background", nc.getComparisonResult().toString());

            lstItems.add(map);
        }

        ListAdapter saItems =
                new SimpleAdapter(
                        this.getBaseContext(),
                        lstItems,
                        R.layout.relation_comparison,
                        new String[] { //"img",
                                "comparisonResult",
                                "multilineDetails"},
                        new int[] { //R.id.img,
                                R.id.comparison_result,
                                R.id.multiline_details}) {
            @Override
            public View getView (int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                LinearLayout layout =
                        (LinearLayout)view.findViewById(R.id.layoutColored);

                NodeComparisonResult result =
                        mNodeComparisons.get(position).getComparisonResult();

                switch (result){
                    case GreenUpsertNeither:
                        layout.setBackgroundColor(Color.parseColor("#97f7ad"));
                        break;
                    case PurpleUpsertBlueLeft:
                        layout.setBackgroundColor(Color.parseColor("#a797f7"));
                        break;
                    case RedUpsertBoth:
                        layout.setBackgroundColor(Color.parseColor("#f79797"));
                        break;
                    case OrangeUpsertYellowRight:
                        layout.setBackgroundColor(Color.parseColor("#f7ce97"));
                        break;
                }

                return view;
            }
        };

//        SimpleAdapter saItems =
//                new SimpleAdapter(
//                        this.getBaseContext(),
//                        lstItems,
//                        R.layout.relation_comparison,
//                        new String[] {"img",
//                                "comparisonResult",
//                                "multilineDetails"},
//                        new int[] {R.id.img,
//                                R.id.comparison_result,
//                                R.id.multiline_details});

        return saItems;
    }

    public NodeComparisonType getSelectedComparisonType() {
        Spinner spRelation = (Spinner)findViewById(R.id.spYellowTop);

        return NodeComparisonType.valueOf(spRelation.getSelectedItem().toString());
    }

    private class AsyncItemLoader extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {

            String result;

            try{

                //ListView lvItems = getListView();

                long start = System.nanoTime();

                publishProgress("loading...");
                mCurrentAdapter = loadItems();

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

            if(mCurrentAdapter != null){

                ListView lvItems = (ListView) findViewById(R.id.lvItems);
                lvItems.setAdapter(mCurrentAdapter);
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

            //Utils.toast(AudioListV2Activity.this, text[0]);
            if(text.length > 0)
            updateStatus(text[0]);
        }
    }

    private void updateStatus(String status){

        TextView tv = (TextView)findViewById(R.id.tvStatus);
        tv.setText(status);
    }

}
