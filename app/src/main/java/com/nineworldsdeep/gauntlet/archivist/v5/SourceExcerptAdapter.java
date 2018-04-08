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

/**
 * Created by brent on 1/13/18.
 */

public class SourceExcerptAdapter extends RecyclerView.Adapter<SourceExcerptAdapter.ViewHolder>{

    private ArrayList<ArchivistSourceExcerpt> mSourceExcerpts;
    //private final Drawable mockPicDrawable;

    static class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView picture;
//        public TextView name;
//        public TextView description;

        TextView excerptLocation;
        TextView excerptValue;
        TextView excerptTagString;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
//            super(inflater.inflate(R.layout.fragment_card_content, parent, false));
//            picture = (ImageView) itemView.findViewById(R.id.card_image);
//            name = (TextView) itemView.findViewById(R.id.card_title);
//            description = (TextView) itemView.findViewById(R.id.card_text);

            super(inflater.inflate(R.layout.fragment_archivist_source_excerpts_content, parent, false));
            excerptLocation = (TextView) itemView.findViewById(R.id.tvExcerptLocation);
            excerptValue = (TextView) itemView.findViewById(R.id.tvExcerptValue);
            excerptTagString = (TextView) itemView.findViewById(R.id.tvTagString);
        }
    }

    SourceExcerptAdapter(Context context){

        mSourceExcerpts = ArchivistWorkspace.getOpenSourceExcerpts();

        //temp code
//        Resources resources = context.getResources();
//        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
//        mockPicDrawable = a.getDrawable(0);
//        a.recycle();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ArchivistSourceExcerpt ase = mSourceExcerpts.get(position);

//        holder.picture.setImageDrawable(mockPicDrawable);
//        holder.name.setText(ase.getExcerptValue());
//        holder.description.setText(ase.getExcerptValue());

        holder.excerptLocation.setText(ase.getLocation());
        holder.excerptValue.setText(ase.getExcerptValue());
        holder.excerptTagString.setText(ase.getTagString());
    }

    @Override
    public int getItemCount() {
        return mSourceExcerpts.size();
    }
}
