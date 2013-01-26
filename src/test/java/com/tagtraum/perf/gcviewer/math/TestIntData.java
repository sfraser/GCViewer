package com.tagtraum.perf.gcviewer.math;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * Date: Jan 30, 2002
 * Time: 5:53:55 PM
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 * @version $Id: $
 */
public class TestIntData extends TestCase {

    public TestIntData(String name) {
        super(name);
    }

    public void testSimpleAverage() throws Exception {
        LongData longData = new LongData();
        longData.add(1);
        longData.add(2);
        assertEquals("Simple average", 1.5, longData.average(), 0.0);
    }

    public void testSimpleStandardDeviation() throws Exception {
        LongData longData = new LongData();
        longData.add(1);
        longData.add(1);
        longData.add(-1);
        longData.add(-1);
        assertEquals("Simple std deviation", 1.1547005383792515, longData.standardDeviation(), 0.0000001);
    }

    public static TestSuite suite() {
        return new TestSuite(TestIntData.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
