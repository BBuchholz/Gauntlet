package com.nineworldsdeep.gauntlet.archivist.v5;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArchivistSourceTypesFragment extends Fragment {


    public ArchivistSourceTypesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        //ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        SourceTypeAdapter adapter = new SourceTypeAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return recyclerView;
    }

//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        public ImageView picture;
//        public TextView name;
//
//        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
//            super(inflater.inflate(
//                    R.layout.fragment_archivist_source_types_content, parent, false));
//
//            picture = (ImageView) itemView.findViewById(R.id.tile_picture);
//            name = (TextView) itemView.findViewById(R.id.tile_title);
//        }
//    }

//    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
//
//        // Set numbers of List in RecyclerView.
//        private static final int LENGTH = 18;
//        private final String[] mPlaces;
//        private final Drawable[] mPlacePictures;
//
//        public ContentAdapter(Context context) {
//            Resources resources = context.getResources();
//            mPlaces = resources.getStringArray(R.array.places);
//            TypedArray a = resources.obtainTypedArray(R.array.places_picture);
//            mPlacePictures = new Drawable[a.length()];
//            for (int i = 0; i < mPlacePictures.length; i++) {
//                mPlacePictures[i] = a.getDrawable(i);
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
//            holder.picture.setImageDrawable(mPlacePictures[position % mPlacePictures.length]);
//            holder.name.setText(mPlaces[position % mPlaces.length]);
//        }
//
//        @Override
//        public int getItemCount() {
//            return LENGTH;
//        }
//    }

}
