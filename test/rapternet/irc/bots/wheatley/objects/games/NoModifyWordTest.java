package rapternet.irc.bots.wheatley.objects.games;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NoModifyWordTest {

    @Test
    public void testNoModifyWord() {
        WordGame game = new NoModifyWord();
        String out = game.modify("word");
        assertEquals("word", out);
    }
    @Test
    public void testNoModifyWord2() {
        WordGame game = new NoModifyWord();
        String out = game.modify("heron");
        assertEquals("heron", out);
    }
    @Test
    public void testNoModifyWord3() {
        WordGame game = new NoModifyWord();
        String out = game.modify("strangecompoundword");
        assertEquals("strangecompoundword", out);
    }
}
