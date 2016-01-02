package com.nineworldsdeep.gauntlet;

import com.nineworldsdeep.gauntlet.synergy.v2.SynergyListFile;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

import junit.framework.TestCase;

/**
 * Created by brent on 1/2/16.
 */
public class UtilsTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

        Configuration.setTestMode(true);
    }

    public void tearDown() throws Exception {

    }

    public void testLog() throws Exception {
        String listName = SynergyUtils.getCurrentTimeStamp_yyyyMMdd() + "-utils-log";

        SynergyListFile slf = new SynergyListFile(listName);
        slf.loadItems();
        int currentCount = slf.size();

        String testLog = "test msg - " + SynergyUtils.getCurrentTimeStamp_yyyyMMdd();
        Utils.log(testLog);

        slf.loadItems();

        assertEquals(currentCount + 1, slf.size());
        assertEquals(testLog, slf.get(slf.size() - 1));

    }
}