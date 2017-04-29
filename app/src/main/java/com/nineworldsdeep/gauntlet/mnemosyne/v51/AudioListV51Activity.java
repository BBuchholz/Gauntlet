package com.nineworldsdeep.gauntlet.mnemosyne.v51;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.AudioDisplayV5Activity;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaListItem;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.UtilsMnemosyneV5;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import java.io.File;
import java.util.ArrayList;

public class AudioListV51Activity extends ListActivity {

    public static final String EXTRA_CURRENT_PATH =
            "com.nineworldsdeep.gauntlet.AUDIOLIST_CURRENT_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list_v51);

        Intent intent = getIntent();
        String currentPathString = null;

        if(intent.hasExtra(EXTRA_CURRENT_PATH)) {
            currentPathString = intent.getStringExtra(EXTRA_CURRENT_PATH);
        }

        File currentFile = null;

        if(currentPathString != null){

            currentFile = new File(currentPathString);

            if(!currentFile.exists()){

                currentFile = null;
            }
        }

        setListAdapter(new ArrayAdapter<MediaListItem>(
                            this,
                            android.R.layout.simple_list_item_1,
                            new ArrayList<MediaListItem>()){

            @Override
            public View getView(int pos, View convertView, ViewGroup parent){

                if(convertView==null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView=vi.inflate(R.layout.media_list_item, null);
                }

                TextView tvDisplayName = (TextView)convertView.findViewById(R.id.display_name);
                TextView tvTags = (TextView)convertView.findViewById(R.id.tags);
                ImageView ivImage = (ImageView)convertView.findViewById(R.id.img);

                MediaListItem mli = getItem(pos);

                tvDisplayName.setText(mli.getFile().getName());
                tvTags.setText(mli.getTags());

                if(mli.getFile().isDirectory()){

                    ivImage.setImageResource(R.mipmap.ic_nwd_junction);

                }else{

                    ivImage.setImageResource(R.mipmap.ic_nwd_media);
                }

                return convertView;
            }
        });

        new AsyncLoadItems().execute(currentFile);
    }

    class ProgressWrapper{

        private MediaListItem mli;
        private String status;

        public ProgressWrapper(MediaListItem mli, String status){

            this.mli = mli;
            this.status = status;
        }

        public MediaListItem getMediaListItem() {
            return mli;
        }

        public String getStatus() {
            return status;
        }
    }

    class AsyncLoadItems extends AsyncTask<File, ProgressWrapper, String>{


        @Override
        protected void onPreExecute(){

            updateStatus("loading...");
        }

        @Override
        protected String doInBackground(File... directories) {

            int count = 0;
            int total = 0;

            String result = "finished loading successfully";

            try{

                NwdDb db = NwdDb.getInstance(AudioListV51Activity.this);

                db.open();

                ArrayList<MediaListItem> items =
                        UtilsMnemosyneV5.getMediaListItemsAudio(
                                directories[0]);

                total = items.size();

                for (MediaListItem mli : items) {

                    count++;

                    if(mli.isFile()) {

                        mli.hashMedia();
                        db.sync(mli.getMedia());
                    }


                    String msg = "Still loading... (" + count + " of " + total + " items loaded)";

                    publishProgress(new ProgressWrapper(mli, msg));
                }

            }catch (Exception ex){

                result = "Error loading items: " + ex.toString();
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(ProgressWrapper... items) {

            ProgressWrapper pw = items[0];
            ((ArrayAdapter<MediaListItem>)getListAdapter()).add(
                    pw.getMediaListItem());

            updateStatus(pw.getStatus());
        }

        @Override
        protected void onPostExecute(String result) {

            updateStatus(result);

            setupListViewListener();
        }
    }

    private TextView getTextViewStatus() {

        return (TextView)findViewById(R.id.tvStatus);
    }

    private void updateStatus(String statusText){

        getTextViewStatus().setText(statusText);
    }


    private void setupListViewListener() {

        final ListView lvItems = (ListView)findViewById(android.R.id.list);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int idx,
                                    long id) {

                MediaListItem mli =
                        ((ArrayAdapter<MediaListItem>)getListAdapter()).getItem(idx);
                File f = mli.getFile();

                if(f.exists() && f.isFile()){

                    Intent intent = new Intent(view.getContext(),
                            AudioDisplayV5Activity.class);

                    intent.putExtra(
                            AudioDisplayV5Activity.EXTRA_AUDIO_PATH,
                            f.getAbsolutePath()
                    );

                    startActivity(intent);

                }else if(f.exists() && f.isDirectory()){

                    Intent intent = new Intent(view.getContext(),
                            AudioListV51Activity.class);
                    intent.putExtra(
                            AudioListV51Activity.EXTRA_CURRENT_PATH,
                            f.getAbsolutePath()
                    );

                    startActivity(intent);

                }else{

                    Utils.toast(view.getContext(), f.getAbsolutePath());
                }
            }
        });
    }

}
