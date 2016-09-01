package com.nineworldsdeep.gauntlet.xml;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.model.FileModelItem;
import com.nineworldsdeep.gauntlet.model.HashModelItem;
import com.nineworldsdeep.gauntlet.model.LocalConfigModelItem;
import com.nineworldsdeep.gauntlet.model.TagModelItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    public List<LocalConfigModelItem> getConfig() {

        List<LocalConfigModelItem> output =
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

            output.add(new LocalConfigModelItem(
                    keyEl.getTextContent(),
                    valEl.getTextContent()));
        }

        return output;
    }

    public List<FileModelItem> getFiles(Context context) {

        List<FileModelItem> output =
                new ArrayList<>();

        NodeList fileEls = doc.getElementsByTagName("file");

        for(int i = 0; i < fileEls.getLength(); i++){

            try {

                //testing
                if(i == 138){
                    String s = "breakpoint";
                }

                Element fileEl = (Element) fileEls.item(i);

                String device = getValueForFirst(fileEl, "device");
                String path = getValueForFirst(fileEl, "path");

                FileModelItem fmi =
                        new FileModelItem(device, path);

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

                    fmi.getHashes().add(new HashModelItem(hashValue, hashedAt));
                }

                NodeList tags = fileEl.getElementsByTagName("tag");

                for (int j = 0; j < tags.getLength(); j++) {

                    Element tagEl = (Element) tags.item(j);

                    if (tagEl != null) {

                        String tagValue = tagEl.getTextContent();

                        fmi.getTags().add(new TagModelItem(fmi, tagValue));
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

    /**
     * gets the text content value of the first child node of
     * parentEl with tag name childName
     * @param parentEl
     * @param childName
     * @return
     */
    private String getValueForFirst(Element parentEl,
                                    String childName){

        Element displayNameEl =
                (Element) parentEl.getElementsByTagName(childName).item(0);

        String output = null;

        if(displayNameEl != null){

            output = displayNameEl.getTextContent();
        }

        return output;
    }
}
