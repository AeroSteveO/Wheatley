package rapternet.irc.bots.common.objects;

import org.junit.Test;
import rapternet.irc.bots.wheatley.objects.GameList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameListTest {
    @Test
    public void testContains() {
        GameList list = new GameList();
        list.add("#test", "coolGame");
        assertTrue(list.contains("#test", "coolGame"));

        list.remove("#test", "coolGame");
        assertFalse(list.contains("#test", "coolGame"));
    }

    @Test
    public void testContains2() {
        GameList list = new GameList();
        list.add(new String[]{"#test", "coolGame"});
        assertTrue(list.contains("#test", "coolGame"));

        list.remove("#test", "coolGame");
        assertFalse(list.contains("#test", "coolGame"));

        list.add(new String[]{"#games", "newGame", "short"});
        assertTrue(list.contains("#games", "newGame"));

        list.remove("#games", "newGame");
        assertFalse(list.contains("#games", "newGame"));
    }

    @Test
    public void testIsGameActive() {
        GameList list = new GameList();
        list.add(new String[]{"#test", "coolGame"});
        assertTrue(list.isGameActive("#test","coolGame"));

        list.remove("#test", "coolGame");
        assertFalse(list.isGameActive("#test","coolGame"));

        list.add(new String[]{"#games", "newGame", "short"});
        assertTrue(list.isGameActive("#games","newGame"));

        list.remove("#games", "newGame");
        assertFalse(list.isGameActive("#games","newGame"));
    }
    @Test
    public void testIsGameActive2() {
        GameList list = new GameList();

        list.add(new String[]{"#games", "newGame", "short"});
        assertTrue(list.isGameActive("#games","newGame", "short"));

        list.remove("#games", "newGame");
        assertFalse(list.isGameActive("#games","newGame", "short"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullChannelInput() {
        GameList list = new GameList();

        list.add(null, "game");
    }
    @Test(expected = NullPointerException.class)
    public void testNullGameInput() {
        GameList list = new GameList();

        list.add("#channel", null);
    }

    @Test(expected = NullPointerException.class)
    public void testOnlyGameInput2() {
        GameList list = new GameList();

        list.add(new String[]{"game"});
    }
    @Test(expected = NullPointerException.class)
    public void testNoInputToAdd() {
        GameList list = new GameList();

        list.add(new String[]{});
    }
    @Test(expected = NullPointerException.class)
    public void testNullInputToAdd() {
        GameList list = new GameList();

        list.add(null);
    }
}
