package com.nineworldsdeep.gauntlet.suites;

import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListFileTest;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyListItemTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by brent on 1/28/16.
 */
public class SynergyTestSuite extends TestCase {

    public SynergyTestSuite(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("JUnit3TestSuite");
        suite.addTest(new TestSuite(SynergyListItemTest.class));
        suite.addTest(new TestSuite(SynergyListFileTest.class));
        return suite;
    }
}
