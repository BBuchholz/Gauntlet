package com.nineworldsdeep.gauntlet.archivist.v5.async;

import android.content.Context;

import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSource;
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceExcerpt;
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceExcerptTagging;
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceLocationEntry;
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceType;
import com.nineworldsdeep.gauntlet.archivist.v5.UtilsArchivist;
import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.xml.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AsyncOperationHiveExportArchivistV5ToXml extends AsyncOperation {

    public AsyncOperationHiveExportArchivistV5ToXml(IStatusResponsive statusActivity) {
        super(statusActivity, "Exporting Archivist V5 to Hive XML");
    }

    @Override
    protected void runOperation() throws Exception {

        Context ctx = statusEnabledActivity.getAsActivity();

        NwdDb db = NwdDb.getInstance(ctx);
        db.open();

        //example of desired output:

        //<nwd>
        //	<archivistSubset>
        //    	<source type="SourceTypeValue"
        //        author="SourceAuthor"
        //        director="SourceDirector"
        //        title="SourceTitle"
        //        year="SourceYear"
        //        url="SourceUrl"
        //        retrievalDate="SourceRetrievalDate"
        //        tag="SourceTag">
        //      		<sourceLocationSubsetEntry location="Brent Buchholz Google Drive"
        //                                         locationSubset="NWD-MEDIA/pdfs folder"
        //                                         locationSubsetEntry="some_pdf_file.pdf"
        //                                         verifiedPresent="2016-10-31 13:13:13"
        //                                         verifiedMissing="2017-11-01 15:05:44"/>
        //      		<sourceLocationSubsetEntry location="Brent Buchholz Google Drive"
        //                                         locationSubset="NWD-MEDIA/pdfs folder"
        //                                         locationSubsetEntry="differently_named_file.pdf"
        //                                         verifiedPresent="2016-10-31 13:13:13"
        //                                         verifiedMissing="2017-11-01 15:05:44"/>
        //      		<sourceExcerpt pages="SourceExcerptPages"
        //                             beginTime="SourceExcerptBeginTime"
        //                             endTime="SourceExcerptEndTime">
        //        		    <sourceExcerptValue>
        //                      Source excerpt value field goes here
        //                  </sourceExcerptValue>
        //      			<sourceExcerptAnnotation>
        //      				<sourceExcerptAnnotationValue>
        //                          A source excerpt annotation value field goes here
        //                      </sourceExcerptAnnotationValue>
        //      			</sourceExcerptAnnotation>
        //      			<sourceExcerptAnnotation>
        //      				<sourceExcerptAnnotationValue>
        //                          Another source excerpt annotation value field goes here
        //                      </sourceExcerptAnnotationValue>
        //      			</sourceExcerptAnnotation>
        //      			<tag tagValue='tag2'
        //                       taggedAt='2016-11-03 11:24:00'
        //                       untaggedAt='2016-11-02 10:24:00'/>
        //        	</sourceExcerpt>
        //        </source>
        //    </archivistSubset>
        //</nwd>

//        publishProgress("uncomment below, just testing scaffolding");
//        Thread.sleep(1000);

        Document doc = Xml.createDocument(Xml.TAG_NWD);
        Element archivistSubsetEl = doc.createElement(Xml.TAG_ARCHIVIST_SUBSET);
        doc.getDocumentElement().appendChild(archivistSubsetEl);

        HashMap<Integer, ArchivistSourceType> idsToSourceTypes =
                UtilsArchivist.asIdMap(db.getArchivistSourceTypes());

        ArrayList<ArchivistSource> allSources =
                new ArrayList<>(db.getAllArchivistSources());

        for(ArchivistSource as : allSources){

            publishProgress("processing source [" +
                    as.getShortDescription() + "]");

            Element sourceEl = doc.createElement(Xml.TAG_SOURCE);

            sourceEl.setAttribute(
                    Xml.ATTR_TYPE,
                    idsToSourceTypes.get(as.getSourceTypeId()).getSourceTypeName());

            Xml.setAttributeIfNotNullOrWhitespace(
                    sourceEl,
                    Xml.ATTR_AUTHOR,
                    as.getSourceAuthor()
            );

            Xml.setAttributeIfNotNullOrWhitespace(
                    sourceEl,
                    Xml.ATTR_DIRECTOR,
                    as.getSourceDirector()
            );

            Xml.setAttributeIfNotNullOrWhitespace(
                    sourceEl,
                    Xml.ATTR_TITLE,
                    as.getSourceTitle()
            );

            Xml.setAttributeIfNotNullOrWhitespace(
                    sourceEl,
                    Xml.ATTR_YEAR,
                    as.getSourceYear()
            );

            Xml.setAttributeIfNotNullOrWhitespace(
                    sourceEl,
                    Xml.ATTR_URL,
                    as.getSourceUrl()
            );

            Xml.setAttributeIfNotNullOrWhitespace(
                    sourceEl,
                    Xml.ATTR_RETRIEVAL_DATE,
                    as.getSourceRetrievalDate()
            );

            archivistSubsetEl.appendChild(sourceEl);



            for(ArchivistSourceLocationEntry asle :
                    db.getArchivistSourceLocationSubsetEntriesForSourceId(as.getSourceId())){

                Element locEntryEl = doc.createElement(Xml.TAG_SOURCE_LOCATION_SUBSET_ENTRY);

                locEntryEl.setAttribute(
                    Xml.ATTR_LOCATION,
                    asle.getSourceLocationValue());

                locEntryEl.setAttribute(
                        Xml.ATTR_LOCATION_SUBSET,
                        asle.getSourceLocationSubsetValue());

                locEntryEl.setAttribute(
                        Xml.ATTR_LOCATION_SUBSET_ENTRY,
                        asle.getSourceLocationSubsetEntryName());

                locEntryEl.setAttribute(
                        Xml.ATTR_VERIFIED_PRESENT,
                        TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                asle.getVerifiedPresent()));

                locEntryEl.setAttribute(
                        Xml.ATTR_VERIFIED_MISSING,
                        TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                asle.getVerifiedMissing()));

                sourceEl.appendChild(locEntryEl);
            }

            ArrayList<ArchivistSourceExcerpt> excerpts =
                    db.getArchivistSourceExcerptsForSourceId(as.getSourceId());

            db.populateArchivistSourceExcerptTaggings(excerpts);

            for(ArchivistSourceExcerpt ase : excerpts){

                Element excerptEl =
                        doc.createElement(Xml.TAG_SOURCE_EXCERPT);


                Xml.setAttributeIfNotNullOrWhitespace(
                        excerptEl,
                        Xml.ATTR_PAGES,
                        ase.getExcerptPages()
                );

                Xml.setAttributeIfNotNullOrWhitespace(
                        excerptEl,
                        Xml.ATTR_BEGINTIME,
                        ase.getExcerptBeginTime()
                );

                Xml.setAttributeIfNotNullOrWhitespace(
                        excerptEl,
                        Xml.ATTR_ENDTIME,
                        ase.getExcerptEndTime()
                );

                Element excerptValueEl =
                        doc.createElement(Xml.TAG_SOURCE_EXCERPT_VALUE);
                excerptValueEl.appendChild(doc.createTextNode(ase.getExcerptValue()));
                excerptEl.appendChild(excerptValueEl);

                sourceEl.appendChild(excerptEl);

                for(ArchivistSourceExcerptTagging aset :
                        ase.getExcerptTaggings()) {

                    Element tagEl = doc.createElement(Xml.TAG_TAG);

                    tagEl.setAttribute(
                            Xml.ATTR_TAG_VALUE,
                            aset.getMediaTagValue()
                    );

                    tagEl.setAttribute(
                            Xml.ATTR_TAGGED_AT,
                            TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                aset.getTaggedAt()));

                    tagEl.setAttribute(
                            Xml.ATTR_UNTAGGED_AT,
                            TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                aset.getUntaggedAt()));

                    excerptEl.appendChild(tagEl);
                }
            }
        }

        publishProgress("writing to file...");

        for(File outputFile :
                Configuration.getOutgoingHiveXmlFiles_yyyyMMddHHmmss(
                        ctx,
                        db,
                        Xml.FILE_NAME_ARCHIVIST_V5)) {

            Xml.write(outputFile, doc);
        }
    }
}
