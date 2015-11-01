package com.nineworldsdeep.gauntlet;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nineworldsdeep.gauntlet.muse.MuseMainActivity;
import com.nineworldsdeep.gauntlet.synergy.SynergyMasterArchiveActivity;
import com.nineworldsdeep.gauntlet.synergy.SynergyMasterListActivity;
import com.nineworldsdeep.gauntlet.synergy.SynergyMasterTemplateActivity;


public class MainActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.nineworldsdeep.gauntlet.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public void sendMessage(View view) {
//        // Do something in response to button
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//    }
//
//    public void launchSimpleToDo(View view){
//
//        startActivity(new Intent(this, SimpleToDoActivity.class));
//
//    }
//    public void launchSynergy(View view){
//
//        startActivity(new Intent(this, SynergyActivity.class));
//
//    }
    public void launchSynergyMasterList(View view){

        startActivity(new Intent(this, SynergyMasterListActivity.class));

    }
    public void launchGrowthAreas(View view){

        startActivity(new Intent(this, GrowthAreasActivity.class));

    }
    public void launchSynergyMasterArchive(View view){

        startActivity(new Intent(this, SynergyMasterArchiveActivity.class));

    }
    public void launchSynergyMasterTemplate(View view){

        startActivity(new Intent(this, SynergyMasterTemplateActivity.class));

    }
    public void launchMuse(View view){

        startActivity(new Intent(this, MuseMainActivity.class));
    }
}
