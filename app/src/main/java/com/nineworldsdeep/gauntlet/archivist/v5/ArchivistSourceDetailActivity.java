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
import android.view.View;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ArchivistSourceDetailActivity extends AppCompatActivity {


//    public static final int REQUEST_RESULT_SOURCE_TYPE_NAME = 1;
//    public static final int REQUEST_RESULT_SOURCE = 2;
//    public static final int REQUEST_RESULT_SOURCE_EXCERPT = 3;

    private FloatingActionButton fabAddSourceLocationEntry;
//    private FloatingActionButton fabAddSourceType;
//    private FloatingActionButton fabAddSourceExcerpt;

    private ArchivistSourceDetailsFragment archivistSourceDetailsFragment;
    private ArchivistSourceLocationSubsetEntriesFragment archivistSourceLocationSubsetEntriesFragment;
    //private ArchivistSourceExcerptsFragment archivistSourceExcerptsFragment;

    private int sourceDetailsTabIndex = -1;
    private int sourceLocationsTabIndex = -1;
    //private int sourceExcerptsTabIndex = -1;

    private ArchivistFragmentStatePagerAdapter archivistFragmentStatePagerAdapter;
    private ViewPager viewPager;

    public ArchivistFragmentStatePagerAdapter getFragmentStatePagerAdapter(){
        return archivistFragmentStatePagerAdapter;
    }

    public void selectSourceDetailsTab(){
        if(sourceDetailsTabIndex > -1) {
            viewPager.setCurrentItem(sourceDetailsTabIndex);
        }
    }

    public void selectSourceLocationsTab(){
        if(sourceLocationsTabIndex > -1){
            viewPager.setCurrentItem(sourceLocationsTabIndex);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivist_source_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //store fabs
        fabAddSourceLocationEntry =
                (FloatingActionButton) findViewById(R.id.fabAddSourceLocationEntry);
//        fabAddSourceType =
//                (FloatingActionButton) findViewById(R.id.fabAddSourceType);
//        fabAddSourceExcerpt =
//                (FloatingActionButton) findViewById(R.id.fabAddSourceExcerpt);

        fabAddSourceLocationEntry.hide(); //hidden by default because it should only show when the second tab is selected

        fabAddSourceLocationEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Utils.toast(ArchivistActivity.this, "add source goes here");

//                if(!ArchivistWorkspace.currentSourceTypeIsAllTypes()) {
//
//                    Intent intent =
//                            new Intent(ArchivistSourceDetailActivity.this,
//                                    ArchivistAddSourceActivity.class);
//
//                    startActivityForResult(intent, REQUEST_RESULT_SOURCE);
//
//                }else{
//
//                    Utils.toast(ArchivistSourceDetailActivity.this,
//                            "Select a specific source type to add new sources");
//                }

                Utils.toast(ArchivistSourceDetailActivity.this, "add source location entry FAB clicked");
            }
        });

//        fabAddSourceType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Utils.toast(ArchivistActivity.this, "add source type goes here");
//
//                Intent intent =
//                        new Intent(ArchivistSourceDetailActivity.this,
//                                ArchivistAddSourceTypeActivity.class);
//
//                startActivityForResult(intent, REQUEST_RESULT_SOURCE_TYPE_NAME);
//
//            }
//        });
//
//        fabAddSourceExcerpt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Utils.toast(ArchivistActivity.this, "add source excerpt goes here");
//
//                Intent intent =
//                        new Intent(ArchivistSourceDetailActivity.this,
//                                ArchivistAddSourceExcerptActivity.class);
//
//                startActivityForResult(intent, REQUEST_RESULT_SOURCE_EXCERPT);
//
//            }
//        });

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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode== REQUEST_RESULT_SOURCE_TYPE_NAME && data != null)
//        {
//            String sourceTypeName =
//                    data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_TYPE_NAME);
//
//            if(!Utils.stringIsNullOrWhitespace(sourceTypeName)){
//
//                NwdDb db = NwdDb.getInstance(this);
//
//                db.ensureArchivistSourceTypeName(sourceTypeName);
//                archivistSourceTypesFragment.refreshSourceTypes(this);
//
//                Utils.toast(this, "added source type name: " + sourceTypeName);
//
//            }else{
//
//                Utils.toast(this, "empty name");
//
//            }
//        }
//
//        if(requestCode== REQUEST_RESULT_SOURCE && data != null){
//
//            int sourceTypeId = data.getIntExtra(Extras.INT_ARCHIVIST_SOURCE_TYPE_ID, -1);
//            String sourceTypeName = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_TYPE_NAME);
//            String sourceTitle = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_TITLE);
//            String sourceAuthor = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_AUTHOR);
//            String sourceDirector = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_DIRECTOR);
//            String sourceYear = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_YEAR);
//            String sourceUrl = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_URL);
//            String sourceRetrievalDate = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_RETRIEVAL_DATE);
//
//            if(sourceTypeId > 0){
//
//                NwdDb db = NwdDb.getInstance(this);
//
//                ArchivistSource archivistSource =
//                        new ArchivistSource(
//                            -1,
//                            sourceTypeId,
//                            sourceTitle,
//                            sourceAuthor,
//                            sourceDirector,
//                            sourceYear,
//                            sourceUrl,
//                            sourceRetrievalDate);
//
//                db.insertOrIgnoreArchivistSource(archivistSource);
//                archivistSourceLocationSubsetEntriesFragment.refreshSources(this);
//
//                Utils.toast(this, "Added new " + sourceTypeName);
//
//            }else{
//
//                Utils.toast(this, "invalid source type");
//            }
//        }
//
//        if(requestCode== REQUEST_RESULT_SOURCE_EXCERPT && data != null){
//
//            int sourceId = data.getIntExtra(Extras.INT_ARCHIVIST_SOURCE_ID, -1);
//            String sourceExcerptPages = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_PAGES);
//            String sourceExcerptBeginTime = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_BEGIN_TIME);
//            String sourceExcerptEndTime = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_END_TIME);
//            String sourceExcerptValue = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_VALUE);
//
//            if(sourceId > 0){
//
//                if(Utils.stringIsNullOrWhitespace(sourceExcerptValue)) {
//
//                    Utils.toast(this, "Source Excerpt Value cannot be empty");
//
//                }else {
//
//                    NwdDb db = NwdDb.getInstance(this);
//
//                    ArchivistSourceExcerpt ase =
//                            new ArchivistSourceExcerpt(
//                                    -1,
//                                    sourceId,
//                                    sourceExcerptValue,
//                                    sourceExcerptPages,
//                                    sourceExcerptBeginTime,
//                                    sourceExcerptEndTime
//                            );
//
//                    db.insertOrIgnoreArchivistSourceExcerpt(ase);
//                    archivistSourceExcerptsFragment.refreshSourceExcerpts(this);
//
//                    Utils.toast(this, "Added excerpt");
//                }
//
//            }else{
//
//                Utils.toast(this, "invalid source");
//            }
//        }

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
                //fabAddSourceType.show();
                fabAddSourceLocationEntry.hide();
                //fabAddSourceExcerpt.hide();
                break;

            case 1:
                //fabAddSourceType.hide();
                fabAddSourceLocationEntry.show();
                //fabAddSourceExcerpt.hide();
                break;

            default:
                //fabAddSourceType.hide();
                fabAddSourceLocationEntry.hide();
                //fabAddSourceExcerpt.hide();
                break;
        }
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        this.viewPager = viewPager;
        archivistFragmentStatePagerAdapter = new ArchivistFragmentStatePagerAdapter(getSupportFragmentManager());

//        archivistSourceTypesFragment = new ArchivistSourceTypesFragment();
//        archivistSourceTypesFragment.setParentArchivistActivity(this);
//
//        archivistFragmentStatePagerAdapter.addFragment(archivistSourceTypesFragment, "Source Types");
//        sourceTypesTabIndex = 0;

        archivistSourceDetailsFragment = new ArchivistSourceDetailsFragment();
        //archivistSourceDetailsFragment.setParentArchivistActivity(this);

        archivistFragmentStatePagerAdapter.addFragment(archivistSourceDetailsFragment, "Source Details");
        sourceDetailsTabIndex = 0;

        archivistSourceLocationSubsetEntriesFragment = new ArchivistSourceLocationSubsetEntriesFragment();
        archivistSourceLocationSubsetEntriesFragment.setParentSourceDetailActivity(this);

        archivistFragmentStatePagerAdapter.addFragment(archivistSourceLocationSubsetEntriesFragment, "Location Entries");
        sourceLocationsTabIndex = 1;

//        archivistSourceExcerptsFragment = new ArchivistSourceExcerptsFragment();
//        archivistSourceExcerptsFragment.setParentArchivistActivity(this);
//        archivistFragmentStatePagerAdapter.addFragment(archivistSourceExcerptsFragment, "Excerpts");
//        sourceExcerptsTabIndex = 2;

        this.viewPager.setAdapter(archivistFragmentStatePagerAdapter);
    }

    public void refreshSourceLocationsSubsetEntries() {

        archivistSourceLocationSubsetEntriesFragment.refreshSourceLocationEntries(this);
    }

//    public void refreshSourceExcerpts(){
//
//        archivistSourceExcerptsFragment.refreshSourceExcerpts(this);
//    }


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
            ArchivistWorkspace.setSourceDetailActivityFragmentTitle(fragmentKey, fragmentTitle);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Fragment fragment = mFragmentList.get(position);
            return ArchivistWorkspace.getSourceDetailActivityFragmentTitle(mFragmentsToKeys.get(fragment));
        }
    }
}
