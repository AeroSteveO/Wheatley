/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapternet.irc.bots.wheatley.objects;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 *
 * @author Stephen
 */
public class ScoreTest {
    
    
    @Test
    public void testConstruction() {
        Score user = new Score("User");
        assertEquals(user.getScore(), 0);
        assertEquals(user.getUser(), "User");
    }

    @Test
    public void testArrayConstruction() {
        Score.ScoreArray array = new Score.ScoreArray();
        assertEquals(0, array.getList().size());
        assertEquals(0, array.size());
        assertEquals(0, array.toArrayList().size());
    }

    @Test
    public void testAddScore() {
        Score.ScoreArray array = new Score.ScoreArray();
        Score user = new Score("User");
        array.add(user);

        assertEquals(user.getScore(), 0);

        Score score2 = new Score("User2");
        array.add(score2);
        array.addScore("User2", 10);

        assertTrue(user.compareTo(score2) > 0);
        assertTrue(score2.compareTo(user) < 0);

        array.addScore("User", 10);
        assertTrue(score2.compareTo(user) == 0);
    }

    @Test
    public void testContains() {
        Score.ScoreArray array = new Score.ScoreArray();
        Score user = new Score("User");
        array.add(user);

        assertTrue(array.containsUser("User"));
        assertTrue(array.containsUser("user"));

        Score score2 = new Score("User2");
        array.add(score2);

        assertTrue(array.containsUser("User2"));
        assertTrue(array.containsUser("user2"));
    }

    @Test
    public void testSubtractScore() {
        Score.ScoreArray array = new Score.ScoreArray();
        Score user = new Score("User");
        array.add(user);

        assertEquals(user.getScore(), 0);

        Score score2 = new Score("User2");
        array.add(score2);
        array.subtractScore("User2", -10);

        assertTrue(user.compareTo(score2) > 0);
        assertTrue(score2.compareTo(user) < 0);

        array.subtractScore("User", -10);
        assertTrue(score2.compareTo(user) == 0);
    }
}
