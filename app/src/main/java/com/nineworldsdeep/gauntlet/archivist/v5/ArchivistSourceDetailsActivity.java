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

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ArchivistSourceDetailsActivity extends AppCompatActivity {


    public static final int REQUEST_RESULT_SOURCE_LOCATION_ENTRY = 1;

    private FloatingActionButton fabAddSourceLocationEntry;

    private ArchivistSourceDetailsFragment archivistSourceDetailsFragment;
    private ArchivistSourceLocationSubsetEntriesFragment archivistSourceLocationSubsetEntriesFragment;

    private int sourceDetailsTabIndex = -1;
    private int sourceLocationsTabIndex = -1;

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

        //store fab
        fabAddSourceLocationEntry =
                (FloatingActionButton) findViewById(R.id.fabAddSourceLocationEntry);

        fabAddSourceLocationEntry.hide(); //hidden by default because it should only show when the second tab is selected

        fabAddSourceLocationEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ArchivistWorkspace.getCurrentSource() != null) {

                    Intent intent =
                            new Intent(ArchivistSourceDetailsActivity.this,
                                    ArchivistAddSourceLocationEntryActivity.class);

                    startActivityForResult(intent, REQUEST_RESULT_SOURCE_LOCATION_ENTRY);

                }else{

                    Utils.toast(ArchivistSourceDetailsActivity.this,
                            "a specific source must be selected to add new sources location entry");
                }

                //Utils.toast(ArchivistSourceDetailsActivity.this, "add source location entry FAB clicked");
            }
        });

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

        if(requestCode == REQUEST_RESULT_SOURCE_LOCATION_ENTRY && data != null){

            int sourceId = data.getIntExtra(Extras.INT_ARCHIVIST_SOURCE_ID, -1);
            int sourceLocationSubsetId = data.getIntExtra(Extras.INT_ARCHIVIST_SOURCE_LOCATION_SUBSET_ID, -1);
            String sourceLocationSubsetEntryName = data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_LOCATION_SUBSET_ENTRY_NAME);

            if(sourceId > 0 && sourceLocationSubsetId > 0){

                if(Utils.stringIsNullOrWhitespace(sourceLocationSubsetEntryName)) {

                    Utils.toast(this, "source location subset entry name cannot be empty");

                }else {

                    NwdDb db = NwdDb.getInstance(this);

                    ArchivistSourceLocationEntry asle =
                            new ArchivistSourceLocationEntry(
                                    sourceId,
                                    sourceLocationSubsetId,
                                    sourceLocationSubsetEntryName
                            );

                    //db.insertOrIgnoreArchivistSourceExcerpt(ase);
                    //archivistSourceExcerptsFragment.refreshSourceExcerpts(this);

                    Utils.toast(this, "added location entry");
                }

            }else{

                Utils.toast(this, "invalid source id or source location subset id");
            }
        }


        if(requestCode == RESULT_CANCELED){

            //do nothing
            //it crashes on back button without this, see:
            //http://stackoverflow.com/questions/20782619/failure-delivering-result-resultinfo

        }
    }


    private void animateFab(int position){

        switch (position) {
            case 0:
                fabAddSourceLocationEntry.hide();
                break;

            case 1:
                fabAddSourceLocationEntry.show();
                break;

            default:
                fabAddSourceLocationEntry.hide();
                break;
        }
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        this.viewPager = viewPager;
        archivistFragmentStatePagerAdapter = new ArchivistFragmentStatePagerAdapter(getSupportFragmentManager());

        archivistSourceDetailsFragment = new ArchivistSourceDetailsFragment();

        archivistFragmentStatePagerAdapter.addFragment(archivistSourceDetailsFragment, "Source Details");
        sourceDetailsTabIndex = 0;

        archivistSourceLocationSubsetEntriesFragment = new ArchivistSourceLocationSubsetEntriesFragment();
        archivistSourceLocationSubsetEntriesFragment.setParentSourceDetailActivity(this);

        archivistFragmentStatePagerAdapter.addFragment(archivistSourceLocationSubsetEntriesFragment, "Location Entries");
        sourceLocationsTabIndex = 1;

        this.viewPager.setAdapter(archivistFragmentStatePagerAdapter);
    }

    public void refreshSourceLocationsSubsetEntries() {

        archivistSourceLocationSubsetEntriesFragment.refreshSourceLocationEntries(this);
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
            ArchivistWorkspace.setSourceDetailActivityFragmentTitle(fragmentKey, fragmentTitle);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Fragment fragment = mFragmentList.get(position);
            return ArchivistWorkspace.getSourceDetailActivityFragmentTitle(mFragmentsToKeys.get(fragment));
        }
    }
}
