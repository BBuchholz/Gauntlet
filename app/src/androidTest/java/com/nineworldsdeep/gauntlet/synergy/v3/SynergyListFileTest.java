package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.Configuration;

import junit.framework.TestCase;

/**
 * Created by brent on 1/21/16.
 */
public class SynergyListFileTest extends TestCase {

    private SynergyListFile slf;
    private String defaultListName = "TestingSynergyListFile";

    public void setUp() throws Exception {
        super.setUp();

        Configuration.setTestMode(true);

        slf = new SynergyListFile(defaultListName);

        if(slf.exists()){
            slf.delete();
        }
    }

    public void testGet() throws Exception {

        String testText1 = "test item 1";
        String testText2 = "test item 2";
        String testText3 = "test item 3";

        slf.add(new SynergyListItem(testText1));
        slf.add(new SynergyListItem(testText2));
        slf.add(new SynergyListItem(testText3));

        assertEquals(testText1, slf.get(0).getText());
        assertEquals(testText2, slf.get(1).getText());
        assertEquals(testText3, slf.get(2).getText());
    }

    public void testGetListName() throws Exception {

    }

    public void testReplace() throws Exception {

    }

    public void testSave() throws Exception {

    }

    public void testSize() throws Exception {

    }

    public void testMove() throws Exception {

    }

    public void testHasCategorizedItems() throws Exception {

    }

    public void testGetFirstCategorizedItemPosition() throws Exception {

    }

    public void testQueueToDailyToDo() throws Exception {

    }

    public void testShelve() throws Exception {

    }

    public void testRemove() throws Exception {

    }

    public void testAdd() throws Exception {

    }

    public void testAdd1() throws Exception {

    }

    public void testAdd2() throws Exception {

    }

    public void testLoadItems() throws Exception {

    }

    public void testGetItems() throws Exception {

    }

    public void testArchiveOne() throws Exception {

    }
}