package com.nineworldsdeep.gauntlet;

import junit.framework.TestCase;

/**
 * Created by brent on 1/26/16.
 */
public class ParserTest extends TestCase {

    private Parser p;

    public void setUp() throws Exception {
        super.setUp();

        p = new Parser();
    }

    public void testSetFirst() throws Exception {

        String item = "item={some test text} createdAt={20160126191500}";
        String item2 = "item={some more testing} createdAt={20160126191500}";

        assertEquals("some test text", p.extract("item", item));
        assertEquals("some more testing", p.extract("item", item2));

        item2 = p.setFirst("item", "some test text", item2);
        assertEquals("some test text", p.extract("item", item2));

    }
}