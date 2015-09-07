package com.nineworldsdeep.gauntlet;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


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

    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void launchPdf(View view){

        startActivity(new Intent(this, PdfActivity.class));
    }

    public void launchEPub(View view){

        startActivity(new Intent(this, EPubActivity.class));

    }
    public void launchMusic(View view){

        startActivity(new Intent(this, MusicActivity.class));

    }
    public void launchVids(View view){

        startActivity(new Intent(this, VidsActivity.class));

    }
    public void launchSimpleToDo(View view){

        startActivity(new Intent(this, SimpleToDoActivity.class));

    }
    public void launchSynergy(View view){

        startActivity(new Intent(this, SynergyActivity.class));

    }
    public void launchGrowthAreas(View view){

        startActivity(new Intent(this, GrowthAreasActivity.class));

    }
}
