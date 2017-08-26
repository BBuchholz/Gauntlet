package com.nineworldsdeep.gauntlet.synergy.v5;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.xml.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Created by brent on 7/25/17.
 */

public class UtilsSynergyV5 {

    /**
     * syncs from database prior to export, only needs a list of SynergyList names
     * to sync for the output...
     * @param listNamesToExport
     * @param db
     */
    public static void exportToXml(
            ArrayList<String> listNamesToExport,
            NwdDb db,
            Context ctx)
            throws ParserConfigurationException, TransformerException {


        Document doc = getDocument(listNamesToExport, db, ctx);

        File outputFile =
            Configuration.getOutgoingXmlFile_yyyyMMddHHmmss(Xml.FILE_NAME_SYNERGY_V5);

        Xml.write(outputFile, doc);
    }

    @NonNull
    private static Document getDocument(ArrayList<String> listNamesToExport, NwdDb db, Context ctx) throws ParserConfigurationException {

        Document doc = Xml.createDocument(Xml.TAG_NWD);
        Element synergySubsetEl = doc.createElement(Xml.TAG_SYNERGY_SUBSET);
        doc.getDocumentElement().appendChild(synergySubsetEl);

        for(String listName : listNamesToExport){

            SynergyV5List v5List = new SynergyV5List(listName);

            v5List.sync(ctx, db);

            Element synergyListEl = doc.createElement(Xml.TAG_SYNERGY_LIST);
            synergyListEl.setAttribute(Xml.ATTR_LIST_NAME, v5List.getListName());
            synergyListEl.setAttribute(Xml.ATTR_ACTIVATED_AT,
                                       TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                            v5List.getActivatedAt()));
            synergyListEl.setAttribute(Xml.ATTR_SHELVED_AT,
                                       TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                            v5List.getShelvedAt()));
            synergySubsetEl.appendChild(synergyListEl);

            for(int i = 0; i < v5List.getAllItems().size(); i++){

                Element synergyItemEl = doc.createElement(Xml.TAG_SYNERGY_ITEM);
                synergyItemEl.setAttribute(Xml.ATTR_POSITION, Integer.toString(i));

                synergyListEl.appendChild(synergyItemEl);

                SynergyV5ListItem sli = v5List.get(i);

                Element itemValueEl = doc.createElement(Xml.TAG_ITEM_VALUE);
                itemValueEl.setTextContent(sli.getItemValue());

                synergyItemEl.appendChild(itemValueEl);

                SynergyV5ToDo toDo = sli.getToDo();

                if(toDo != null){

                    Element toDoEl = doc.createElement(Xml.TAG_TO_DO);

                    toDoEl.setAttribute(Xml.ATTR_ACTIVATED_AT,
                            TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                    toDo.getActivatedAt()));

                    toDoEl.setAttribute(Xml.ATTR_COMPLETED_AT,
                            TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                    toDo.getCompletedAt()));

                    toDoEl.setAttribute(Xml.ATTR_ARCHIVED_AT,
                            TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                    toDo.getArchivedAt()));

                    synergyItemEl.appendChild(toDoEl);
                }

            }
        }
        return doc;
    }


    public static void hiveExportToXml(
            ArrayList<String> listNamesToExport,
            NwdDb db,
            Context ctx)
            throws ParserConfigurationException, TransformerException {


        Document doc = getDocument(listNamesToExport, db, ctx);

        for(File outputFile :
                Configuration.getOutgoingHiveXmlFiles_yyyyMMddHHmmss(
                        ctx,
                        db,
                        Xml.FILE_NAME_SYNERGY_V5))
        {
            Xml.write(outputFile, doc);
        }
    }
}
