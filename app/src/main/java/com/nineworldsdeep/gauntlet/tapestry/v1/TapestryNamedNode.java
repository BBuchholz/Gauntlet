package com.nineworldsdeep.gauntlet.tapestry.v1;

import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brent on 4/28/16.
 */
public class TapestryNamedNode {

    private String mNodeName;
    private File mNodeFile;
    private HashMap<String, TapestryNamedNodeLink> mNodeLinks;

    public TapestryNamedNode(String nodeName) {

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

                TapestryNamedNodeLink lnk =
                        new ParentLink(parentNodeName);

                mNodeLinks.put(lnk.getNodeName(), lnk);

                nodePartialPath = nodeName.replace("-", "/");
            }

            if(hasJunction(nodePartialPath)){

                TapestryNamedNodeLink lnk =
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
        // empty node which then calls sync() would
        // overwrite everything.
        // thus we always want to loadToDbFromFile the items
        // in the constructor

        try{

            if(exists()){

                if(mNodeFile.isFile()){

                    for (String line : FileUtils.readLines(mNodeFile)){

                        TapestryNamedNodeLink lnk =
                                TapestryNamedNodeLink.fromLineItem(line);

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
                        TapestryNamedNodeLink lnk =
                                new ChildLink(mNodeName + childNodeName);
                        mNodeLinks.put(lnk.getNodeName(), lnk);
                    }
                }
            }

        }catch(Exception ex){

            Utils.log("TapestryNode.loadLinks() error: " + ex.getMessage());
        }

        linkMotherNodeIfParentMissing();
    }

    private void linkMotherNodeIfParentMissing() {

        boolean parentFound = false;

        if(mNodeName.equalsIgnoreCase("MotherNode") ||
                isJunctionNode()){

            parentFound = true; //mother node is own parent, ignore junction nodes
        }

        if(!parentFound){

            for(TapestryNamedNodeLink lnk : getLinks()){

                if(lnk.getLinkType() == LinkType.ParentLink){

                    parentFound = true;
                }
            }
        }

        if(!parentFound){

            add(new ParentLink("MotherNode"));
            TapestryNamedNode mother = new TapestryNamedNode("MotherNode");
            mother.add(new ChildLink(this.getNodeName()));
            mother.save();
        }
    }

    public boolean exists() {

        return mNodeFile.exists();
    }

    public void save() {

        if(isJunctionNode()){
            return; //disable sync for junction nodes
        }

        try{

            ArrayList<String> lst = new ArrayList<>();

            for(TapestryNamedNodeLink link : mNodeLinks.values()){

                if(link.getLinkType() != LinkType.JunctionLink){

                    lst.add(link.toLineItem());
                }
            }

            FileUtils.writeLines(this.mNodeFile, lst);

        }catch (Exception ex){

            Utils.log("TapestryNode.sync() error: " + ex.getMessage());
        }
    }

    public void add(TapestryNamedNodeLink link) {

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

    public ArrayList<TapestryNamedNodeLink> getLinks() {

        return new ArrayList<>(mNodeLinks.values());
    }

    /**
     * searches for links that can be cast
     * to the specified class (so specifying
     * TapestryNodeLink will return all,
     * while HashedMediaLink would return
     * Image and Audio links, as both
     * derive from that class).
     *
     * Specifying an exact type will
     * also work (so SynergyListLink will
     * just return SynergyListLinks, &c.)
     * @param desiredClass
     * @return
     */
    public ArrayList<TapestryNamedNodeLink> getByType(Class desiredClass) {

        ArrayList<TapestryNamedNodeLink> lst =
                new ArrayList<>();

        for(TapestryNamedNodeLink lnk : mNodeLinks.values()){

            if(desiredClass.isAssignableFrom(lnk.getClass())){

                lst.add(lnk);
            }
        }

        return lst;
    }

    public String getNodeName() {

        return mNodeName;
    }

    /**
     * goes through list of external nodes linked to
     * this current node, and remaps those external
     * links to the specified node.
     *
     * node relationships (such as parent to child)
     * are maintained in the remapped node
     * @param nd
     */
    public void remapExternalLinksTo(TapestryNamedNode nd) {

        for(TapestryNamedNodeLink lnk : getLinks()){

            switch (lnk.getLinkType()){

                //only remap these kinds of links
                case ChildLink:
                case ParentLink:
                case PeerLink:

                    TapestryNamedNode extNd = new TapestryNamedNode(lnk.getNodeName());
                    extNd.remapLinks(this, nd);

                default:
                    //do nothing
            }
        }
    }

    private void remapLinks(TapestryNamedNode prevNode, TapestryNamedNode newNode) {

        for(TapestryNamedNodeLink lnk: getLinks()){

            if(lnk.getNodeName().equalsIgnoreCase(prevNode.getNodeName())){

                remove(lnk);
                add(lnk.remapTo(newNode));
            }
        }

        save();
    }

    private void remove(TapestryNamedNodeLink lnk) {

        mNodeLinks.remove(lnk.getNodeName());
    }

    public void delete() {

        if(!mNodeName.equalsIgnoreCase("MotherNode") &&
                mNodeFile != null &&
                mNodeFile.exists()){

            mNodeFile.delete();
        }
    }
}
