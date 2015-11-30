package com.nineworldsdeep.gauntlet;

import java.util.HashMap;

/**
 * Created by brent on 11/8/15.
 */
public class Fragment {

    private String originalLineItem;
    private String displayKey;
    private HashMap<String, String> meta =
            new HashMap<>();

    public Fragment(String lineItem) {

        originalLineItem = lineItem;
    }

    public void setDisplayKey(String displayKey) {
        this.displayKey = displayKey;
    }

    protected void processExtract(String key){

        Parser p = new Parser();

        meta.put(key, p.extract(key, originalLineItem));
    }

//    @Deprecated
//    protected void processExtractOld(String key){
//
//        String openTag = key + "={";
//
//        if(originalLineItem.contains(openTag)){
//
//            int startIdx =
//                    originalLineItem.indexOf(openTag)
//                    + openTag.length();
//            int endIdx = originalLineItem.indexOf("}", startIdx);
//
//            String val = originalLineItem.substring(startIdx, endIdx);
//
//            if(val.trim().length() > 0){
//
//                meta.put(key, val);
//            }
//        }
//    }

    protected String get(String key){
        return meta.get(key);
    }

    @Override
    public String toString(){

        if(displayKey != null){

            return meta.get(displayKey);
        }

        return "[Fragment.DisplayKey not set]";
    }
}
