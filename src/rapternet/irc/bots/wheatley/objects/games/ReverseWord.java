/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapternet.irc.bots.wheatley.objects.games;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stephen
 */
public class ReverseWord implements WordGame {

    public String modify(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        for (int i = characters.size(); i > 0; i--) {
            output.append(characters.get(i - 1));
        }
        return (output.toString());
    }

}
