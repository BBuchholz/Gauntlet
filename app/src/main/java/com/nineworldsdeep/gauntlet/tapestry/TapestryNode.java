package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by brent on 4/28/16.
 */
public class TapestryNode {

    private String mNodeName;
    private File mNodeFile;
    private ArrayList<TapestryNodeLink> mNodeLinks;

    public TapestryNode(String nodeName) {

        if(Utils.stringIsNullOrWhitespace(nodeName)){

            throw new IllegalArgumentException("blank or null node mNodeName");
        }

        this.mNodeName = nodeName;
        this.mNodeFile =
                new File(Configuration.getTapestryDirectory(),
                         this.mNodeName + ".txt");
        this.mNodeLinks = new ArrayList<>();

        loadLinks();
    }

    private void loadLinks() {

        // NOTE:
        //
        // loadLinks should stay privately accessible, as an
        // empty node which then calls save() would
        // overwrite everything.
        // thus we always want to load the items
        // in the constructor

        try{

            if(mNodeFile.exists()){

                for (String line : FileUtils.readLines(mNodeFile)){

                    mNodeLinks.add(TapestryNodeLink.fromLineItem(line));
                }
            }

        }catch(Exception ex){

            Utils.log("TapestryNode.loadLinks() error: " + ex.getMessage());
        }

    }

    public void save() {

        try{

            ArrayList<String> lst = new ArrayList<>();

            for(TapestryNodeLink link : mNodeLinks){

                lst.add(link.toLineItem());
            }

            FileUtils.writeLines(this.mNodeFile, lst);

        }catch (Exception ex){

            Utils.log("TapestryNode.save() error: " + ex.getMessage());
        }
    }

    public void add(TapestryNodeLink link) {

        //turn all nodeLinks into child nodeLinks
        //for the MotherNode
        if(this.mNodeName.equalsIgnoreCase("MotherNode") &&
                link.getLinkType() != LinkType.ChildLink){

            link = new ChildLink(link.getNodeName());
        }

        mNodeLinks.add(link);
    }

    public ArrayList<TapestryNodeLink> getLinks() {

        return mNodeLinks;
    }
}
