package com.nineworldsdeep.gauntlet.core;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by brent on 10/5/16.
 */

public abstract class AsyncListBasedActivity extends ListBaseActivity {

    protected ListAdapter mCurrentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void refreshLayout(){

//        ListView lvItems = getListView();
//
//        // http://stackoverflow.com/a/8276140/670768
//        //save position info
//        int index = lvItems.getFirstVisiblePosition();
//        View v = lvItems.getChildAt(0);
//        int top = (v == null) ? 0 : v.getTop();
//
//        //perform adapter operations
//        readItems(getListView());
//        setupListViewListener(getListView());
//        registerForContextMenu(getListView());
//
//        //restore listview postion
//        lvItems.setSelectionFromTop(index, top);

        AsyncItemLoader ail = new AsyncItemLoader();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            ail.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            ail.execute();
    }

    protected class AsyncItemLoader extends AsyncTask<Void, String, String> {

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

                ListView lvItems = getListView();
                lvItems.setAdapter(mCurrentAdapter);
                setupListViewListener(lvItems);
                registerForContextMenu(lvItems);

                restoreInstanceState();
            }
        }

        @Override
        protected void onPreExecute() {


            storeInstanceState();

        }

        @Override
        protected void onProgressUpdate(String... text) {

            //Utils.toast(AudioListV2Activity.this, text[0]);
            if(text.length > 0)
            updateStatus(text[0]);
        }
    }

    public void updateStatus(String status){

        TextView tv = getStatusView();
        tv.setText(status);
    }

    @Override
    protected final void readItems(ListView lv){

        //Async List Activities use load() with return type ListAdapter
        throw new UnsupportedOperationException("use load() with AsyncList");
    }

    protected abstract ListAdapter loadItems();
    protected abstract TextView getStatusView();
}
