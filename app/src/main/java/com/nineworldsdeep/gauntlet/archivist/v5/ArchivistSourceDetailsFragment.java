package com.nineworldsdeep.gauntlet.archivist.v5;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArchivistSourceDetailsFragment extends Fragment {


    public ArchivistSourceDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume(){
        super.onResume();

        ArchivistSource currentSource = ArchivistWorkspace.getCurrentSource();

        if(currentSource != null) {

            TextView tvSourceTypeName = (TextView) getView().findViewById(R.id.tvSourceTypeName);
            TextView tvTitle = (TextView) getView().findViewById(R.id.tvTitle);
            TextView tvAuthor = (TextView) getView().findViewById(R.id.tvAuthor);
            TextView tvDirector = (TextView) getView().findViewById(R.id.tvDirector);
            TextView tvYear = (TextView) getView().findViewById(R.id.tvYear);
            TextView tvUrl = (TextView) getView().findViewById(R.id.tvUrl);
            TextView tvRetrievalDate = (TextView) getView().findViewById(R.id.tvRetrievalDate);

            //TODO: this is ugly, fix it, no time right now
            String sourceTypeName = "SourceTypeName: " + currentSource.getSourceType().getSourceTypeName();
            String title = "Title: " + currentSource.getSourceTitle();
            String author = "Author: " + currentSource.getSourceAuthor();
            String director = "Director: " + currentSource.getSourceDirector();
            String year = "Year: " + currentSource.getSourceYear();
            String uRL = "URL: " + currentSource.getSourceUrl();
            String retrievalDate = "RetrievalDate: " + currentSource.getSourceRetrievalDate();

            tvSourceTypeName.setText(sourceTypeName);
            tvTitle.setText(title);
            tvAuthor.setText(author);
            tvDirector.setText(director);
            tvYear.setText(year);
            tvUrl.setText(uRL);
            tvRetrievalDate.setText(retrievalDate);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_archivist_source_details, container, false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_archivist_source_details, container, false);

        Button button = (Button) view.findViewById(R.id.btnPurge);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.prompt, null);

                TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
                tv.setText("WARNING! This is irreversible and will PERMANENTLY remove all excerpts " +
                        "as well as the source itself from the database. " +
                        "To confirm, type the phrase \"yes i am sure\"");

                android.app.AlertDialog.Builder alertDialogBuilder =
                        new android.app.AlertDialog.Builder(getActivity());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {


                                        String verificationPhrase = userInput.getText().toString();

                                        if(verificationPhrase.equalsIgnoreCase("yes I am sure")){

                                            Utils.toast(getActivity(),"purge source here");

                                        }else{

                                            Utils.toast(getActivity(),"purge source cancelled");
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        Utils.toast(getActivity(),"purge source cancelled");
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        return view;
    }

}
