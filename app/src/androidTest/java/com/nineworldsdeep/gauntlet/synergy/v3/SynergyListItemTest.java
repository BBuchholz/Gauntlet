package com.nineworldsdeep.gauntlet.synergy.v3;

import junit.framework.TestCase;

/**
 * Created by brent on 1/21/16.
 */
public class SynergyListItemTest extends TestCase {

    SynergyListItem sli;
    String defaultItemTextV2 = "Android Testing Some Item Text";
    String defaultItemTextV3 = "item={Android Testing Some Item Text}";

    public void setUp() throws Exception {
        super.setUp();

        sli = null;
    }

    private void initV2(){
        sli = new SynergyListItem(defaultItemTextV2);
    }

    private void initV3(){
        sli = new SynergyListItem(defaultItemTextV3);
    }

    public void testListItemIsCompletedV2AndV3() throws Exception {

        initV2();

        assertFalse(sli.isCompleted());

        sli.markCompleted();

        assertTrue(sli.isCompleted());

        initV3();

        assertFalse(sli.isCompleted());

        sli.markCompleted();

        assertTrue(sli.isCompleted());
    }

    public void testGetText() throws Exception {

        initV2();
        assertEquals(defaultItemTextV2, sli.getItem());

        initV3();
        //should have same value
        assertEquals(defaultItemTextV2, sli.getItem());

        //should not be defaultV3 text, which is in keyVal notation
        assertFalse(defaultItemTextV3.equalsIgnoreCase(sli.getItem()));
    }

    public void testEquals() throws Exception {

        sli = new SynergyListItem("Test Item Duplicates");
        SynergyListItem sli2 = new SynergyListItem("Test Item 2");
        SynergyListItem sli3 = new SynergyListItem("Test Item Duplicates");

        assertTrue(sli.equals(sli3));
        assertFalse(sli.equals(sli2));
    }

    public void testTrimCategory() throws Exception {

        sli = new SynergyListItem("::Testing:: - testing trim category");

        assertEquals("::Testing:: - testing trim category", sli.getItem());
        assertEquals("Testing", sli.trimCategory());
        assertEquals("testing trim category", sli.getItem());
    }
}