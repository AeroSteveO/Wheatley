package rapternet.irc.bots.wheatley.objects;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlayingCardTest {
    @Test
    public void testToMaskedString() {
        PlayingCard pc = new PlayingCard(1,1);
        assertEquals("**", pc.toMaskedString());
        pc = new PlayingCard(10,0);
        assertEquals("**", pc.toMaskedString());
        pc = new PlayingCard(5,3);
        assertEquals("**", pc.toMaskedString());
    }

    @Test
    public void testCompareNoSuit() {
        PlayingCard pc = new PlayingCard(1,4);
        PlayingCard pc2 = new PlayingCard(5,1);
        assertEquals(-1, pc.compareToIgnoreSuit(pc2));
        assertEquals(1, pc2.compareToIgnoreSuit(pc));

        assertEquals(0, pc2.compareToIgnoreSuit(pc2));

        pc = new PlayingCard(5, 2);
        assertEquals(0, pc2.compareToIgnoreSuit(pc));
    }
}
