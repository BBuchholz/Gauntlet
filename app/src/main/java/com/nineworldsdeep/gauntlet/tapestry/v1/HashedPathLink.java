package com.nineworldsdeep.gauntlet.tapestry.v1;

import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

/**
 * Created by brent on 4/29/16.
 */
public abstract class HashedPathLink extends TapestryNamedNodeLink {

    public HashedPathLink(String nodeName, LinkType linkType) {
        super(nodeName, linkType);

        put("img", String.valueOf(R.mipmap.ic_nwd_singlenode));
    }

    public String toLineItem(){

        String output = super.toLineItem();

        output += "path={" + getPath() + "} ";

        //get a fresh hash before export
        refreshHash();

        output += "sha1Hash={" + getHash() + "} ";

        return output;
    }

    public String getHash() {

        return get("hash");
    }

    public void refreshHash(){

        try{

            //any hash exception will be caught and value will
            //be unchanged, so we can later search by
            //hash if path fails (eg. because it was moved)
            put("hash", Utils.computeSHA1(getPath()));
            put("hashedAt", SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss());

        }catch(Exception ex){

            Utils.log("ImageLink.getHash() error: " + ex.getMessage());
        }

    }

    public String getHashedAt() {

        return get("hashedAt");
    }

    public String getPath() {

        return get("path");
    }

    public void setPath(String path){

        put("path", path);
    }

    public String getImg(){

        return get("img");
    }

    public void extractHashedPathFromLineItem(String lineItem){

        setPath(Parser.extract("path", lineItem));

        refreshHash(); //try to get a fresh hash
        if(Utils.stringIsNullOrWhitespace(getHash())){

            //if path won't hash, loadToDbFromFile last stored one
            //to possibly locate the file elsewhare
            put("hash", Parser.extract("sha1Hash", lineItem));
        }
    }
}
