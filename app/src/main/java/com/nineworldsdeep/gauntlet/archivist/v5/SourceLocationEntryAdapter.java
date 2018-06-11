package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.ArrayList;


public class SourceLocationEntryAdapter extends RecyclerView.Adapter<SourceLocationEntryAdapter.ViewHolder> {

    private ArrayList<ArchivistSourceLocationEntry> mSourcesLocationEntries;

    private ArchivistSourceDetailsActivity parentArchivistSourceDetailsActivity;

    public void refreshSourceLocationEntries(Context context) {

        mSourcesLocationEntries = ArchivistWorkspace.getSourceLocationEntries(context);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvStatus;



        public ViewHolder(final View itemView) {

            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvSourceLocationEntryName);
            tvStatus = (TextView) itemView.findViewById(R.id.tvSourceLocationEntryStatus);
        }
    }

    SourceLocationEntryAdapter(ArchivistSourceDetailsActivity parent){

        mSourcesLocationEntries = ArchivistWorkspace.getSourceLocationEntries(parent);
        this.parentArchivistSourceDetailsActivity = parent;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {




        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_archivist_source_location_entries_content, parent, false);

        final ViewHolder holder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){

                    ///////////////////////////// BEGIN--> LEAVE THIS HERE FOR FUTURE REFERENCE (from adaptation) WILL IMPLEMENT CLICK BEHAVIOR IN FUTURE ITERATION /////////
//                    ArchivistSource archivistSource = mSources.get(position);
//                    ArchivistWorkspace.setCurrentSource(archivistSource);
//
//                    parentArchivistActivity.getFragmentStatePagerAdapter().notifyDataSetChanged();
//                    parentArchivistActivity.selectSourceExcerptsTab();
//                    parentArchivistActivity.refreshSourceExcerpts();
                    ///////////////////////////// END--> LEAVE THIS HERE FOR FUTURE REFERENCE (from adaptation) WILL IMPLEMENT CLICK BEHAVIOR IN FUTURE ITERATION /////////

                    Utils.toast(parentArchivistSourceDetailsActivity, "clicked source location entry");
                }
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final int position = holder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {

                    ///////////////////////////// BEGIN--> LEAVE THIS HERE FOR FUTURE REFERENCE (from adaptation) WILL IMPLEMENT CLICK BEHAVIOR IN FUTURE ITERATION /////////
//                    ArchivistSource archivistSource = mSources.get(position);
//                    ArchivistWorkspace.setCurrentSource(archivistSource);
                    ///////////////////////////// END--> LEAVE THIS HERE FOR FUTURE REFERENCE (from adaptation) WILL IMPLEMENT CLICK BEHAVIOR IN FUTURE ITERATION /////////


                    Utils.toast(parentArchivistSourceDetailsActivity,
                            "source location entry long press");
                }

                //consume the long click
                return true;
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ArchivistSourceLocationEntry asle = mSourcesLocationEntries.get(position);

        holder.tvName.setText(asle.getSourceLocationSubsetEntryName());
        holder.tvStatus.setText(asle.getStatus());
    }

    @Override
    public int getItemCount() {
        return mSourcesLocationEntries.size();
}
}
