package com.nineworldsdeep.gauntlet.mnemosyne;

import android.content.Context;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.UtilsMnemosyneV5;
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
import java.util.ArrayList;

/**
 * Created by brent on 10/5/16.
 */
public class AsyncOperationExportMnemosyneV5ToXml extends AsyncOperation {

    public AsyncOperationExportMnemosyneV5ToXml(IStatusActivity statusActivity) {
        super(statusActivity, "Exporting Synergy V5 to XML");
    }

    @Override
    protected void runOperation() {

        asdf;

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

        try{

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

                asdf;


                v5List.sync(ctx, db);

                Element synergyListEl = doc.createElement("synergyList");
                synergyListEl.setAttribute("listName", v5List.getListName());
                synergyListEl.setAttribute("activatedAt",
                                           TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                                v5List.getActivatedAt()));
                synergyListEl.setAttribute("shelvedAt",
                                           TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                                                v5List.getShelvedAt()));
                mnemosyneSubsetEl.appendChild(synergyListEl);

                for(int i = 0; i < v5List.getAllItems().size(); i++){

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
                Configuration.getOutgoingXmlFile_yyyyMMddHHmmss("nwd-mnemosyne-v5");

            Xml.write(outputFile, doc);

        }catch(Exception ex){

            publishProgress(ex.getMessage());
        }

        asdf;
    }
}
