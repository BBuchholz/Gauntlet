package com.nineworldsdeep.gauntlet.xml;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.NotImplementedException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by brent on 6/16/16.
 */
public class XmlUtils {

    public static File getMostRecentFileFromXmlFolder() {

        // should cycle through all files in folder "NWD/xml/"
        // for each file, use a regular expression to test if it's name
        // fits the pattern "yyyyMMddHHmmss-nwd.xml"
        // from that list, find most recent time stamp and import
        // we can later have code that will remove an import afterwards
        // and can import each in succession. for now just one

        List<String> paths =
                Utils.getAllFilePathsWithExt(
                        Configuration.getXmlDirectory(),
                        new String[]{"xml"});

        List<String> validPaths = new ArrayList<>();

        for(String path : paths){

            String fileName = FilenameUtils.getName(path);

            if(Pattern.matches("^\\d{14}-nwd.xml$", fileName)){

                validPaths.add(path);
            }
        }

        File mostRecent = null;

        if(validPaths.size() > 0){

            Collections.sort(validPaths);
            mostRecent = new File(validPaths.get(validPaths.size() - 1));
        }

        return mostRecent;
    }
}
