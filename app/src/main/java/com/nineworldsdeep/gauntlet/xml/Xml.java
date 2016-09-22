package com.nineworldsdeep.gauntlet.xml;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.model.FileNode;
import com.nineworldsdeep.gauntlet.model.HashNode;
import com.nineworldsdeep.gauntlet.model.LocalConfigNode;
import com.nineworldsdeep.gauntlet.model.TagNode;
import com.nineworldsdeep.gauntlet.tapestry.v1.TapestryUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
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
                                    List<LocalConfigNode> config,
                                    List<FileNode> files,
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
                                          List<FileNode> files) {

        Element filesEl = doc.createElement("files");

        for(FileNode fmi : files){

            filesEl.appendChild(toElement(doc, fmi));
        }

        return filesEl;
    }

    private static Element toConfigElement(Document doc,
                                           List<LocalConfigNode> config) {

        Element cfgEl = doc.createElement("local-config");
        cfgEl.setAttribute("device", TapestryUtils.getCurrentDeviceName());

        for(LocalConfigNode lcmi : config){

            cfgEl.appendChild(toElement(doc, lcmi));
        }

        return cfgEl;
    }

    private static Element toElement(Document doc,
                                     FileNode fmi) {

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

    private static Element createHashesElement(Document doc, FileNode fmi) {

        Element hashesEl = doc.createElement("hashes");

        if(fmi.getHashes() != null) {

            Iterator<HashNode> hashes = fmi.getHashes();

            while(hashes.hasNext()){

                HashNode hmi = hashes.next();

                hashesEl.appendChild(createHashElement(doc, hmi));
            }
        }

        return hashesEl;
    }

    private static Element createHashElement(Document doc, HashNode hmi) {

        Element hashEl = doc.createElement("hash");

        hashEl.setAttribute("hash", hmi.getHash());

        if(hmi.getHashedAt() != null) {

            hashEl.setAttribute("hashedAt", hmi.getHashedAt());
        }

        return hashEl;
    }

    private static Element createTagsNode(Document doc,
                                          Iterator<TagNode> tags) {

        Element tagsEl = doc.createElement("tags");

        while(tags.hasNext()){

            TagNode tag = tags.next();

            tagsEl.appendChild(createTextElement(doc, "tag", tag.value()));
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
                                     LocalConfigNode lcmi) {

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
