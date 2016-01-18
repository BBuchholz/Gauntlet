package com.nineworldsdeep.gauntlet;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;

import com.nineworldsdeep.gauntlet.bookSegments.AliasListActivity;
import com.nineworldsdeep.gauntlet.bookSegments.BookSegmentsActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.AudioListActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.ImageListActivity;
import com.nineworldsdeep.gauntlet.muse.MuseMainActivity;
import com.nineworldsdeep.gauntlet.synergy.v1.SynergyMasterArchiveActivity;
import com.nineworldsdeep.gauntlet.synergy.v1.SynergyMasterListActivity;
import com.nineworldsdeep.gauntlet.synergy.v1.SynergyMasterTemplateActivity;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyMainActivity;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshLayout();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        refreshLayout();
    }

    private void refreshLayout(){
        setContentView(R.layout.activity_main);

        if(Configuration.isInTestMode()) {
            addTestFeatureButton("TEST");
        }
    }

    private void addTestFeatureButton(String btnText){

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_linear);
        Button btnTag = new Button(this);
        btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        btnTag.setText(btnText);
        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toast(getApplicationContext(), "test click");
            }
        });
        layout.addView(btnTag, 0);
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

    public void testClick(View view){

    }

    public void launchGrowthAreas(View view){

        startActivity(new Intent(this, GrowthAreasActivity.class));

    }

    public void launchMuse(View view){

        startActivity(new Intent(this, MuseMainActivity.class));
    }

    public void launchSynergyV2(View view) {

        startActivity(new Intent(this, SynergyMainActivity.class));
    }

    public void launchAliasList(View view) {

        startActivity(new Intent(this, AliasListActivity.class));
    }

    public void launchImageList(View view) {

        startActivity(new Intent(this, ImageListActivity.class));
    }

    public void launchAudioList(View view) {

        startActivity(new Intent(this, AudioListActivity.class));
    }

    public void launchTestModeActivity(View view) {

        startActivity(new Intent(this, TestModeActivity.class));
    }
}
