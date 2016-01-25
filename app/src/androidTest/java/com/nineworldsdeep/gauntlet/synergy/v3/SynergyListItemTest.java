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

        assertFalse(sli.listItemIsCompleted());

        sli.markCompleted();

        assertTrue(sli.listItemIsCompleted());

        initV3();

        assertFalse(sli.listItemIsCompleted());

        sli.markCompleted();

        assertTrue(sli.listItemIsCompleted());
    }

    public void testGetText() throws Exception {

        initV2();
        assertEquals(defaultItemTextV2, sli.getText());

        initV3();
        //should have same value
        assertEquals(defaultItemTextV2, sli.getText());

        //should not be defaultV3 text, which is in keyVal notation
        assertFalse(defaultItemTextV3.equalsIgnoreCase(sli.getText()));
    }
}