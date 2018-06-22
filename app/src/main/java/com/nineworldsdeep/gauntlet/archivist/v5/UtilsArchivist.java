package com.nineworldsdeep.gauntlet.archivist.v5;

import java.util.ArrayList;
import java.util.HashMap;

public class UtilsArchivist {
    public static HashMap<Integer, ArchivistSourceType> asIdMap(
            ArrayList<ArchivistSourceType> archivistSourceTypes) {

        HashMap<Integer, ArchivistSourceType> map = new HashMap<>();

        for(ArchivistSourceType ast : archivistSourceTypes){
            map.put(ast.getSourceTypeId(), ast);
        }

        return map;
    }
}
