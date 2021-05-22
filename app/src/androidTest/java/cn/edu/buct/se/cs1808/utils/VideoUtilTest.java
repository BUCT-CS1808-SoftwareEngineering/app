package cn.edu.buct.se.cs1808.utils;

import junit.framework.TestCase;

public class VideoUtilTest extends TestCase {

    public void testSetVideoDuration() {
        assertEquals("00:03:00", VideoUtil.durationSecToString(180));
        assertEquals("00:03:10", VideoUtil.durationSecToString(190));
        assertEquals("01:03:10", VideoUtil.durationSecToString(3790));
    }
}
