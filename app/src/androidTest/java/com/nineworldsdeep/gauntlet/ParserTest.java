package com.nineworldsdeep.gauntlet;

import junit.framework.TestCase;

import java.util.HashMap;

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

    public void testTrimKeyVal() throws Exception {

        String valid = "tags={testing, test} item={some text}";

        valid = p.trimKeyVal("tags", valid);

        assertEquals("item={some text}", valid);

        valid = p.trimKeyVal("item", valid);

        assertEquals("", valid);

    }

    public void testGetFirstKey() throws Exception {

        String valid = "tags={testing, test} item={some text}";

        assertEquals("tags", p.getFirstKey(valid));

    }

    public void testFragmentToHashMap() throws Exception{

        String invalid = "fragment={ partialTag={some text}";
        String valid = "tags={testing, test} item={some text}";

        HashMap<String, String> expectedResult =
                new HashMap<>();

        //invalid should return an empty hashmap
        assertEquals(expectedResult, p.fragmentToHashMap(invalid));

        expectedResult.put("tags", "testing, test");
        expectedResult.put("item", "some text");

        //should be the same
        assertEquals(expectedResult, p.fragmentToHashMap(valid));
    }

    public void testHashMapToFragment() throws Exception{

        String v1 = "tags={testing, test} item={some text}";
        String v2 = "item={some text} tags={testing, test}";

        HashMap<String, String> input =
                new HashMap<>();

        input.put("tags", "testing, test");
        input.put("item", "some text");

        HashMap<String, String> input2 =
                new HashMap<>();

        input2.put("tags", "testing, test");
        input2.put("item", "some text");

        //have to verify either order, isn't guaranteed
        String input1Frag = p.hashMapToFragment(input);
        String input2Frag = p.hashMapToFragment(input2);

        assertTrue(input1Frag.equalsIgnoreCase(v1) ||
                    input1Frag.equalsIgnoreCase(v2));

        assertTrue(input2Frag.equalsIgnoreCase(v1) ||
                input2Frag.equalsIgnoreCase(v2));

    }
}