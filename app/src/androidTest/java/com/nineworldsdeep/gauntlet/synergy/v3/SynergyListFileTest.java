package com.nineworldsdeep.gauntlet.synergy.v3;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.Parser;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.synergy.v2.SynergyUtils;

import junit.framework.TestCase;

import java.util.ArrayList;

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

        assertEquals(testText1, slf.get(0).getText());
        assertEquals(testText2, slf.get(1).getText());
        assertEquals(testText3, slf.get(2).getText());
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

        assertEquals(testText1, slf.get(0).getText());
        assertEquals(testText2, slf.get(1).getText());
        assertEquals(testText3, slf.get(2).getText());

        ArrayList<SynergyListItem> splitItems = new ArrayList<>();
        SynergyListItem split1 = new SynergyListItem("test");
        SynergyListItem split2 = new SynergyListItem("item 2");
        splitItems.add(split1);
        splitItems.add(split2);

        slf.replace(1, splitItems);

        assertEquals(testText1, slf.get(0).getText());
        assertEquals("test", slf.get(1).getText());
        assertEquals("item 2", slf.get(2).getText());
        assertEquals(testText3, slf.get(3).getText());
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

        assertEquals(testText1, slf.get(0).getText());
        assertEquals(testText2, slf.get(1).getText());
        assertEquals(testText3, slf.get(2).getText());

        slf.save();

        slf = null;

        slf = new SynergyListFile(defaultListName);

        assertEquals(0, slf.size());

        slf.loadItems();

        assertEquals(3, slf.size());

        assertEquals(testText1, slf.get(0).getText());
        assertEquals(testText2, slf.get(1).getText());
        assertEquals(testText3, slf.get(2).getText());

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

        assertEquals(testText1, slf.get(0).getText());
        assertEquals(testText2, slf.get(1).getText());
        assertEquals(testText3, slf.get(2).getText());

        assertEquals("testVal1", slf.get(0).getVal("testKey"));
        assertTrue(Utils.stringIsNullOrWhitespace(slf.get(1).getVal("testKey")));
        assertEquals("testVal3", slf.get(2).getVal("testKey"));

        slf.move(1, 0);

        assertEquals(testText2, slf.get(0).getText());
        assertEquals(testText1, slf.get(1).getText());
        assertEquals(testText3, slf.get(2).getText());

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

        String dailyToDoName = SynergyUtils.getTimeStampedListName("DailyToDo");

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

        slf.queueToDailyToDo(1);
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

    }

    public void testGetItems() throws Exception {

    }

    public void testArchiveOne() throws Exception {

    }
}