package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 1/21/16.
 */
public class SynergyListFileTest extends TestCase {

    private SynergyListFile slf;
    private String defaultListName = "TestingSynergyListFile";
    private Parser p = new Parser();

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

        assertEquals(testText1, slf.get(0).getItem());
        assertEquals(testText2, slf.get(1).getItem());
        assertEquals(testText3, slf.get(2).getItem());
    }

    public void testGetListName() throws Exception {

        assertEquals(defaultListName, slf.getListName());
    }

    public void testReplace() throws Exception {

        String testText1 = "test item 1";
        String testText2 = "test item 2";
        String testText3 = "test item 3";

        slf.add(new SynergyListItem(testText1));
        slf.add(new SynergyListItem(testText2));
        slf.add(new SynergyListItem(testText3));

        assertEquals(testText1, slf.get(0).getItem());
        assertEquals(testText2, slf.get(1).getItem());
        assertEquals(testText3, slf.get(2).getItem());

        ArrayList<SynergyListItem> splitItems = new ArrayList<>();
        SynergyListItem split1 = new SynergyListItem("test");
        SynergyListItem split2 = new SynergyListItem("item 2");
        splitItems.add(split1);
        splitItems.add(split2);

        slf.replace(1, splitItems);

        assertEquals(testText1, slf.get(0).getItem());
        assertEquals("test", slf.get(1).getItem());
        assertEquals("item 2", slf.get(2).getItem());
        assertEquals(testText3, slf.get(3).getItem());
    }

    public void testSaveAndSizeAndLoadItems() throws Exception {

        String testText1 = "test item 1";
        String testText2 = "test item 2";
        String testText3 = "test item 3";

        slf.add(new SynergyListItem(testText1));
        slf.add(new SynergyListItem(testText2));
        slf.add(new SynergyListItem(testText3));

        slf.get(0).append("testKey", "testVal1");
        slf.get(2).append("testKey", "testVal3");

        assertEquals(testText1, slf.get(0).getItem());
        assertEquals(testText2, slf.get(1).getItem());
        assertEquals(testText3, slf.get(2).getItem());

        slf.save();

        slf = null;

        slf = new SynergyListFile(defaultListName);

        assertEquals(0, slf.size());

        slf.loadItems();

        assertEquals(3, slf.size());

        assertEquals(testText1, slf.get(0).getItem());
        assertEquals(testText2, slf.get(1).getItem());
        assertEquals(testText3, slf.get(2).getItem());

        assertEquals("testVal1", slf.get(0).getVal("testKey"));
        assertTrue(Utils.stringIsNullOrWhitespace(slf.get(1).getVal("testKey")));
        assertEquals("testVal3", slf.get(2).getVal("testKey"));
    }

    public void testMove() throws Exception {

        String testText1 = "test item 1";
        String testText2 = "test item 2";
        String testText3 = "test item 3";

        slf.add(new SynergyListItem(testText1));
        slf.add(new SynergyListItem(testText2));
        slf.add(new SynergyListItem(testText3));

        slf.get(0).append("testKey", "testVal1");
        slf.get(2).append("testKey", "testVal3");

        assertEquals(testText1, slf.get(0).getItem());
        assertEquals(testText2, slf.get(1).getItem());
        assertEquals(testText3, slf.get(2).getItem());

        assertEquals("testVal1", slf.get(0).getVal("testKey"));
        assertTrue(Utils.stringIsNullOrWhitespace(slf.get(1).getVal("testKey")));
        assertEquals("testVal3", slf.get(2).getVal("testKey"));

        slf.move(1, 0);

        assertEquals(testText2, slf.get(0).getItem());
        assertEquals(testText1, slf.get(1).getItem());
        assertEquals(testText3, slf.get(2).getItem());

        assertTrue(Utils.stringIsNullOrWhitespace(slf.get(0).getVal("testKey")));
        assertEquals("testVal1", slf.get(1).getVal("testKey"));
        assertEquals("testVal3", slf.get(2).getVal("testKey"));
    }

    public void testHasCategorizedItems() throws Exception {

        String testText1 = "test item 1";
        String testText2 = "::Category:: - test item 2";
        String testText3 = "test item 3";

        slf.add(new SynergyListItem(testText1));
        slf.add(new SynergyListItem(testText3));

        assertFalse(slf.hasCategorizedItems());

        slf.add(1, new SynergyListItem(testText2));

        assertTrue(slf.hasCategorizedItems());
    }

    public void testGetFirstCategorizedItemPosition() throws Exception {

        String testText1 = "test item 1";
        String testText2 = "::Category:: - test item 2";
        String testText3 = "test item 3";

        slf.add(new SynergyListItem(testText1));
        slf.add(new SynergyListItem(testText2));
        slf.add(new SynergyListItem(testText3));

        assertEquals(1, slf.getFirstCategorizedItemPosition());

    }

    public void testQueueToDailyToDo() throws Exception {

        //String dailyToDoName = SynergyUtils.getTimeStampedListName("DailyToDo");

        String dailyToDoName = "000-ActiveQueue";

        SynergyListFile dailyToDo = new SynergyListFile(dailyToDoName);

        if(dailyToDo.exists()){
            dailyToDo.delete();
        }

        String testText1 = "test item 1";
        String testText2 = "test item 2";
        String testText3 = "test item 3";

        slf.add(new SynergyListItem(testText1));
        slf.add(new SynergyListItem(testText2));
        slf.add(new SynergyListItem(testText3));

        dailyToDo.loadItems();

        assertTrue(slf.containsByDeCategorizedItemText(testText2));
        assertFalse(dailyToDo.containsByDeCategorizedItemText(testText2));

        slf.queueToActive(1);
        dailyToDo.loadItems();

        assertFalse(slf.containsByDeCategorizedItemText(testText2));
        assertTrue(dailyToDo.containsByDeCategorizedItemText(testText2));

    }

    public void testContainsByDeCategorizedItemText() throws Exception {

        String testText1 = "test item 1";
        String testText2 = "::Category:: - test item 2";
        String testText3 = "test item 3";

        slf.add(new SynergyListItem(testText1));
        slf.add(new SynergyListItem(testText2));
        slf.add(new SynergyListItem(testText3));

        assertTrue(slf.containsByDeCategorizedItemText("test item 2"));
    }

    public void testShelve() throws Exception {

        String testText1 = "test item 1";
        String testText2 = "::Category:: - test item 2";
        String testText3 = "test item 3";

        slf.add(new SynergyListItem(testText1));
        slf.add(new SynergyListItem(testText2));
        slf.add(new SynergyListItem(testText3));

        assertTrue(slf.containsByDeCategorizedItemText("test item 2"));

        int pos = 1;

        String category = SynergyUtils.parseCategory(slf.get(pos).getItem());

        slf.shelve(pos, category);

        SynergyListFile shelvedTo = new SynergyListFile(category);

        shelvedTo.loadItems();

        assertTrue(shelvedTo.containsByDeCategorizedItemText("test item 2"));
        assertFalse(slf.containsByDeCategorizedItemText("test item 2"));
    }

    public void testGetItems() throws Exception {

        List<SynergyListItem> expected = new ArrayList<>();

        String s1 = "test item 1";
        String s2 = "test item 2";
        String s3 = "test item 3";

        expected.add(new SynergyListItem(s1));
        expected.add(new SynergyListItem(s2));
        expected.add(new SynergyListItem(s3));

        slf = new SynergyListFile("TestingGetItems");

        slf.add(new SynergyListItem(s1));
        slf.add(new SynergyListItem(s2));
        slf.add(new SynergyListItem(s3));

        assertEquals(expected, slf.getItems());
    }

    public void testArchiveOne() throws Exception {

        String s1 = "test item 1";
        String s2 = "test item 2";
        String s3 = "test item 3";

        SynergyListItem sli1 = new SynergyListItem(s1);
        SynergyListItem sli2 = new SynergyListItem(s2);
        SynergyListItem sli3 = new SynergyListItem(s3);

        slf = new SynergyListFile("TestingArchiveOne");
        SynergyArchiveFile saf = new SynergyArchiveFile("TestingArchiveOne");

        //clear previously generated test files
        if(slf.exists()){
            slf.delete();
        }

        if(saf.exists()){
            saf.delete();
        }

        slf.loadItems();
        saf.loadItems();

        slf.add(sli1);
        slf.add(sli2);
        slf.add(sli3);

        assertTrue(slf.getItems().contains(sli1));
        assertTrue(slf.getItems().contains(sli2));
        assertTrue(slf.getItems().contains(sli3));

        assertFalse(saf.getItems().contains(sli1));
        assertFalse(saf.getItems().contains(sli2));
        assertFalse(saf.getItems().contains(sli3));

        //slf.archiveOne(sli2); //should sync internally...

        saf.loadItems();      //...so this should include the newly archived item

        assertTrue(slf.getItems().contains(sli1));
        assertFalse(slf.getItems().contains(sli2));
        assertTrue(slf.getItems().contains(sli3));

        assertFalse(saf.getItems().contains(sli1));
        assertTrue(saf.getItems().contains(sli2));
        assertFalse(saf.getItems().contains(sli3));
    }
}