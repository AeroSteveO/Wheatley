package rapternet.irc.bots.wheatley.objects.games;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ShuffleWordTest {
    @Test
    public void testShuffleWord() {
        WordGame modifier = new ShuffleWord();
        assertTrue(containsLetters("asdf", modifier.modify("asdf")));
        assertTrue(containsLetters("cuttingboard", modifier.modify("cuttingboard")));
        assertTrue(containsLetters("helicopter", modifier.modify("helicopter")));
    }

    // ensures string a contains all the letters contained within string b
    private boolean containsLetters(String a, String b) {
        System.out.println(b + " == " + a);
        for (int i = 0; i < a.length(); i++) {

            for (int j = 0; j < b.length(); j++) {
                if (b.charAt(j) == a.charAt(i)) {
                    b = buildString(j, b.toCharArray());
                    System.out.println(b);
                }
            }
        }

        if (b.equals("")) {
            return true;
        }
        return false;
    }
    private String buildString(int indexToRemove, char[] array) {
        String newStr = new String();
        for (int i = 0; i < array.length; i++) {
            if (i != indexToRemove) {
                newStr += array[i];
            }
        }
        return newStr;
    }
}
