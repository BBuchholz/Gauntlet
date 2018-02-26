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
    private Context context;
    private FragmentStatePagerAdapter fragmentStatePagerAdapter;

    //private final Drawable mockPicDrawable;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        public TextView name;
        FragmentStatePagerAdapter fragmentStatePagerAdapter;

        public ViewHolder(LayoutInflater inflater, final ViewGroup parent, final FragmentStatePagerAdapter fragmentStatePagerAdapter) {
            super(inflater.inflate(
                    R.layout.fragment_archivist_source_types_content, parent, false));

            this.fragmentStatePagerAdapter = fragmentStatePagerAdapter;

            picture = (ImageView) itemView.findViewById(R.id.tile_picture);
            name = (TextView) itemView.findViewById(R.id.tile_title);

            picture.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    String typeName = name.getText().toString();

                    ArchivistSourceType sourceType =
                            ArchivistWorkspace.getSourceTypeByName(typeName);

                    Utils.toast(parent.getContext(),
                            "clicked " + sourceType.getSourceTypeName());

                    ArchivistWorkspace.setFragmentTitle("Sources", typeName);

                    fragmentStatePagerAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    SourceTypeAdapter(Context context, FragmentStatePagerAdapter fragmentStatePagerAdapter){

        mSourceTypes = ArchivistWorkspace.getSourceTypes();
        this.context = context;
        this.fragmentStatePagerAdapter = fragmentStatePagerAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent, fragmentStatePagerAdapter);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ArchivistSourceType srcType = mSourceTypes.get(position);

        holder.picture.setImageDrawable(context.getDrawable(srcType.getSourcePicDrawableResourceId()));
        holder.name.setText(srcType.getSourceTypeName());
    }

    @Override
    public int getItemCount() {
        return mSourceTypes.size();
    }
}
