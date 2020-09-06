package rapternet.irc.bots.wheatley.objects;

import org.junit.Test;
import rapternet.irc.bots.uno2.Deck;

import static org.junit.Assert.*;

public class DeckOfCardsTest {

    @Test
    public void testConstruction() {
        DeckOfCards dc = new DeckOfCards();
        assertEquals(52, dc.cardsLeft());
        assertFalse(dc.hasJokers());
    }

    @Test
    public void testDeal() {
        DeckOfCards dc = new DeckOfCards();
        PlayingCard pc = dc.dealCard();

        assertEquals(51, dc.cardsLeft());

        assertTrue(pc.getValue() <= 13);
        assertTrue(pc.getValue() > 0);

        assertTrue(pc.getSuit() >0);
        assertTrue(pc.getSuit() <= 4);
    }
}
