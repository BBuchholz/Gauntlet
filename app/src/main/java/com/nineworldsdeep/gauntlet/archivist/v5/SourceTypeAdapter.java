package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Context;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;


public class SourceTypeAdapter extends RecyclerView.Adapter<SourceTypeAdapter.ViewHolder> {

    private ArrayList<ArchivistSourceType> mSourceTypes;

    private ArchivistActivity parentArchivistActivity;

    void refreshSourceTypes(Context context) {

        mSourceTypes = ArchivistWorkspace.getSourceTypes(context);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        public TextView name;
        //private ArchivistActivity parentArchivistActivity;

        public ViewHolder(LayoutInflater inflater, final ViewGroup parent, final ArchivistActivity parentArchivistActivity) {
            super(inflater.inflate(
                    R.layout.fragment_archivist_source_types_content, parent, false));

            //this.parentArchivistActivity = parentArchivistActivity;

            picture = (ImageView) itemView.findViewById(R.id.tile_picture);
            name = (TextView) itemView.findViewById(R.id.tile_title);

            picture.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    String typeName = name.getText().toString();

//                    ArchivistSourceType sourceType =
//                            ArchivistWorkspace.getSourceTypeByName(typeName);

//                    Utils.toast(parent.getContext(),
//                            "clicked " + sourceType.getSourceTypeName());

                    ArchivistWorkspace.setCurrentSourceTypeByName(typeName);
                    ArchivistWorkspace.setCurrentSource(null);

                    parentArchivistActivity.getFragmentStatePagerAdapter().notifyDataSetChanged();
                    parentArchivistActivity.selectSourcesTab();
                    parentArchivistActivity.refreshSources();
                    parentArchivistActivity.refreshSourceExcerpts();
                }
            });
        }
    }

    SourceTypeAdapter(ArchivistActivity parentArchivistActivity){

        mSourceTypes = ArchivistWorkspace.getSourceTypes(parentArchivistActivity);
        this.parentArchivistActivity = parentArchivistActivity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent, parentArchivistActivity);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ArchivistSourceType srcType = mSourceTypes.get(position);

        holder.picture.setImageDrawable(parentArchivistActivity.getDrawable(srcType.getSourcePicDrawableResourceId()));
        holder.name.setText(srcType.getSourceTypeName());
    }

    @Override
    public int getItemCount() {
        return mSourceTypes.size();
    }
}
