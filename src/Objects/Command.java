/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Objects;

import java.util.ArrayList;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 */
public interface Command {
    
    public void processCommand(Event event);
    public ArrayList<String> commandTerms();
    public boolean isCommand(String toCheck);
    
}
