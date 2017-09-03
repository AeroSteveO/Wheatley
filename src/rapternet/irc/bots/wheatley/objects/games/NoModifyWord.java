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
public class NoModifyWord implements WordGame{
    
    @Override
    public String modify(String word) {
        return word;
    }
}
