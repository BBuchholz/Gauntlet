package com.nineworldsdeep.gauntlet.archivist.v5;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;

/**
 * Created by brent on 1/13/18.
 */

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder> {

   public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView avator;
        public TextView name;
        public TextView description;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_archivist_sources_content, parent, false));
            avator = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_desc);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.avator.setImageDrawable(mPlaceAvators[position % mPlaceAvators.length]);
//        holder.name.setText(mPlaces[position % mPlaces.length]);
//        holder.description.setText(mPlaceDesc[position % mPlaceDesc.length]);
    }

    @Override
    public int getItemCount() {
        //return LENGTH;
        return 0;
    }
}
