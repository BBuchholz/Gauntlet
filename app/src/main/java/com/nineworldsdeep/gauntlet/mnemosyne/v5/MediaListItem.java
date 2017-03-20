package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.text.TextUtils;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.DisplayNameDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.FileHashDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MediaListItem {

    private Media media;

    public MediaListItem(String filePath, String tagString){

        media = new Media();
        addPath(filePath);

        if(tagString != null) {

            setTagsFromTagString(tagString);
        }
    }

    public MediaListItem(String path) {

        this(path, "");
    }

    public Media getMedia() {
        return media;
    }

    public void addPath(String filePath){

        media.add(new DevicePath(filePath));
    }

    public void setTagsFromTagString(String tagString){

        for (String tag : tagString.split(",")) {

            media.add(new MediaTagging(tag.trim()));
        }
    }

    public String getTags(){

        ArrayList<String> tagValues = new ArrayList<>();

        for(MediaTagging mt : media.getMediaTaggings()){

            if(mt.isTagged()) {
                tagValues.add(mt.getMediaTagValue());
            }
        }

        return TextUtils.join(", ", tagValues);
    }

    /**
     * As Media can be at multiple paths, and will also
     * hold info for other locations in the ecosystem,
     * this will iterate through all DevicePaths for the
     * underlying Media and will return the first one that
     * exists on the current device.
     *
     * If none are found, will return null
     * @return
     */
    public File getFile() {

        File f = null;

        for(DevicePath dp : media.getDevicePaths().getAll()){

            File temp = new File(dp.getPath());

            if(temp.exists()){

                f = temp;
                break;
            }
        }

        return f;
    }

    public boolean hasTag(String tag) {

        return media.hasTag(tag);
    }

    public void untag(String tag) {

        media.untag(tag);
    }

    public void tag(String tag) {

        media.tag(tag);
    }

    public void hashMedia() throws Exception {

        File f = getFile();

        if(f != null && f.exists()){

            getMedia().setMediaHash(Utils.computeSHA1(f.getAbsolutePath()));
        }
    }
}
