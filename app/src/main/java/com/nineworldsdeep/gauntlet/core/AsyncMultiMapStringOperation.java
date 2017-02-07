package com.nineworldsdeep.gauntlet.core;

import android.os.AsyncTask;
import android.os.Build;

import com.nineworldsdeep.gauntlet.MultiMapString;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by brent on 10/5/16.
 */

public abstract class AsyncMultiMapStringOperation
        extends AsyncTask<Void, String, MultiMapString> {

    protected IStatusActivity statusEnabledActivity;
    protected String operationVerb;
//    protected String commandText;

    public AsyncMultiMapStringOperation(IStatusActivity statusActivity,
                                        String operationVerb){

        this.statusEnabledActivity = statusActivity;
        this.operationVerb = operationVerb;
//        this.commandText = commandText;
    }

    public AsyncTask<Void, String, MultiMapString> executeAsync()
            throws ExecutionException, InterruptedException {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            return execute();
    }

    @Override
    protected MultiMapString doInBackground(Void... params) {

        MultiMapString result;

        try{

            //ListView lvItems = getListView();

            long start = System.nanoTime();

            publishProgress(operationVerb + "...");

            result = runOperation();

            long elapsedTime = System.nanoTime() - start;
            long milliseconds = elapsedTime / 1000000;

            String elapsedTimeStr = Long.toString(milliseconds);

            publishProgress("finished " + operationVerb + ": " + elapsedTimeStr + "ms");

        }catch (Exception e){

            //do nothing, return empty list
            result = new MultiMapString();
        }

        return result;
    }

//    @Override
//    protected void onPostExecute(String result) {
//
////        statusEnabledActivity.updateStatus(result);
//
//    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(String... text) {

        //Utils.toast(AudioListV2Activity.this, text[0]);
        if(text.length > 0)
        statusEnabledActivity.updateStatus(text[0]);
    }

    protected abstract MultiMapString runOperation();

}
