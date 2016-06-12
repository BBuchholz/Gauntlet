package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.Fragment;

/**
 * Created by brent on 12/1/15.
 */
public class FileTagFragment extends Fragment {

    public FileTagFragment(String lineItem){
        super(lineItem);

        processExtract("path");
        processExtract("tags");

        setDisplayKey("tags");
    }

    public FileTagFragment(String path, String tags){
        this(toLineItem(path, tags));
        //chained constructor
    }

    public static String toLineItem(String path, String tags){
        return "path={" + path + "} tags={" + tags + "}";
    }

    public String getTags() {
        return get("tags");
    }

    public String getPath() {
        return get("path");
    }

//    public void setTagString(String tags) {
//        TagIndex ti = TagIndex.getInstance();
//        ti.setTagString(getPath(), tags);
//        updateLineItem(getPath(), tags);
//    }

    private void updateLineItem(String path, String tags){
        super.updateLineItem(toLineItem(path, tags));
    }
}
