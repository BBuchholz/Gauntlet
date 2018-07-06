package com.nineworldsdeep.gauntlet.xml;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceExcerptTagging;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.DevicePath;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaTagging;
import com.nineworldsdeep.gauntlet.model.FileNode;
import com.nineworldsdeep.gauntlet.model.HashNode;
import com.nineworldsdeep.gauntlet.model.LocalConfigNode;
import com.nineworldsdeep.gauntlet.model.TagNode;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5List;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5ListItem;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5ToDo;
import com.nineworldsdeep.gauntlet.xml.archivist.ArchivistXmlLocationEntry;
import com.nineworldsdeep.gauntlet.xml.archivist.ArchivistXmlSource;
import com.nineworldsdeep.gauntlet.xml.archivist.ArchivistXmlSourceExcerpt;
import com.nineworldsdeep.gauntlet.xml.archivist.ArchivistXmlTag;

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

    public ArrayList<ArchivistXmlSource> getArchivistV5TestSources() throws Exception {

        //mock
        ArrayList<ArchivistXmlSource> allSources = new ArrayList<>();
        String testSourceName = "test Source: " + TimeStamp.now().toString();
        ArchivistXmlSource testSource = new ArchivistXmlSource();
        testSource.setSourceType("Book");
        testSource.setTitle(testSourceName);

        ArchivistXmlLocationEntry axle = new ArchivistXmlLocationEntry();
        axle.setLocation("Test Location");
        axle.setLocationSubset("Test Subset");
        axle.setLocationSubsetEntry(testSourceName + ".pdf");

        testSource.add(axle);

        ArchivistXmlSourceExcerpt axse1 = new ArchivistXmlSourceExcerpt();
        axse1.setBeginTime("5:00");
        axse1.setEndTime("6:00");
        axse1.setExcerptValue("test val 1: " + TimeStamp.now().toString());

        ArchivistXmlSourceExcerpt axse2 = new ArchivistXmlSourceExcerpt();
        axse2.setPages("10-15");
        axse2.setExcerptValue("test val 2: " + TimeStamp.now().toString());

        ArchivistXmlTag axt1 = new ArchivistXmlTag();
        axt1.setTagValue("Test");
        axt1.setTaggedAt("2018-06-22 16:45:58");

        ArchivistXmlTag axt2 = new ArchivistXmlTag();
        axt2.setTagValue("Testing");
        axt2.setTaggedAt("2018-06-22 16:45:58");
        axt2.setUntaggedAt("2018-06-22 16:50:58");

        axse1.add(axt1);
        axse1.add(axt2);
        axse2.add(axt1);

        testSource.add(axse1);
        testSource.add(axse2);

        allSources.add(testSource);

        return allSources;
    }

    public ArrayList<Media> getMnemosyneV5Media() throws Exception {

        ArrayList<Media> v5Media = new ArrayList<>();

        NodeList mediaEls = doc.getElementsByTagName(Xml.TAG_MEDIA);

        for(int i = 0; i < mediaEls.getLength(); i++){

            Element mediaEl = (Element) mediaEls.item(i);

            String sha1Hash = mediaEl.getAttribute(Xml.ATTR_SHA1_HASH);
            String fileName = mediaEl.getAttribute(Xml.ATTR_FILE_NAME);
            String description = mediaEl.getAttribute(Xml.ATTR_DESCRIPTION);

            Media media = new Media();
            media.setMediaHash(sha1Hash);
            media.setMediaFileName(fileName);
            media.setMediaDescription(description);

            NodeList tagEls = mediaEl.getElementsByTagName(Xml.TAG_TAG);

            for(int j = 0; j < tagEls.getLength(); j++){

                Element tagEl = (Element) tagEls.item(j);

                String tagValue = tagEl.getAttribute(Xml.ATTR_TAG_VALUE);
                String taggedAtString = tagEl.getAttribute(Xml.ATTR_TAGGED_AT);
                String untaggedAtString =
                        tagEl.getAttribute(Xml.ATTR_UNTAGGED_AT);

                Date taggedAt =
                TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                        taggedAtString
                );

                Date untaggedAt =
                TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                        untaggedAtString
                );

                MediaTagging mt = new MediaTagging(tagValue);
                mt.setTimeStamps(taggedAt, untaggedAt);

                media.add(mt);
            }

            NodeList deviceEls =
                    mediaEl.getElementsByTagName(Xml.TAG_MEDIA_DEVICE);

            for(int j = 0; j < deviceEls.getLength(); j++){

                Element deviceEl = (Element) deviceEls.item(j);

                String deviceName = deviceEl.getAttribute(Xml.ATTR_DESCRIPTION);

                NodeList pathEls =
                        mediaEl.getElementsByTagName(Xml.TAG_PATH);

                for(int k = 0; k < pathEls.getLength(); k++) {

                    Element pathEl = (Element) pathEls.item(k);

                    String pathValue =
                            pathEl.getAttribute(Xml.ATTR_VALUE);

                    String verifiedPresentString =
                            pathEl.getAttribute(Xml.ATTR_VERIFIED_PRESENT);

                    String verifiedMissingString =
                            pathEl.getAttribute(Xml.ATTR_VERIFIED_MISSING);

                    Date verifiedPresent =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    verifiedPresentString
                            );

                    Date verifiedMissing =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    verifiedMissingString
                            );

                    DevicePath dp = new DevicePath(deviceName, pathValue);
                    dp.setTimeStamps(verifiedPresent, verifiedMissing);

                    media.add(dp);
                }
            }

            v5Media.add(media);
        }

        return v5Media;
    }

    public ArrayList<ArchivistXmlSource> getArchivistV5Sources() throws Exception {

        ////testing
        //return getArchivistV5TestSources();

        ArrayList<ArchivistXmlSource> v5Sources = new ArrayList<>();

        NodeList sourceEls = doc.getElementsByTagName(Xml.TAG_SOURCE);

        for(int i = 0; i < sourceEls.getLength(); i++){

            Element sourceEl = (Element) sourceEls.item(i);

            String type = sourceEl.getAttribute(Xml.ATTR_TYPE);
            String author = sourceEl.getAttribute(Xml.ATTR_AUTHOR);
            String director = sourceEl.getAttribute(Xml.ATTR_DIRECTOR);
            String title = sourceEl.getAttribute(Xml.ATTR_TITLE);
            String year = sourceEl.getAttribute(Xml.ATTR_YEAR);
            String url = sourceEl.getAttribute(Xml.ATTR_URL);
            String retrievalDate = sourceEl.getAttribute(Xml.ATTR_RETRIEVAL_DATE);


            ArchivistXmlSource axs = new ArchivistXmlSource();
            axs.setSourceType(type);
            axs.setAuthor(author);
            axs.setDirector(director);
            axs.setTitle(title);
            axs.setYear(year);
            axs.setUrl(url);
            axs.setRetrievalDate(retrievalDate);


            NodeList sourceLocationEntryEls =
                    sourceEl.getElementsByTagName(Xml.TAG_SOURCE_LOCATION_SUBSET_ENTRY);

            for(int j = 0; j < sourceLocationEntryEls.getLength(); j++){

                Element srcLocEntEl = (Element) sourceLocationEntryEls.item(j);

                String location = srcLocEntEl.getAttribute(Xml.ATTR_LOCATION);
                String locSub =
                        srcLocEntEl.getAttribute(Xml.ATTR_LOCATION_SUBSET);
                String locSubEnt =
                        srcLocEntEl.getAttribute(Xml.ATTR_LOCATION_SUBSET_ENTRY);
                String verifiedPresent =
                        srcLocEntEl.getAttribute(Xml.ATTR_VERIFIED_PRESENT);
                String verifiedMissing =
                        srcLocEntEl.getAttribute(Xml.ATTR_VERIFIED_MISSING);

                ArchivistXmlLocationEntry axle =
                        new ArchivistXmlLocationEntry();
                axle.setLocation(location);
                axle.setLocationSubset(locSub);
                axle.setLocationSubsetEntry(locSubEnt);
                axle.setVerifiedPresent(verifiedPresent);
                axle.setVerifiedMissing(verifiedMissing);

                axs.add(axle);
            }

            NodeList excerptEls =
                    sourceEl.getElementsByTagName(Xml.TAG_SOURCE_EXCERPT);

            for(int j = 0; j < excerptEls.getLength(); j++){

                Element excerptEl = (Element) excerptEls.item(j);


                String pages = excerptEl.getAttribute(Xml.ATTR_PAGES);
                String beginTime =
                        excerptEl.getAttribute(Xml.ATTR_BEGINTIME);
                String endTime =
                        excerptEl.getAttribute(Xml.ATTR_ENDTIME);

                String excerptValue =
                        getValueForFirst(excerptEl, Xml.TAG_SOURCE_EXCERPT_VALUE);

                ArchivistXmlSourceExcerpt axse =
                        new ArchivistXmlSourceExcerpt();
                axse.setPages(pages);
                axse.setBeginTime(beginTime);
                axse.setEndTime(endTime);
                axse.setExcerptValue(excerptValue);

                //////////////////////////////tags/////////////////////
                NodeList tagEls =
                        excerptEl.getElementsByTagName(Xml.TAG_TAG);

                for(int k = 0; k < tagEls.getLength(); k++){

                    Element tagEl = (Element) tagEls.item(k);

                    String tagValue = tagEl.getAttribute(Xml.ATTR_TAG_VALUE);

                    String taggedAt =
                            tagEl.getAttribute(Xml.ATTR_TAGGED_AT);
                    String untaggedAt =
                            tagEl.getAttribute(Xml.ATTR_UNTAGGED_AT);


                    ArchivistXmlTag tag =
                            new ArchivistXmlTag();
                    tag.setTagValue(tagValue);
                    tag.setTaggedAt(taggedAt);
                    tag.setUntaggedAt(untaggedAt);

                    axse.add(tag);
                }

                axs.add(axse);
            }

            v5Sources.add(axs);
        }

        return v5Sources;
    }

}
