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
public class BlankWord implements WordGame {

    public String modify(String input) {
        String blanks = new String();
        for (int i = 0; i < input.length(); i++) {
            blanks = blanks + "_";
        }
        return (blanks);
    }

}
