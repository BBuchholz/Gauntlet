package com.nineworldsdeep.gauntlet.tapestry;

import android.content.Context;
import android.content.Intent;

import com.nineworldsdeep.gauntlet.R;

import java.util.HashMap;

/**
 * Created by brent on 5/4/16.
 */
public class MetaEntry extends HashMap<String, String> {

    private HashedPathLink mHashedPathLink;
    private static String mDescriptionKey = "path";

    public MetaEntry(HashedPathLink hpl){

        mHashedPathLink = hpl;

        setPath(hpl.getPath());
        setHash(hpl.getHash());
        setHashedAt(hpl.getHashedAt());
        put("img", hpl.getImg()); //use the same icon as the referenced media

        setDevice(TapestryUtils.getCurrentDevice());
        MetaRegistry.fill(this);
    }

    public String getDevice() {

        return get("device");
    }

    public void setDevice(String device) {

        put("device", device);
    }

    public String getHash() {

        return get("hash");
    }

    public void setHash(String hash) {

        put("hash", hash);
    }

    public String getPath() {

        return get("path");
    }

    public void setPath(String path) {

        put("path", path);
    }

    public String getHashedAt() {

        return get("hashedAt");
    }

    public void setHashedAt(String hashedAt) {

        put("hashedAt", hashedAt);
    }

    public String getTags() {

        return get("tags");
    }

    public void setTags(String tags) {

        put("tags", tags);
    }

    public String getDisplayName() {

        return get("displayName");
    }

    public void setDisplayName(String displayName) {

        put("displayName", displayName);
    }

    public static String getDescriptionKey(){

        return mDescriptionKey;
    }

    public static void setDescriptionKey(String key){

        mDescriptionKey = key;
    }

    public static int getLayout() {

        return R.layout.meta_entry;
    }

    public static String[] getMapKeysForView() {

        return  new String[] {"img",
                              "displayName",
                              getDescriptionKey()};
    }

    public static int[] getIdsForViewElements() {

        return new int[] {R.id.img,
                          R.id.display_name,
                          R.id.description};
    }

    public Intent getIntent(Context context) {

        return mHashedPathLink.getIntent(context);
    }
}
