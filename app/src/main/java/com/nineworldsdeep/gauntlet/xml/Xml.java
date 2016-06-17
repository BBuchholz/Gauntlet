package com.nineworldsdeep.gauntlet.xml;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.model.FileModelItem;
import com.nineworldsdeep.gauntlet.sqlite.model.FileTagModelItem;
import com.nineworldsdeep.gauntlet.sqlite.model.HashModelItem;
import com.nineworldsdeep.gauntlet.sqlite.model.LocalConfigModelItem;
import com.nineworldsdeep.gauntlet.tapestry.TapestryUtils;

import org.apache.commons.lang3.NotImplementedException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by brent on 6/14/16.
 */
public class Xml {

    public static void importMostRecentToDb(Context context,
                                            File xmlSource){

        try {



        }catch(Exception ex){

            Utils.log(context, "Error importing xml: " + ex.getMessage());
        }


        //to test this, export, modify a tag or something, then import and verify changes
    }

    public static void exportFromDb(Context context,
                                    List<LocalConfigModelItem> config,
                                    List<FileModelItem> files,
                                    File destinationFile){

        try {

            Document doc = createDocument("nwd");

            doc.getDocumentElement().appendChild(toConfigElement(doc, config));
            doc.getDocumentElement().appendChild(toFilesElement(doc, files));

            write(destinationFile, doc);

        }catch(Exception ex){

            Utils.log(context, "Error exporting xml: " + ex.getMessage());
        }
    }

    private static Element toFilesElement(Document doc,
                                          List<FileModelItem> files) {

        Element filesEl = doc.createElement("files");

        for(FileModelItem fmi : files){

            filesEl.appendChild(toElement(doc, fmi));
        }

        return filesEl;
    }

    private static Element toConfigElement(Document doc,
                                           List<LocalConfigModelItem> config) {

        Element cfgEl = doc.createElement("local-config");
        cfgEl.setAttribute("device", TapestryUtils.getCurrentDeviceName());

        for(LocalConfigModelItem lcmi : config){

            cfgEl.appendChild(toElement(doc, lcmi));
        }

        return cfgEl;
    }

    private static Element toElement(Document doc,
                                     FileModelItem fmi) {

        Element fileEl = doc.createElement("file");

        fileEl.appendChild(createTextElement(doc,"device",fmi.getDevice()));
        fileEl.appendChild(createTextElement(doc,"path",fmi.getPath()));

        if(fmi.getDisplayName() != null) {

            fileEl.appendChild(createTextElement(doc, "display-name", fmi.getDisplayName()));
        }

        if(fmi.getHashes() != null) {

            fileEl.appendChild(createHashesElement(doc, fmi));
        }

        if(fmi.getDescription() != null) {

            fileEl.appendChild(createTextElement(doc,"description", fmi.getDescription()));
        }

        if(fmi.getName() != null) {

            fileEl.appendChild(createTextElement(doc,"file-name", fmi.getName()));
        }

        if(fmi.getAudioTranscript() != null) {

            fileEl.appendChild(createTextElement(doc,"audio-transcript", fmi.getAudioTranscript()));
        }

        fileEl.appendChild(createTagsNode(doc, fmi.getTags()));

        return fileEl;
    }

    private static Element createHashesElement(Document doc, FileModelItem fmi) {

        Element hashesEl = doc.createElement("hashes");

        if(fmi.getHashes() != null) {

            for(HashModelItem hmi : fmi.getHashes()){

                hashesEl.appendChild(createHashElement(doc, hmi));
            }
        }

        return hashesEl;
    }

    private static Element createHashElement(Document doc, HashModelItem hmi) {

        Element hashEl = doc.createElement("hash");

        hashEl.setAttribute("hash", hmi.getHash());

        if(hmi.getHashedAt() != null) {

            hashEl.setAttribute("hashedAt", hmi.getHashedAt());
        }

        return hashEl;
    }

    private static Element createTagsNode(Document doc,
                                          ArrayList<String> tags) {

        Element tagsEl = doc.createElement("tags");

        for(String tag : tags){

            tagsEl.appendChild(createTextElement(doc, "tag", tag));
        }

        return tagsEl;
    }

    private static Element createTextElement(Document doc,
                                             String tag,
                                             String content){

        Element el = doc.createElement(tag);
        el.appendChild(doc.createTextNode(content));

        return el;
    }

    private static Element toElement(Document doc,
                                     LocalConfigModelItem lcmi) {

        Element cfgEl = doc.createElement("config-item");
        Element keyEl = doc.createElement("config-key");
        Element valEl = doc.createElement("config-value");

        keyEl.appendChild(doc.createTextNode(lcmi.getKey()));
        valEl.appendChild(doc.createTextNode(lcmi.getValue()));

        cfgEl.appendChild(keyEl);
        cfgEl.appendChild(valEl);

        return cfgEl;
    }

    private static void write(File destinationFile, Document doc)
            throws TransformerException {

        TransformerFactory transformerFactory =
                TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(destinationFile);

        transformer.transform(source, result);
    }

    private static Document createDocument(String rootTag)
            throws ParserConfigurationException {

        DocumentBuilderFactory docFactory =
                DocumentBuilderFactory.newInstance();

        Document doc = docFactory.newDocumentBuilder().newDocument();
        Element root = doc.createElement(rootTag);
        doc.appendChild(root);

        return doc;
    }

    public static XmlImporter getImporter(File source)
            throws IOException, SAXException, ParserConfigurationException {

        return new XmlImporter(source);
    }
}
