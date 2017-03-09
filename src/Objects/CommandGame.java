/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.ArrayList;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 *
 */
public interface CommandGame  {
    
    /**
     *
     * @return boolean true if the object implementing the interface is a game
     * and false if it is a general command
     * 
     * -Games would include stuff like hangman
     * -Non-games would include stuff like money, cheat, and give
     */
    boolean isGame();
    
    /**
     *
     * @return boolean true if the game is a multi-line, "Long" game, and false
     * if the game is a single line "Short" game
     * 
     * -Single line games are those that only require the bot to output a single
     *  line (Think lotto, omgword, and bomb)
     * -Long games may require constant interaction between the user and the bot
     *  (Think hangman, and mastermind)
     */
    boolean isShortGame();
    
    /**
     *
     * @param event the event that triggered the game/command to be run, allows
     * the command to use the full data from the event.
     */
    public void processCommand(MessageEvent event);
    
    /**
     *
     * @return ArrayList String of terms that will activate the command
     */
    public ArrayList<String> commandTerms();
    
    /**
     *
     * @param toCheck
     * @return boolean true if the string is an activating command, false if the
     * string is not to activate the game/command
     */
    public boolean isCommand(String toCheck);
    
}
