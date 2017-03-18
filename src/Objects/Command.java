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
 * 
 * Requirements:<br>
 * - APIs<br>
 *    N/A<br>
 * - Custom Objects<br>
 *    N/A<br>
 * - Linked Classes<br>
 *    N/A<br>
 * 
 */
public interface Command {
    
  /**
   * Processes the command in the input Event.
   * The Event must already have been screened to contain the command.
   * @param event Event to process
   */
  public void processCommand(Event event);
    
  /**
   * Command terms this object will accept as input.
   * @return Array of Strings containing the terms that will trigger this
   *         command
   */
  public ArrayList<String> commandTerms();
    
  /**
   * Determines if the input command string can be processed with this object.
   * @param toCheck Command string to check
   * @return TRUE if this object can process the command, FALSE otherwise.
   */
  public boolean isCommand(String toCheck);
    
  /**
   * Returns an arraylist of help data for the input command. Each element
   * in the list will be output to the user on a new line.
   * @param command Command to get the help information for.
   * @return List of help string lines.
   */
  public ArrayList<String> help(String command);
    
}
