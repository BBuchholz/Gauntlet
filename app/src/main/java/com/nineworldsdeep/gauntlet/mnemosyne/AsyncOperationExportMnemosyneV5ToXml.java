package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.DevicePath;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaTagging;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.xml.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;

public class AsyncOperationExportMnemosyneV5ToXml extends AsyncOperation {

    public AsyncOperationExportMnemosyneV5ToXml(IStatusActivity statusActivity) {
        super(statusActivity, "Exporting Mnemosyne V5 to XML");
    }

    @Override
    protected void runOperation() throws Exception {

        Context ctx = statusEnabledActivity.getAsActivity();

        NwdDb db = NwdDb.getInstance(ctx);
        db.open();

        //example of desired output:

        //<nwd>
        //	<mnemosyneSubset>
        //		<media sha1Hash='da39a3ee5e6b4b0d3255bfef95601890afd80709'
        //		       fileName='someFile.txt'
        //		       description='this is a dummy media example for prototyping'>
        //		    <tag tagValue='tag1'
        //		    	 taggedAt='2016-11-01 09:24:00'
        //		    	 untaggedAt='2016-11-02 10:24:00'/>
        //		    <tag tagValue='tag2'
        //		    	 taggedAt='2016-11-03 11:24:00'
        //		    	 untaggedAt='2016-11-02 10:24:00'/>
        //			<mediaDevice description='galaxy-a'>
        //				<path value='/some/path/to/file'
        //					  verifiedPresent='2016-11-01 09:24:00'
        //					  verifiedMissing='2016-11-02 09:30:00'/>
        //			</mediaDevice>
        //			<mediaDevice description='logos'>
        //				<path value='/some/path/to/file'
        //					  verifiedPresent='2016-11-01 11:24:00'
        //					  verifiedMissing='2016-11-02 11:30:00'/>
        //				<path value='/some/path/to/file'
        //					  verifiedPresent='2016-11-02 10:24:00'
        //					  verifiedMissing='2016-11-03 10:30:00'/>
        //			</mediaDevice>
        //	    </media>
        //	</mnemosyneSubset>
        //</nwd>

//        try{

            Document doc = Xml.createDocument(Xml.TAG_NWD);
            Element mnemosyneSubsetEl = doc.createElement(Xml.TAG_MNEMOSYNE_SUBSET);
            doc.getDocumentElement().appendChild(mnemosyneSubsetEl);

            //get all media, just single table query, to get hash
            //then sync all with db.populateByHash(ArrayList<Media>)

            ArrayList<Media> allMedia =
                    new ArrayList<>(db.getAllMedia().values());

            db.populateTaggingsAndDevicePaths(allMedia);

            for(Media media : allMedia){

                publishProgress("processing media [" + media.getMediaHash() + "]");

                Element mediaEl = doc.createElement(Xml.TAG_MEDIA);

                mediaEl.setAttribute(
                        Xml.ATTR_SHA1_HASH,
                        media.getMediaHash());

//
//                mediaEl.setAttribute(
//                        Xml.ATTR_FILE_NAME,
//                        media.getMediaFileName());
//
//                mediaEl.setAttribute(
//                        Xml.ATTR_DESCRIPTION,
//                        media.getMediaDescription());


                Xml.setAttributeIfNotNullOrWhitespace(
                        mediaEl,
                        Xml.ATTR_FILE_NAME,
                        media.getMediaFileName());

                Xml.setAttributeIfNotNullOrWhitespace(
                        mediaEl,
                        Xml.ATTR_DESCRIPTION,
                        media.getMediaDescription());

                mnemosyneSubsetEl.appendChild(mediaEl);

                for(MediaTagging mt : media.getMediaTaggings()){

                    Element tagEl = doc.createElement(Xml.TAG_TAG);

                    tagEl.setAttribute(
                        Xml.ATTR_TAG_VALUE,
                        mt.getMediaTagValue());

                    tagEl.setAttribute(
                        Xml.ATTR_TAGGED_AT,
                        TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                        mt.getTaggedAt()));

                    tagEl.setAttribute(
                        Xml.ATTR_UNTAGGED_AT,
                        TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                        mt.getUntaggedAt()));

                    mediaEl.appendChild(tagEl);
                }

                for(String deviceName : media.getDevicePaths().keySet()){

                    Element mediaDeviceEl =
                            doc.createElement(Xml.TAG_MEDIA_DEVICE);

                    mediaDeviceEl.setAttribute(
                            Xml.ATTR_DESCRIPTION, deviceName);

                    mediaEl.appendChild(mediaDeviceEl);

                    for(DevicePath dp : media.getDevicePaths().get(deviceName)) {

                        Element pathEl = doc.createElement(Xml.TAG_PATH);

                        pathEl.setAttribute(
                                Xml.ATTR_VALUE,
                                dp.getPath());

                        pathEl.setAttribute(
                                Xml.ATTR_VERIFIED_PRESENT,
                                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                    dp.getVerifiedPresent()));

                        pathEl.setAttribute(
                                Xml.ATTR_VERIFIED_MISSING,
                                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                    dp.getVerifiedMissing()));

                        mediaDeviceEl.appendChild(pathEl);
                    }
                }
            }

            publishProgress("writing to file...");

            File outputFile =
                Configuration.getOutgoingXmlFile_yyyyMMddHHmmss(
                        Xml.FILE_NAME_MNEMOSYNE_V5);

            Xml.write(outputFile, doc);

//        }catch(Exception ex){

//            publishProgress(ex.getMessage());
//        }
    }
}
