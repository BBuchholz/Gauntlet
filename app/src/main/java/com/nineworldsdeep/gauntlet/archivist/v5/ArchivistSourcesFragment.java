package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;

public class ArchivistSourcesFragment extends Fragment {

    public ArchivistSourcesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        //ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        SourceAdapter adapter = new SourceAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        public ImageView avator;
//        public TextView name;
//        public TextView description;
//
//        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
//            super(inflater.inflate(R.layout.fragment_archivist_sources_content, parent, false));
//            avator = (ImageView) itemView.findViewById(R.id.list_avatar);
//            name = (TextView) itemView.findViewById(R.id.list_title);
//            description = (TextView) itemView.findViewById(R.id.list_desc);
//        }
//    }
//
//    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
//
//        // Set numbers of List in RecyclerView.
//        private static final int LENGTH = 18;
//        private final String[] mPlaces;
//        private final String[] mPlaceDesc;
//        private final Drawable[] mPlaceAvators;
//
//        public ContentAdapter(Context context) {
//            Resources resources = context.getResources();
//            mPlaces = resources.getStringArray(R.array.places);
//            mPlaceDesc = resources.getStringArray(R.array.place_desc);
//            TypedArray a = resources.obtainTypedArray(R.array.place_avator);
//            mPlaceAvators = new Drawable[a.length()];
//            for (int i = 0; i < mPlaceAvators.length; i++) {
//                mPlaceAvators[i] = a.getDrawable(i);
//            }
//            a.recycle();
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.avator.setImageDrawable(mPlaceAvators[position % mPlaceAvators.length]);
//            holder.name.setText(mPlaces[position % mPlaces.length]);
//            holder.description.setText(mPlaceDesc[position % mPlaceDesc.length]);
//        }
//
//        @Override
//        public int getItemCount() {
//            return LENGTH;
//        }
//    }

}