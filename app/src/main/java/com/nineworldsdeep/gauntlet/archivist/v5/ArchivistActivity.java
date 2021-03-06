package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.HomeListActivity;
import com.nineworldsdeep.gauntlet.core.NavigateActivityCommand;
import com.nineworldsdeep.gauntlet.mnemosyne.TransferActivity;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ArchivistActivity extends AppCompatActivity {

    //FLOATING ACTION BUTTON FOR EACH TAB, THIS LOOKS LIKE WHAT WE NEED
    //https://stackoverflow.com/questions/31415742/how-to-change-floatingactionbutton-between-tabs/34128992

    public static final int REQUEST_RESULT_SOURCE_TYPE_NAME = 1;
    public static final int REQUEST_RESULT_SOURCE = 2;
    public static final int REQUEST_RESULT_SOURCE_EXCERPT = 3;

    private FloatingActionButton fabAddSource;
    private FloatingActionButton fabAddSourceType;
    private FloatingActionButton fabAddSourceExcerpt;

    private ArchivistSourceTypesFragment archivistSourceTypesFragment;
    private ArchivistSourcesFragment archivistSourcesFragment;
    private ArchivistSourceExcerptsFragment archivistSourceExcerptsFragment;

    private int sourceTabIndex = -1;
    private int sourceTypesTabIndex = -1;
    private int sourceExcerptsTabIndex = -1;

    private ArchivistFragmentStatePagerAdapter archivistFragmentStatePagerAdapter;
    private ViewPager viewPager;

    public ArchivistFragmentStatePagerAdapter getFragmentStatePagerAdapter(){
        return archivistFragmentStatePagerAdapter;
    }

    public void selectSourcesTab(){
        if(sourceTabIndex > -1) {
            viewPager.setCurrentItem(sourceTabIndex);
        }
    }

    public void selectSourceExcerptsTab(){
        if(sourceTabIndex > -1){
            viewPager.setCurrentItem(sourceExcerptsTabIndex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_archivist_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_xml_ops){

            //just send to transfer activity where other xml export operations live
            NavigateActivityCommand.navigateTo(
                    TransferActivity.class, this
            );

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivist);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //store fabs
        fabAddSource =
                (FloatingActionButton) findViewById(R.id.fabAddSource);
        fabAddSourceType =
                (FloatingActionButton) findViewById(R.id.fabAddSourceType);
        fabAddSourceExcerpt =
                (FloatingActionButton) findViewById(R.id.fabAddSourceExcerpt);

        fabAddSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Utils.toast(ArchivistActivity.this, "add source goes here");

                if(!ArchivistWorkspace.currentSourceTypeIsAllTypes()) {

                    Intent intent =
                            new Intent(ArchivistActivity.this,
                                    ArchivistAddSourceActivity.class);

                    startActivityForResult(intent, REQUEST_RESULT_SOURCE);

                }else{

                    Utils.toast(ArchivistActivity.this,
                            "Select a specific source type to add new sources");
                }
            }
        });

        fabAddSourceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Utils.toast(ArchivistActivity.this, "add source type goes here");

                Intent intent =
                        new Intent(ArchivistActivity.this,
                                ArchivistAddSourceTypeActivity.class);

                startActivityForResult(intent, REQUEST_RESULT_SOURCE_TYPE_NAME);

            }
        });

        fabAddSourceExcerpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Utils.toast(ArchivistActivity.this, "add source excerpt goes here");

                Intent intent =
                        new Intent(ArchivistActivity.this,
                                ArchivistAddSourceExcerptActivity.class);

                startActivityForResult(intent, REQUEST_RESULT_SOURCE_EXCERPT);

            }
        });

        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);

        if (tabs != null) {
            tabs.setupWithViewPager(viewPager);
        }

        if (viewPager != null) {

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    animateFab(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        ArchivistWorkspace.setArchivistMainActivity(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQUEST_RESULT_SOURCE_TYPE_NAME && data != null)
        {
            String sourceTypeName =
                    data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_TYPE_NAME);

            if(!Utils.stringIsNullOrWhitespace(sourceTypeName)){

                NwdDb db = NwdDb.getInstance(this);

                db.ensureArchivistSourceTypeName(sourceTypeName);
                archivistSourceTypesFragment.refreshSourceTypes(this);

                Utils.toast(this, "added source type name: " + sourceTypeName);

            }else{

                Utils.toast(this, "empty name");

            }
        }

        if(requestCode== REQUEST_RESULT_SOURCE && data != null){

            int sourceTypeId = data.getIntExtra(Extras.INT_ARCHIVIST_SOURCE_TYPE_ID, -1);
            String sourceTypeName = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_TYPE_NAME);
            String sourceTitle = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_TITLE);
            String sourceAuthor = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_AUTHOR);
            String sourceDirector = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_DIRECTOR);
            String sourceYear = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_YEAR);
            String sourceUrl = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_URL);
            String sourceRetrievalDate = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_RETRIEVAL_DATE);

            if(sourceTypeId > 0){

                if(!Utils.stringIsNullOrWhitespace(sourceTitle)) {
                    NwdDb db = NwdDb.getInstance(this);

                    ArchivistSource archivistSource =
                            new ArchivistSource(
                                    -1,
                                    sourceTypeId,
                                    sourceTitle,
                                    sourceAuthor,
                                    sourceDirector,
                                    sourceYear,
                                    sourceUrl,
                                    sourceRetrievalDate);

                    db.insertOrIgnoreArchivistSource(archivistSource);
                    archivistSourcesFragment.refreshSources(this);

                    Utils.toast(this, "Added new " + sourceTypeName);

                }else{

                    Utils.toast(this, "cannot add empty title");
                }
            }else{

                Utils.toast(this, "invalid source type");
            }
        }

        if(requestCode== REQUEST_RESULT_SOURCE_EXCERPT && data != null){

            int sourceId = data.getIntExtra(Extras.INT_ARCHIVIST_SOURCE_ID, -1);
            String sourceExcerptPages = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_PAGES);
            String sourceExcerptBeginTime = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_BEGIN_TIME);
            String sourceExcerptEndTime = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_END_TIME);
            String sourceExcerptValue = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_VALUE);

            if(sourceId > 0){

                if(Utils.stringIsNullOrWhitespace(sourceExcerptValue)) {

                    Utils.toast(this, "Source Excerpt Value cannot be empty");

                }else {

                    NwdDb db = NwdDb.getInstance(this);

                    ArchivistSourceExcerpt ase =
                            new ArchivistSourceExcerpt(
                                    -1,
                                    sourceId,
                                    sourceExcerptValue,
                                    sourceExcerptPages,
                                    sourceExcerptBeginTime,
                                    sourceExcerptEndTime
                            );

                    db.insertOrIgnoreArchivistSourceExcerpt(ase);
                    archivistSourceExcerptsFragment.refreshSourceExcerpts(this);

                    Utils.toast(this, "Added excerpt");
                }

            }else{

                Utils.toast(this, "invalid source");
            }
        }

        if(requestCode== RESULT_CANCELED){

            //do nothing
            //it crashes on back button without this, see:
            //http://stackoverflow.com/questions/20782619/failure-delivering-result-resultinfo

            Utils.toast(this, "cancelled");
        }
    }


    private void animateFab(int position){

        switch (position) {
            case 0:
                fabAddSourceType.show();
                fabAddSource.hide();
                fabAddSourceExcerpt.hide();
                break;

            case 1:
                fabAddSourceType.hide();
                fabAddSource.show();
                fabAddSourceExcerpt.hide();
                break;

            case 2:
                fabAddSourceType.hide();
                fabAddSource.hide();
                fabAddSourceExcerpt.show();
                break;

            default:
                fabAddSourceType.hide();
                fabAddSource.hide();
                fabAddSourceExcerpt.hide();
                break;
        }
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        this.viewPager = viewPager;
        archivistFragmentStatePagerAdapter = new ArchivistFragmentStatePagerAdapter(getSupportFragmentManager());

        archivistSourceTypesFragment = new ArchivistSourceTypesFragment();
        archivistSourceTypesFragment.setParentArchivistActivity(this);

        archivistFragmentStatePagerAdapter.addFragment(archivistSourceTypesFragment, "Source Types");
        sourceTypesTabIndex = 0;

        archivistSourcesFragment = new ArchivistSourcesFragment();
        archivistSourcesFragment.setParentArchivistActivity(this);

        archivistFragmentStatePagerAdapter.addFragment(archivistSourcesFragment, "Sources");
        sourceTabIndex = 1;

        archivistSourceExcerptsFragment = new ArchivistSourceExcerptsFragment();
        archivistSourceExcerptsFragment.setParentArchivistActivity(this);
        archivistFragmentStatePagerAdapter.addFragment(archivistSourceExcerptsFragment, "Excerpts");
        sourceExcerptsTabIndex = 2;

        this.viewPager.setAdapter(archivistFragmentStatePagerAdapter);
    }

    public void refreshSources() {

        archivistSourcesFragment.refreshSources(this);
    }

    public void refreshSourceExcerpts(){

        archivistSourceExcerptsFragment.refreshSourceExcerpts(this);
    }


    // using FragmentStatePagerAdapter instead of FragmentPagerAdapter per:
    // https://stackoverflow.com/questions/30080045/fragmentpageradapter-notifydatasetchanged-not-working
    static class ArchivistFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final HashMap<Fragment, String> mFragmentsToKeys = new HashMap<>();

        ArchivistFragmentStatePagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String fragmentKey) {

            addFragment(fragment, fragmentKey, fragmentKey);
        }

        void addFragment(Fragment fragment, String fragmentKey, String fragmentTitle) {

            mFragmentList.add(fragment);
            mFragmentsToKeys.put(fragment, fragmentKey);
            ArchivistWorkspace.setMainActivityFragmentTitle(fragmentKey, fragmentTitle);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Fragment fragment = mFragmentList.get(position);
            return ArchivistWorkspace.getMainActivityFragmentTitle(mFragmentsToKeys.get(fragment));
        }
    }
}
