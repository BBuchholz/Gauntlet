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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Xml {

    public static final String TAG_NWD = "nwd";

    public static final String TAG_SYNERGY_SUBSET = "synergySubset";
    public static final String TAG_MNEMOSYNE_SUBSET = "mnemosyneSubset";

    public static final String TAG_SYNERGY_LIST = "synergyList";
    public static final String ATTR_LIST_NAME = "listName";
    public static final String ATTR_ACTIVATED_AT = "activatedAt";
    public static final String ATTR_SHELVED_AT = "shelvedAt";
    public static final String TAG_SYNERGY_ITEM = "synergyItem";
    public static final String ATTR_POSITION = "position";
    public static final String TAG_ITEM_VALUE = "itemValue";
    public static final String TAG_TO_DO = "toDo";
    public static final String ATTR_COMPLETED_AT = "completedAt";
    public static final String ATTR_ARCHIVED_AT = "archivedAt";

    public static final String FILE_NAME_SYNERGY_V5 = "nwd-synergy-v5";

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

    public static void write(File destinationFile, Document doc)
            throws TransformerException {

        TransformerFactory transformerFactory =
                TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(destinationFile);

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount", "2");

        transformer.transform(source, result);
    }

    public static Document createDocument(String rootTag)
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
