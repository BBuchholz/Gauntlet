package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nineworldsdeep.gauntlet.Extras;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

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

                Intent intent =
                        new Intent(ArchivistActivity.this,
                                ArchivistAddSourceActivity.class);

                startActivityForResult(intent, REQUEST_RESULT_SOURCE);
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQUEST_RESULT_SOURCE_TYPE_NAME && data != null)
        {
            String sourceTypeName =
                    data.getStringExtra(Extras.STRING_ARCHIVIST_SOURCE_TYPE_NAME);

            if(!Utils.stringIsNullOrWhitespace(sourceTypeName)){

                Utils.toast(this, "source type name: " + sourceTypeName);

            }else{

                Utils.toast(this, "empty name");

            }
        }

        if(requestCode== REQUEST_RESULT_SOURCE && data != null){

            Utils.toast(this,
                    "add source activity result awaiting implementation");
        }

        if(requestCode== REQUEST_RESULT_SOURCE_EXCERPT && data != null){

            Utils.toast(this,
                    "add source excerpt activity result awaiting implementation");
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

        Adapter adapter = new Adapter(getSupportFragmentManager());

        ArchivistSourceTypesFragment archivistSourceTypesFragment = new ArchivistSourceTypesFragment();
        archivistSourceTypesFragment.setFragmentStatePagerAdapter(adapter);

        adapter.addFragment(archivistSourceTypesFragment, "Source Types");
        adapter.addFragment(new ArchivistSourcesFragment(), "Sources");
        adapter.addFragment(new ArchivistSourceExcerptsFragment(), "Excerpts");

        viewPager.setAdapter(adapter);
    }

    // using FragmentStatePagerAdapter instead of FragmentPagerAdapter per:
    // https://stackoverflow.com/questions/30080045/fragmentpageradapter-notifydatasetchanged-not-working
    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final HashMap<Fragment, String> mFragmentsToKeys = new HashMap<>();

        public Adapter(FragmentManager manager) {
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
            ArchivistWorkspace.setFragmentTitle(fragmentKey, fragmentTitle);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Fragment fragment = mFragmentList.get(position);
            return ArchivistWorkspace.getFragmentTitle(mFragmentsToKeys.get(fragment));
        }
    }
}
