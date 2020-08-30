package rapternet.irc.bots.common.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IRCUtilsTest {
    @Test
    public void testArrayListToString() {
        List<String> textToConvert = new ArrayList<>();
        textToConvert.add("a");
        assertEquals("a", IRCUtils.arrayListToString(textToConvert));
        textToConvert.add("b");
        assertEquals("a, b", IRCUtils.arrayListToString(textToConvert));
        textToConvert.add("c");
        assertEquals("a, b, c", IRCUtils.arrayListToString(textToConvert));
    }
    @Test
    public void testArrayListToStringEmpty() {
        List<String> textToConvert = new ArrayList<>();
        assertEquals("", IRCUtils.arrayListToString(textToConvert));
        assertEquals("", IRCUtils.arrayListToString(null));
    }
}
