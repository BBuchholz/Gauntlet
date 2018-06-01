package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import org.w3c.dom.Text;

public class ArchivistSourceExcerptDetailActivity extends Activity {


    public static final String EXTRA_SOURCE_EXCERPT_VALUE =
            "com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceExcerptDetailActivity.SOURCE_EXCERPT_VALUE";

    public static final String EXTRA_SOURCE_EXCERPT_TAG_STRING =
            "com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceExcerptDetailActivity.SOURCE_EXCERPT_TAG_STRING";

    public static final String EXTRA_SOURCE_EXCERPT_ID =
            "com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceExcerptDetailActivity.SOURCE_EXCERPT_ID";

    public static final String EXTRA_SOURCE_ID =
            "com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceExcerptDetailActivity.SOURCE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivist_source_excerpt_detail);

        Intent intent = getIntent();

        int sourceId = intent.getIntExtra(EXTRA_SOURCE_ID, -1);

        int sourceExcerptId = intent.getIntExtra(EXTRA_SOURCE_EXCERPT_ID, -1);

        String excerptValue =
                intent.getStringExtra(EXTRA_SOURCE_EXCERPT_VALUE);

        String tagString =
                intent.getStringExtra(EXTRA_SOURCE_EXCERPT_TAG_STRING);

        ArchivistSource currentSource = ArchivistWorkspace.getCurrentSource();

        CollapsingToolbarLayout appBarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {

            //set imageview source for collapsing toolbar to source type drawable
            ImageView sourceTypeImage = (ImageView) findViewById(R.id.imgSourceTypeImage);

            ArchivistSourceType srcType =
                    ArchivistWorkspace.getSourceTypeById(currentSource.getSourceTypeId());

            sourceTypeImage.setImageDrawable(getDrawable(srcType.getSourcePicDrawableResourceId()));

            //set toolbar title to source title
            String sourceTitle = currentSource.getShortDescription();
            appBarLayout.setTitle(sourceTitle);
        }

        TextView tvTagString = (TextView)findViewById(R.id.tvTagString);
        TextView tvExcerptValue = (TextView)findViewById(R.id.tvExcerptValue);

        tvTagString.setText(tagString);
        tvExcerptValue.setText(excerptValue);
    }
}
