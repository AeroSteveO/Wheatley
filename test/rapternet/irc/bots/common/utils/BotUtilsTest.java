package rapternet.irc.bots.common.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BotUtilsTest {

    @Test
    public void testGetClassName() {
        assertEquals("BotUtilsTest", BotUtils.getClassName(this));
    }

}
