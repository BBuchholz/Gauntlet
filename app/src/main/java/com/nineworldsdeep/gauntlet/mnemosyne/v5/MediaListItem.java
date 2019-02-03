package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.text.TextUtils;
import android.util.Log;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.sqlite.DisplayNameDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.FileHashDbIndex;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.sqlite.TagDbIndex;
import com.nineworldsdeep.gauntlet.tapestry.v1.TapestryUtils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MediaListItem {

    private Media media;

//    public MediaListItem(String filePath, String tagString){
//
//        media = new Media();
//        addPath(filePath);
//
//        if(tagString != null) {
//
//            setTagsFromTagString(tagString);
//        }
//    }

    public MediaListItem(String path) {

        media = new Media();
        addPath(path);
    }

    public MediaListItem(Media media){
        this.media = media;
    }

    public Media getMedia() {
        return media;
    }

    public void addPath(String filePath){

        try {

            media.add(
                    new DevicePath(
                            Configuration.getLocalDeviceName(),
                            filePath));

        } catch (Exception e) {

            Log.e("MediaListItem", Log.getStackTraceString(e));
        }
    }

    /**
     *
     * @param tagString
     */
    public void setTagsFromTagString(String tagString){

        //get list of existing tags
        ArrayList<String> existingTags = toTagList(getTags());

        //get list of tags from new tag string
        ArrayList<String> newTags = toTagList(tagString);

        //for each in existing tags, if new tags !contain, untag
        for(String tag : existingTags){

            if(!newTags.contains(tag)){

                media.getTag(tag).untag();
            }
        }

        //for each in new tags, if existing tags !contain, tag
        for(String tag : newTags){

            if(!existingTags.contains(tag)){

                media.getTag(tag).tag();
            }
        }

    }

    private ArrayList<String> toTagList(String tags) {

        //be sure to trim, leave case sensitive though, we have
        //case sensitive duplicates in the db right now, need to
        //allow them until they can be phased out entirely

        ArrayList<String> tagList = new ArrayList<>();

        for(String tag : tags.split(",")){

            tagList.add(tag.trim());
        }

        return tagList;
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

    @Override
    public String toString(){

        String tagString = " {" + getTags() + "}";
        String fileName = "";

        File f = getFile();

        if(f != null){

            fileName = f.getName();
        }

        return fileName + tagString;
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

    public boolean isFile() {

        File f = getFile();

        if(f != null){

            return f.isFile();
        }

        return false;
    }

    /**
     * returns true if tag exists and is tagged
     * @param tag
     * @return
     */
    public boolean isTagged(String tag) {

        if(!media.hasTag(tag)){

            return false;
        }

        return media.getTag(tag).isTagged();
    }

    public String getHash() {

        if(getMedia() == null){
            return null;
        }

        return getMedia().getMediaHash();
    }

    public void setHash(String hash) {

        getMedia().setMediaHash(hash);
    }
}
