package rapternet.irc.bots.wheatley.objects.games;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BlankWordTest {
    @Test
    public void testBlankWord() {
        WordGame modifier = new BlankWord();
        assertEquals("_", modifier.modify("a"));
        assertEquals("____", modifier.modify("word"));
        assertEquals("____________", modifier.modify("cuttingboard"));
        assertEquals("___________", modifier.modify("woodenpanel"));
    }
}
