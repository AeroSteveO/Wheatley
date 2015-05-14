/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Objects.Command;
import Objects.CommandMetaData;
import Wheatley.Global;
import java.util.ArrayList;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    Command
 *    CommandMetaData
 * - Utilities
 *    N/A
 * - Linked Classes
 *    N/A
 *
 * Activate Command with:
 *      !Command
 *      Wheatley, command
 *          Command description here
 *
 */
public class ExampleCMD implements Command{
    @Override
    public String toString(){
        return("Some text about the command that will never be used");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return toCheck.equalsIgnoreCase(Global.mainNick+", phrase command check");
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add(null);
        return a;
    }
    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData data = new CommandMetaData(event,true);
        
        String caller = data.getCaller();
        boolean isVerified = data.isVerifiedBotOwner();
        String[] cmdSplit = data.getCommandSplit();
        
        
        
        
    }
}
