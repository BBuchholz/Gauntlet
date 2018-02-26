package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;

import java.util.ArrayList;


public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder> {

    private ArrayList<ArchivistSource> mSources;
    private final Drawable mockPicDrawable;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView sourceTypeImage;
        public TextView name;
        public TextView description;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_archivist_sources_content, parent, false));
            sourceTypeImage = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_desc);
        }
    }

    public SourceAdapter(Context context){

        mSources = ArchivistWorkspace.getOpenSources();

        //temp code
        Resources resources = context.getResources();
        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
        mockPicDrawable = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ArchivistSource src = mSources.get(position);

        holder.sourceTypeImage.setImageDrawable(mockPicDrawable);
        holder.name.setText(src.getSourceTitle());
        holder.description.setText(src.getSourceDescription());
    }

    @Override
    public int getItemCount() {
        return mSources.size();
}
}
