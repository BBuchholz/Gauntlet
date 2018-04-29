package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;


public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder> {

    private ArrayList<ArchivistSource> mSources;

    private ArchivistActivity parentArchivistActivity;

    public void refreshSources(Context context) {

        mSources = ArchivistWorkspace.getSources(context);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView sourceTypeImage;
        public TextView name;
        public TextView description;

//        public ViewHolder(LayoutInflater inflater,
//                          final ViewGroup parent) {
//
//            super(inflater.inflate(R.layout.fragment_archivist_sources_content, parent, false));
//
//            sourceTypeImage = (ImageView) itemView.findViewById(R.id.list_avatar);
//            name = (TextView) itemView.findViewById(R.id.list_title);
//            description = (TextView) itemView.findViewById(R.id.list_desc);
//
//            sourceTypeImage.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//
////                    ArchivistWorkspace.setCurrentSource(archivistSource);
////
////                    parentArchivistActivity.getFragmentStatePagerAdapter().notifyDataSetChanged();
////                    parentArchivistActivity.selectSourceExcerptsTab();
////                    parentArchivistActivity.refreshSourceExcerpts();
//
//                }
//            });
//        }

        public ViewHolder(final View itemView) {

            super(itemView);

            sourceTypeImage = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_desc);
        }
    }

    SourceAdapter(ArchivistActivity parentArchivistActivity){

        mSources = ArchivistWorkspace.getSources(parentArchivistActivity);
        this.parentArchivistActivity = parentArchivistActivity;

//        //temp code
//        Resources resources = context.getResources();
//        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
//        mockPicDrawable = a.getDrawable(0);
//        a.recycle();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


//        final ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
//
//
//
//        return holder;

        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_archivist_sources_content, parent, false);

        final ViewHolder holder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){

                    ArchivistSource archivistSource = mSources.get(position);
                    ArchivistWorkspace.setCurrentSource(archivistSource);

                    parentArchivistActivity.getFragmentStatePagerAdapter().notifyDataSetChanged();
                    parentArchivistActivity.selectSourceExcerptsTab();
                    parentArchivistActivity.refreshSourceExcerpts();

                    Utils.toast(parentArchivistActivity, archivistSource.getShortDescription() + " clicked in SourceAdapter.");
                }
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ArchivistSource src = mSources.get(position);

        holder.sourceTypeImage.setImageDrawable(
                parentArchivistActivity.getDrawable(
                        src.getSourceType().getSourcePicDrawableResourceId()
                )
        );

        holder.name.setText(src.getSourceTitle());
        holder.description.setText(src.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mSources.size();
}
}
