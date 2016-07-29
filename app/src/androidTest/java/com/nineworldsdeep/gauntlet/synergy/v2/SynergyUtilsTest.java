package com.nineworldsdeep.gauntlet.synergy.v2;

import com.nineworldsdeep.gauntlet.core.Configuration;

import junit.framework.TestCase;

/**
 * Created by brent on 1/31/16.
 */
public class SynergyUtilsTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

        //DO NOT CHANGE THIS
        Configuration.setTestMode(true);

    }

    public void testPush() throws Exception {

        String categoryFileName = "Testing";
        String queueFileName = "20160101-DailyTestToDo";
        String expectedPushFileName = "20160102-DailyTestToDo";

        //initialize files
        SynergyListFile categoryFile =
                new SynergyListFile(categoryFileName);
        SynergyListFile queueFile =
                new SynergyListFile(queueFileName);
        SynergyListFile expectedPushFile =
                new SynergyListFile(expectedPushFileName);

        //clear files
        if(categoryFile.exists()){
            categoryFile.delete();
        }

        if(queueFile.exists()){
            queueFile.delete();
        }

        if(expectedPushFile.exists()){
            expectedPushFile.delete();
        }

        //loadToDbFromFile fresh
        categoryFile.loadItems();
        queueFile.loadItems();
        expectedPushFile.loadItems();

        //add some test data
        queueFile.add("Test item 1");
        queueFile.add("::Testing:: - Test item 2");
        queueFile.add("Test item 3");

        //save test data
        queueFile.save();

        //push
        SynergyUtils.push(queueFileName);

        //loadToDbFromFile fresh
        categoryFile.loadItems();
        queueFile.loadItems();
        expectedPushFile.loadItems();

        assertEquals(0, queueFile.size()); //should be empty
        assertEquals(1, categoryFile.size()); //should have test item 2 only
        assertEquals(2, expectedPushFile.size()); //should have test items 1 & 3

        assertTrue(categoryFile.contains("Test item 2"));
        assertTrue(expectedPushFile.contains("Test item 1"));
        assertTrue(expectedPushFile.contains("Test item 3"));
    }
}