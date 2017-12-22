package com.nineworldsdeep.gauntlet.archivist.v5;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;
import java.util.List;


public class ArchivistActivity extends AppCompatActivity {

    //FLOATING ACTION BUTTON FOR EACH TAB, THIS LOOKS LIKE WHAT WE NEED
    //https://stackoverflow.com/questions/31415742/how-to-change-floatingactionbutton-between-tabs/34128992

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
                Utils.toast(ArchivistActivity.this, "add source goes here");
            }
        });

        fabAddSourceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toast(ArchivistActivity.this, "add source type goes here");
            }
        });

        fabAddSourceExcerpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toast(ArchivistActivity.this, "add source excerpt goes here");
            }
        });

        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                animateFab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
        adapter.addFragment(new ArchivistSourceTypesFragment(), "Source Types");
        adapter.addFragment(new ArchivistSourcesFragment(), "Sources");
        adapter.addFragment(new ArchivistSourceExcerptsFragment(), "Excerpts");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
