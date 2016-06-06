package com.nineworldsdeep.gauntlet.mnemosyne;

import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

import org.apache.commons.io.FilenameUtils;

/**
 * Created by brent on 3/18/16.
 */
public class AudioMediaEntry extends FileListItem{

//    private String path;
//    private String name;

    public AudioMediaEntry(String filePath, NwdDb db){
        super(filePath, db);
    }

    public String getPath() {

        return getFile().getAbsolutePath();
    }

//    public void setPath(String path) {
//        this.path = path;
//        this.name = FilenameUtils.getName(path);
//    }

//    @Override
//    public String toString(){
//        return "[" + name + "]";
//    }
}
