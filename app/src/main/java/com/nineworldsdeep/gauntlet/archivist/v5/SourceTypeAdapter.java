package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;

import java.util.ArrayList;

/**
 * Created by brent on 1/13/18.
 */


public class SourceTypeAdapter extends RecyclerView.Adapter<SourceTypeAdapter.ViewHolder> {

    private ArrayList<ArchivistSourceType> mSourceTypes;
    private Context context;

    //private final Drawable mockPicDrawable;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView picture;
        public TextView name;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(
                    R.layout.fragment_archivist_source_types_content, parent, false));

            picture = (ImageView) itemView.findViewById(R.id.tile_picture);
            name = (TextView) itemView.findViewById(R.id.tile_title);
        }
    }

    public SourceTypeAdapter(Context context){

        mSourceTypes = ArchivistWorkspace.getSourceTypes();
        this.context = context;

        //temp code
        //Resources resources = context.getResources();
        //TypedArray a = resources.obtainTypedArray(R.array.places_picture);
        //mockPicDrawable = a.getDrawable(0);
        //a.recycle();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ArchivistSourceType srcType = mSourceTypes.get(position);

        //holder.picture.setImageDrawable(mockPicDrawable);
        //holder.name.setText(mSourceTypes.get(position).getSourceTypeName());

        holder.picture.setImageDrawable(context.getDrawable(srcType.getSourcePicDrawableResourceId()));
        holder.name.setText(srcType.getSourceTypeName());
    }

    @Override
    public int getItemCount() {
        return mSourceTypes.size();
    }
}
