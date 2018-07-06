package com.nineworldsdeep.gauntlet.xml.archivist;

public class ArchivistXmlLocationEntry {

    private String location, locationSubset, locationSubsetEntry, verifiedPresent, verifiedMissing;

    public ArchivistXmlLocationEntry(){

        //we don't want nulls (messes with db save)
        location = "";
        locationSubset = "";
        locationSubsetEntry = "";
        verifiedPresent = "";
        verifiedMissing = "";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {

        if(location == null){

            location = "";
        }

        this.location = location;
    }

    public String getLocationSubset() {
        return locationSubset;
    }

    public void setLocationSubset(String locationSubset) {

        if(locationSubset == null){

            locationSubset = "";
        }

        this.locationSubset = locationSubset;
    }

    public String getLocationSubsetEntry() {
        return locationSubsetEntry;
    }

    public void setLocationSubsetEntry(String locationSubsetEntry) {

        if(locationSubsetEntry == null){

            locationSubsetEntry = "";
        }

        this.locationSubsetEntry = locationSubsetEntry;
    }

    public String getVerifiedPresent() {
        return verifiedPresent;
    }

    public void setVerifiedPresent(String verifiedPresent) {

        if(verifiedPresent == null){

            verifiedPresent = "";
        }

        this.verifiedPresent = verifiedPresent;
    }

    public String getVerifiedMissing() {
        return verifiedMissing;
    }

    public void setVerifiedMissing(String verifiedMissing) {

        if(verifiedMissing == null){

            verifiedMissing = "";
        }

        this.verifiedMissing = verifiedMissing;
    }
}
