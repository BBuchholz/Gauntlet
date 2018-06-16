package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusResponsive;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;
import com.nineworldsdeep.gauntlet.synergy.v2.ListEntry;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5List;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5ListItem;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5ToDo;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5Utils;
import com.nineworldsdeep.gauntlet.xml.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

public class AsyncOperationHiveExportSynergyV5ToXml extends AsyncOperation {

    public AsyncOperationHiveExportSynergyV5ToXml(IStatusResponsive statusActivity) {
        super(statusActivity, "Exporting Synergy V5 to Hive XML");
    }

    @Override
    protected void runOperation() {

        Context ctx = statusEnabledActivity.getAsActivity();

        NwdDb db = NwdDb.getInstance(ctx);
        db.open();

        //nwd
        //  synergySubset
        //      synergyList listName=''
        //          synergyItem position=''
        //              itemValue
        //                  <text content>

        try{

            Document doc = Xml.createDocument(Xml.TAG_NWD);
            Element synergySubsetEl = doc.createElement(Xml.TAG_SYNERGY_SUBSET);
            doc.getDocumentElement().appendChild(synergySubsetEl);

            for(ListEntry entry : SynergyV5Utils.getAllListEntries(ctx, db)){

                String listName = entry.getListName();

                publishProgress("processing list [" + listName + "]");

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

            publishProgress("writing to file...");

//            File outputFile =
//                Configuration.getOutgoingXmlFile_yyyyMMddHHmmss(Xml.FILE_NAME_SYNERGY_V5);
//
//            Xml.write(outputFile, doc);


            for(File outputFile :
                Configuration.getOutgoingHiveXmlFiles_yyyyMMddHHmmss(
                        ctx,
                        db,
                        Xml.FILE_NAME_SYNERGY_V5))
        {
            Xml.write(outputFile, doc);
        }

        }catch(Exception ex){

            publishProgress(ex.getMessage());
        }
    }
}
