package com.nineworldsdeep.gauntlet.xml;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.model.FileNode;
import com.nineworldsdeep.gauntlet.model.HashNode;
import com.nineworldsdeep.gauntlet.model.LocalConfigNode;
import com.nineworldsdeep.gauntlet.model.TagNode;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5List;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5ListItem;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5ToDo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by brent on 6/16/16.
 */
public class XmlImporter {

    private Document doc;

    public XmlImporter(File source)
            throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        this.doc = builder.parse(source);
    }

    public List<LocalConfigNode> getConfig() {

        List<LocalConfigNode> output =
                new ArrayList<>();

        NodeList configItems = doc.getElementsByTagName("config-item");

        for(int i = 0; i < configItems.getLength(); i++){

            Element configItem = (Element) configItems.item(i);

            Element keyEl =
                    (Element) configItem.getElementsByTagName("config-key")
                                        .item(0);
            Element valEl =
                    (Element) configItem.getElementsByTagName("config-value")
                                        .item(0);

            output.add(new LocalConfigNode(
                    keyEl.getTextContent(),
                    valEl.getTextContent()));
        }

        return output;
    }

    public List<FileNode> getFiles(Context context) {

        List<FileNode> output =
                new ArrayList<>();

        NodeList fileEls = doc.getElementsByTagName("file");

        for(int i = 0; i < fileEls.getLength(); i++){

            try {

                Element fileEl = (Element) fileEls.item(i);

                String device = getValueForFirst(fileEl, "device");
                String path = getValueForFirst(fileEl, "path");

                FileNode fmi =
                        new FileNode(device, path);

                String displayName = getValueForFirst(fileEl, "display-name");

                if (!Utils.stringIsNullOrWhitespace(displayName)) {

                    fmi.setDisplayName(displayName);
                }

                String audioTranscript =
                        getValueForFirst(fileEl, "audio-transcript");

                if (!Utils.stringIsNullOrWhitespace(audioTranscript)) {

                    fmi.setAudioTranscript(audioTranscript);
                }

                //hashes and tags
                NodeList hashes = fileEl.getElementsByTagName("hash");
                for (int j = 0; j < hashes.getLength(); j++) {

                    Element hashEl = (Element) hashes.item(j);
                    String hashValue = hashEl.getAttribute("hash");
                    String hashedAt = hashEl.getAttribute("hashedAt");

                    fmi.add(new HashNode(hashValue, hashedAt));
                }

                NodeList tags = fileEl.getElementsByTagName("tag");

                for (int j = 0; j < tags.getLength(); j++) {

                    Element tagEl = (Element) tags.item(j);

                    if (tagEl != null) {

                        String tagValue = tagEl.getTextContent();

                        fmi.add(new TagNode(fmi, tagValue));
                    }
                }

                output.add(fmi);

            }catch (Exception ex){

                Utils.log(context, "Error retrieving files from xml: " +
                    ex.getMessage());
            }
        }

        return output;
    }

    public List<SynergyV5List> getSynergyV5Lists() throws ParseException {

        List<SynergyV5List> v5Lists = new ArrayList<>();

        NodeList listEls = doc.getElementsByTagName("synergyList");

        for(int i = 0; i < listEls.getLength(); i++){

            Element v5ListEl = (Element) listEls.item(i);

            String listName = v5ListEl.getAttribute("listName");
            String activatedAtString = v5ListEl.getAttribute("activatedAt");
            String shelvedAtString = v5ListEl.getAttribute("shelvedAt");

            Date activated =
                    TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(activatedAtString);

            Date shelved =
                    TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(shelvedAtString);

            SynergyV5List v5List = new SynergyV5List(listName);

            v5List.setTimeStamps(activated, shelved);

            NodeList v5ListItemEls = v5ListEl.getElementsByTagName("synergyItem");

            for(int j = 0; j < v5ListItemEls.getLength(); j++){

                Element listItemEl = (Element) v5ListItemEls.item(j);

                String itemValue = getValueForFirst(listItemEl, "itemValue");

                Element toDoEl = getFirst(listItemEl, "toDo");

                SynergyV5ListItem v5ListItem =
                        new SynergyV5ListItem(itemValue);

                if(toDoEl != null){

                    String toDoActivatedAtString =
                            toDoEl.getAttribute("activatedAt");
                    String toDoCompletedAtString =
                            toDoEl.getAttribute("completedAt");
                    String toDoArchivedAtString =
                            toDoEl.getAttribute("archivedAt");

                    Date toDoActivated =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    toDoActivatedAtString
                            );
                    Date toDoCompleted =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    toDoCompletedAtString
                            );
                    Date toDoArchived =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    toDoArchivedAtString
                            );

                    SynergyV5ToDo toDo = new SynergyV5ToDo();

                    toDo.setTimeStamps(toDoActivated, toDoCompleted, toDoArchived);

                    v5ListItem.setToDo(toDo);
                }

                v5List.add(v5ListItem);
            }

            v5Lists.add(v5List);
        }

        return v5Lists;
    }

    /**
     * gets the text content value of the first child node of
     * parentEl with tag name childName
     * @param parentEl
     * @param childName
     * @return
     */
    private String getValueForFirst(Element parentEl,
                                    String childName){

//        Element firstEl =
//                (Element) parentEl.getElementsByTagName(childName).item(0);

        Element firstEl = getFirst(parentEl, childName);

        String output = null;

        if(firstEl != null){

            output = firstEl.getTextContent();
        }

        return output;
    }

    private Element getFirst(Element parentEl, String childName){

        return (Element) parentEl.getElementsByTagName(childName).item(0);
    }
}
