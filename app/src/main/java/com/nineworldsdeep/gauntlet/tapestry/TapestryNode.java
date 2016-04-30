package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brent on 4/28/16.
 */
public class TapestryNode {

    private String mNodeName;
    private File mNodeFile;
    private HashMap<String, TapestryNodeLink> mNodeLinks;

    public TapestryNode(String nodeName) {

        if(Utils.stringIsNullOrWhitespace(nodeName)){

            throw new IllegalArgumentException("blank or null node mNodeName");
        }

        nodeName = nodeName.trim();

        this.mNodeName = nodeName;

        this.mNodeLinks = new HashMap<>();

        String nodePartialPath = nodeName;

        //add txt to non-junction paths
        if(isJunctionNode()){

            nodePartialPath = nodeName.substring(0, nodeName.length() - 1);

        }else {

            if(nodeName.contains("-")){

                //ensure parent link
                String [] temp = nodeName.split("-");
                String toTrim = "-" + temp[temp.length - 1];
                String parentNodeName =
                        nodeName.substring(0,
                                nodeName.length() - toTrim.length());

                TapestryNodeLink lnk =
                        new ParentLink(parentNodeName);

                mNodeLinks.put(lnk.getNodeName(), lnk);

                nodePartialPath = nodeName.replace("-", "/");
            }

            if(hasJunction(nodePartialPath)){

                TapestryNodeLink lnk =
                        new JunctionLink(nodeName + "-");

                mNodeLinks.put(lnk.getNodeName(), lnk);

            }

            nodePartialPath += ".txt";
        }

        this.mNodeFile =
                new File(Configuration.getTapestryDirectory(),
                         nodePartialPath);

        loadLinks();
    }

    public static boolean hasJunction(String nodePartialPath){

        if(Utils.stringIsNullOrWhitespace(nodePartialPath)){

            return false;
        }

        File temp = new File(Configuration.getTapestryDirectory(),
                             nodePartialPath);

        return temp.exists() && temp.isDirectory();
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

                if(mNodeFile.isFile()){

                    for (String line : FileUtils.readLines(mNodeFile)){

                        TapestryNodeLink lnk =
                                TapestryNodeLink.fromLineItem(line);

                        //prevents child links from appearing
                        //in nodes with associated junctions
                        if(!lnk.getNodeName().startsWith(mNodeName + "-")){

                            mNodeLinks.put(lnk.getNodeName(), lnk);
                        }
                    }

                }else if(mNodeFile.isDirectory()){

                    for (String childNodeName
                            : Utils.getAllFileNamesMinusExt(mNodeFile,
                                                            new String[]{"txt"})){
                        TapestryNodeLink lnk =
                                new ChildLink(mNodeName + childNodeName);
                        mNodeLinks.put(lnk.getNodeName(), lnk);
                    }
                }
            }

        }catch(Exception ex){

            Utils.log("TapestryNode.loadLinks() error: " + ex.getMessage());
        }

    }

    public void save() {

        if(isJunctionNode()){
            return; //disable save for junction nodes
        }

        try{

            ArrayList<String> lst = new ArrayList<>();

            for(TapestryNodeLink link : mNodeLinks.values()){

                if(link.getLinkType() != LinkType.JunctionLink){

                    lst.add(link.toLineItem());
                }
            }

            FileUtils.writeLines(this.mNodeFile, lst);

        }catch (Exception ex){

            Utils.log("TapestryNode.save() error: " + ex.getMessage());
        }
    }

    public void add(TapestryNodeLink link) {

        if(isJunctionNode()){
            return; //disable public link add for junction nodes
        }

        //turn all nodeLinks into child nodeLinks
        //for the MotherNode
        if(this.mNodeName.equalsIgnoreCase("MotherNode") &&
                link.getLinkType() != LinkType.ChildLink){

            link = new ChildLink(link.getNodeName());
        }

        mNodeLinks.put(link.getNodeName(), link);
    }

    public boolean isJunctionNode() {

        return mNodeName.endsWith("-");
    }

    public ArrayList<TapestryNodeLink> getLinks() {

        return new ArrayList<TapestryNodeLink>(mNodeLinks.values());
    }
}
