package rapternet.irc.bots.wheatley.objects.games;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReverseWordTest {
    @Test
    public void testReverseWord() {
        WordGame modifier = new ReverseWord();
        assertEquals("drow", modifier.modify("word"));
        assertEquals("gnittuc", modifier.modify("cutting"));
        assertEquals("elbat", modifier.modify("table"));
        assertEquals("gnilenap", modifier.modify("paneling"));
    }
}
