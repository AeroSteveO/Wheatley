/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapternet.irc.bots.wheatley.objects.games;

/**
 *
 * @author Stephen
 */
public interface WordGame {

    /**
     * Modifies the input word to create a puzzle, for example, reversed, shuffled, etc
     * @param wordToModify the word to modify
     * @return the modified word for the game
     */
    public String modify(String wordToModify);
}
