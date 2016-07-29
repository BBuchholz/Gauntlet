package com.nineworldsdeep.gauntlet;

import com.nineworldsdeep.gauntlet.core.Configuration;

import junit.framework.TestCase;

import java.io.File;

/**
 * Created by brent on 12/31/15.
 */
public class ConfigurationTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

        Configuration.setTestMode(true);
    }

    public void tearDown() throws Exception {

    }

    public void testGetSynergyDirectory() throws Exception {

        Configuration.setTestMode(false);

        assertEquals(new File("/storage/emulated/0/NWD/synergy"),
                Configuration.getSynergyDirectory());
    }

    public void testGetArchiveDirectory() throws Exception {

        Configuration.setTestMode(false);

        assertEquals(new File("/storage/emulated/0/NWD/synergy/archived"),
                Configuration.getArchiveDirectory());
    }

    public void testGetTemplateDirectory() throws Exception {

        Configuration.setTestMode(false);

        assertEquals(new File("/storage/emulated/0/NWD/synergy/templates"),
                Configuration.getTemplateDirectory());
    }

    public void testGetMuseSessionNotesDirectory() throws Exception {

        Configuration.setTestMode(false);

        assertEquals(new File("/storage/emulated/0/NWD/muse/sessions"),
                Configuration.getMuseSessionNotesDirectory());
    }

    public void testGetBookSegmentsDirectory() throws Exception {

        Configuration.setTestMode(false);


        assertEquals(new File("/storage/emulated/0/NWD/bookSegments"),
                Configuration.getBookSegmentsDirectory());
    }

    public void testGetConfigDirectory() throws Exception {

        Configuration.setTestMode(false);

        assertEquals(new File("/storage/emulated/0/NWD/config"),
                Configuration.getConfigDirectory());
    }

    public void testGetImagesDirectory() throws Exception {

        Configuration.setTestMode(false);

        assertEquals(new File("/storage/emulated/0/NWD-MEDIA/images"),
                Configuration.getImagesDirectory());
    }

    public void testGetAudioDirectory() throws Exception {

        Configuration.setTestMode(false);

        assertEquals(new File("/storage/emulated/0/NWD-MEDIA/audio"),
                Configuration.getAudioDirectory());
    }

    public void testGetVoicememosDirectory() throws Exception {

        Configuration.setTestMode(false);

        assertEquals(new File("/storage/emulated/0/NWD-AUX/voicememos"),
                Configuration.getVoicememosDirectory());
    }

    public void testGetSdCardMediaMusicDirectory() throws Exception {

        Configuration.setTestMode(false);

        File externalSD = new File("/storage/external_SD");

        if(externalSD.exists()){

            assertEquals(new File("/storage/external_SD/NWD-MEDIA"),
                Configuration.getSdCardMediaMusicDirectory());

        }else{

            assertNull(Configuration.getSdCardMediaMusicDirectory());
        }
    }

    public void testGetAllSynergyListNames() throws Exception {
//        assertEquals(new File("EXPECTED HERE"),
//                Configuration.getAllSynergyListNames());
//        fail("prototype - not sure how to handle these since they're integration tests with file system");
    }

    public void testGetAllTemplateListNames() throws Exception {
//        assertEquals(new File("EXPECTED HERE"),
//                Configuration.getAllTemplateListNames());
//        fail("prototype - not sure how to handle these since they're integration tests with file system");
    }

    public void testGetAllArchiveListNames() throws Exception {
//        assertEquals(new File("EXPECTED HERE"),
//                Configuration.getAllArchiveListNames());
//        fail("prototype - not sure how to handle these since they're integration tests with file system");
    }

    public void testGetCameraDirectory() throws Exception {

        Configuration.setTestMode(false);

        assertEquals(new File("/storage/emulated/0/DCIM/Camera"),
                Configuration.getCameraDirectory());
    }

    public void testGetScreenshotDirectory() throws Exception {

        Configuration.setTestMode(false);

        assertEquals(new File("/storage/emulated/0/Pictures/Screenshots"),
                Configuration.getScreenshotDirectory());
    }

    public void testGetSkitchDirectory() throws Exception {

        Configuration.setTestMode(false);

        assertEquals(new File("/storage/emulated/0/Pictures/Skitch"),
                Configuration.getSkitchDirectory());
    }

    public void testGetSynergyDirectoryTestMode() throws Exception {
        assertEquals(new File("/storage/emulated/0/NWD-SNDBX/NWD/synergy"),
                Configuration.getSynergyDirectory());
    }

    public void testGetArchiveDirectoryTestMode() throws Exception {

        assertEquals(new File("/storage/emulated/0/NWD-SNDBX/NWD/synergy/archived"),
                Configuration.getArchiveDirectory());
    }

    public void testGetTemplateDirectoryTestMode() throws Exception {

        assertEquals(new File("/storage/emulated/0/NWD-SNDBX/NWD/synergy/templates"),
                Configuration.getTemplateDirectory());
    }

    public void testGetMuseSessionNotesDirectoryTestMode() throws Exception {

        assertEquals(new File("/storage/emulated/0/NWD-SNDBX/NWD/muse/sessions"),
                Configuration.getMuseSessionNotesDirectory());
    }

    public void testGetBookSegmentsDirectoryTestMode() throws Exception {
        assertEquals(new File("/storage/emulated/0/NWD-SNDBX/NWD/bookSegments"),
                Configuration.getBookSegmentsDirectory());
    }

    public void testGetConfigDirectoryTestMode() throws Exception {
        assertEquals(new File("/storage/emulated/0/NWD-SNDBX/NWD/config"),
                Configuration.getConfigDirectory());
    }

    public void testGetImagesDirectoryTestMode() throws Exception {
        assertEquals(new File("/storage/emulated/0/NWD-SNDBX/NWD-MEDIA/images"),
                Configuration.getImagesDirectory());
    }

    public void testGetAudioDirectoryTestMode() throws Exception {
        assertEquals(new File("/storage/emulated/0/NWD-SNDBX/NWD-MEDIA/audio"),
                Configuration.getAudioDirectory());
    }

    public void testGetVoicememosDirectoryTestMode() throws Exception {
        assertEquals(new File("/storage/emulated/0/NWD-SNDBX/NWD-AUX/voicememos"),
                Configuration.getVoicememosDirectory());
    }

    public void testGetSdCardMediaMusicDirectoryTestMode() throws Exception {
        File externalSD = new File("/storage/external_SD");

        if(externalSD.exists()){

            assertEquals(new File("/storage/external_SD/NWD-SNDBX/NWD-MEDIA"),
                    Configuration.getSdCardMediaMusicDirectory());
        }else{

            assertNull(Configuration.getSdCardMediaMusicDirectory());
        }
    }

    public void testGetAllSynergyListNamesTestMode() throws Exception {
//        assertEquals(new File("EXPECTED HERE"),
//                Configuration.getAllSynergyListNames());
//        fail("prototype - not sure how to handle these since they're integration tests with file system");
    }

    public void testGetAllTemplateListNamesTestMode() throws Exception {
//        assertEquals(new File("EXPECTED HERE"),
//                Configuration.getAllTemplateListNames());
//        fail("prototype - not sure how to handle these since they're integration tests with file system");
    }

    public void testGetAllArchiveListNamesTestMode() throws Exception {
//        assertEquals(new File("EXPECTED HERE"),
//                Configuration.getAllArchiveListNames());
//        fail("prototype - not sure how to handle these since they're integration tests with file system");
    }

    public void testGetCameraDirectoryTestMode() throws Exception {
        assertEquals(new File("/storage/emulated/0/NWD-SNDBX/DCIM/Camera"),
                Configuration.getCameraDirectory());
    }

    public void testGetScreenshotDirectoryTestMode() throws Exception {
        assertEquals(new File("/storage/emulated/0/NWD-SNDBX/Pictures/Screenshots"),
                Configuration.getScreenshotDirectory());
    }

    public void testGetSkitchDirectoryTestMode() throws Exception {
        assertEquals(new File("/storage/emulated/0/NWD-SNDBX/Pictures/Skitch"),
                Configuration.getSkitchDirectory());
    }
}