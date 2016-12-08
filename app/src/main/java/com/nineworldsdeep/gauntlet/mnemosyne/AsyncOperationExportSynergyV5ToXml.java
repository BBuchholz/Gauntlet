package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;
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

/**
 * Created by brent on 10/5/16.
 */
public class AsyncOperationExportSynergyV5ToXml extends AsyncOperation {

    public AsyncOperationExportSynergyV5ToXml(IStatusActivity statusActivity) {
        super(statusActivity, "Exporting Synergy V5 to XML");
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

            Document doc = Xml.createDocument("nwd");
            Element synergySubsetEl = doc.createElement("synergySubset");
            doc.getDocumentElement().appendChild(synergySubsetEl);

            for(ListEntry entry : SynergyV5Utils.getAllListEntries(ctx, db)){

                String listName = entry.getListName();

                publishProgress("processing list [" + listName + "]");

                SynergyV5List v5List = new SynergyV5List(listName);

                v5List.load(ctx, db);

                Element synergyListEl = doc.createElement("synergyList");
                synergyListEl.setAttribute("listName", v5List.getListName());
                synergyListEl.setAttribute("activatedAt",
                                           TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                                v5List.getActivatedAt()));
                synergyListEl.setAttribute("shelvedAt",
                                           TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                                v5List.getShelvedAt()));
                synergySubsetEl.appendChild(synergyListEl);

                for(int i = 0; i < v5List.getItems().size(); i++){

                    Element synergyItemEl = doc.createElement("synergyItem");
                    synergyItemEl.setAttribute("position", Integer.toString(i));

                    synergyListEl.appendChild(synergyItemEl);

                    SynergyV5ListItem sli = v5List.get(i);

                    Element itemValueEl = doc.createElement("itemValue");
                    itemValueEl.setTextContent(sli.getItemValue());

                    synergyItemEl.appendChild(itemValueEl);

                    SynergyV5ToDo toDo = sli.getToDo();

                    if(toDo != null){

                        Element toDoEl = doc.createElement("toDo");

                        toDoEl.setAttribute("activatedAt",
                                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                        toDo.getActivatedAt()));

                        toDoEl.setAttribute("completedAt",
                                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                        toDo.getCompletedAt()));

                        toDoEl.setAttribute("archivedAt",
                                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                        toDo.getArchivedAt()));

                        synergyItemEl.appendChild(toDoEl);
                    }
                }
            }

            publishProgress("writing to file...");

            File outputFile =
                Configuration.getOutgoingXmlFile_yyyyMMddHHmmss("nwd-synergy-v5");

            Xml.write(outputFile, doc);

        }catch(Exception ex){

            publishProgress(ex.getMessage());
        }
    }
}
