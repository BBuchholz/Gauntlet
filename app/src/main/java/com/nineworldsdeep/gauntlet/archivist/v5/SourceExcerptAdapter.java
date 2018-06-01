package com.nineworldsdeep.gauntlet.archivist.v5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.util.ArrayList;

/**
 * Created by brent on 1/13/18.
 */

public class SourceExcerptAdapter extends RecyclerView.Adapter<SourceExcerptAdapter.ViewHolder>{

    private ArrayList<ArchivistSourceExcerpt> mSourceExcerpts;

    private ArchivistActivity parentArchivistActivity;

    public void refreshSourceExcerpts(Context context){

        try {

            mSourceExcerpts = ArchivistWorkspace.getOpenSourceExcerpts(context);
            notifyDataSetChanged();

        } catch (Exception e) {

            Utils.toast(parentArchivistActivity,
                    "error getting excerpts: " + e.getMessage());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView excerptLocation;
        TextView excerptValue;
        TextView excerptTagString;
        ImageButton tagsButtonGoesHere;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {


            super(inflater.inflate(R.layout.fragment_archivist_source_excerpts_content, parent, false));
            excerptLocation = (TextView) itemView.findViewById(R.id.tvExcerptLocation);
            excerptValue = (TextView) itemView.findViewById(R.id.tvExcerptValue);
            excerptTagString = (TextView) itemView.findViewById(R.id.tvTagString);
            tagsButtonGoesHere = (ImageButton) itemView.findViewById(R.id.favorite_button);
        }
    }

    SourceExcerptAdapter(ArchivistActivity parentArchivistActivity){

        try {

            mSourceExcerpts = ArchivistWorkspace.getOpenSourceExcerpts(parentArchivistActivity);

        } catch (Exception e) {

            Utils.toast(parentArchivistActivity,
                    "error getting excerpts: " + e.getMessage());
        }

        this.parentArchivistActivity = parentArchivistActivity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ArchivistSourceExcerpt ase = mSourceExcerpts.get(position);


        holder.excerptLocation.setText(ase.getLocation());
        holder.excerptValue.setText(ase.getExcerptValue());
        holder.excerptTagString.setText(ase.getTagString());
        holder.tagsButtonGoesHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.toast(parentArchivistActivity, "SourceExcerptAdapter tags button clicked for location: " + ase.getLocation());

                LayoutInflater li = LayoutInflater.from(parentArchivistActivity);
                View promptsView = li.inflate(R.layout.prompt, null);

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(parentArchivistActivity);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput2 = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                String currentValue = ase.getTagString();

                if(!Utils.stringIsNullOrWhitespace(currentValue)){
                    userInput2.setText(currentValue);
                }

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        try {

                                            NwdDb db = NwdDb.getInstance(parentArchivistActivity);

                                            ase.setTagsFromTagString(
                                                    userInput2.getText().toString());

                                            //db.sync(currentMediaListItem.getMedia());
                                            //MnemosyneRegistry.sync(currentMediaListItem, db);
                                            ////////////////////////////////////////////////////////////////
                                            // save new tags to db
                                            ///////////////////////////////////////////////////////////////
                                            db.ensureArchivistSourceExcerptTaggings(ase);

                                            refreshSourceExcerpts(parentArchivistActivity);

                                            Utils.toast(parentArchivistActivity,
                                                    "tags set (awaiting implementation)");

                                        } catch (Exception e) {

                                            Utils.toast(parentArchivistActivity,
                                                    "error setting tag string: " +
                                                            e.getMessage());
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        holder.excerptValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(),
                        ArchivistSourceExcerptDetailActivity.class);


                intent.putExtra(
                        ArchivistSourceExcerptDetailActivity.EXTRA_SOURCE_ID,
                        ase.getSourceId());

                intent.putExtra(
                        ArchivistSourceExcerptDetailActivity.EXTRA_SOURCE_EXCERPT_ID,
                        ase.getExcerptId());

                intent.putExtra(
                        ArchivistSourceExcerptDetailActivity.EXTRA_SOURCE_EXCERPT_VALUE,
                        ase.getExcerptValue());

                intent.putExtra(
                        ArchivistSourceExcerptDetailActivity.EXTRA_SOURCE_EXCERPT_TAG_STRING,
                        ase.getTagString());

                parentArchivistActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSourceExcerpts.size();
    }
}
