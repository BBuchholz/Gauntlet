package com.nineworldsdeep.gauntlet.core;

import android.os.AsyncTask;
import android.os.Build;

/**
 * Created by brent on 10/5/16.
 */

public abstract class AsyncOperationCommand
        extends AsyncTask<Void, String, String> {

    protected IStatusEnabledActivity statusEnabledActivity;
    protected String operationVerb;
    protected String commandText;

    public AsyncOperationCommand(IStatusEnabledActivity statusActivity,
                                 String operationVerb,
                                 String commandText){

        this.statusEnabledActivity = statusActivity;
        this.operationVerb = operationVerb;
        this.commandText = commandText;
    }

    public void executeCommand(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            execute();
    }

    @Override
    protected String doInBackground(Void... params) {

        String result;

        try{

            //ListView lvItems = getListView();

            long start = System.nanoTime();

            publishProgress(operationVerb + "...");

            runOperation();

            long elapsedTime = System.nanoTime() - start;
            long milliseconds = elapsedTime / 1000000;

            String elapsedTimeStr = Long.toString(milliseconds);

            result = "finished " + operationVerb + ": " + elapsedTimeStr + "ms";

        }catch (Exception e){

            result = e.getMessage();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        statusEnabledActivity.updateStatus(result);

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(String... text) {

        //Utils.toast(AudioListV2Activity.this, text[0]);
        if(text.length > 0)
        statusEnabledActivity.updateStatus(text[0]);
    }

    @Override
    public String toString(){

        return this.commandText;
    }

    protected abstract void runOperation();

}
