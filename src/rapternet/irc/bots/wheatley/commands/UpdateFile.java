/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.commands;

import rapternet.irc.bots.common.objects.Command;
import rapternet.irc.bots.common.objects.CommandMetaData;
import rapternet.irc.bots.common.utils.TextUtils;
import rapternet.irc.bots.wheatley.listeners.Global;
import java.util.ArrayList;
import org.pircbotx.Colors;
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
 *    TextUtils
 * - Linked Classes
 *    N/A
 * 
 * Activate commands with:
 *      !update [filename] [singleWordItem]
 *          Update a text file by adding a new line to the end of it
 *          EXAMPLE: !update badwords defenetely
 * 
 */
public class UpdateFile implements Command{
    
    @Override
    public String toString(){
        return("Simple text file updater");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("update");
        return a;
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        a.add(Colors.BOLD + Global.commandPrefix + "update [filename] [addition]" + Colors.NORMAL + ": Adds the word to the given text file, currently for the bad word list");
        return a;
    }

    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData data = new CommandMetaData(event,true);
        
        String caller = data.getCaller();
//        String channel = data.getEventChannel();
//        String responseChan = data.getEventChannel();
        boolean isVerified = data.isVerifiedBotOwner();
        String[] cmdSplit = data.getCommandSplit();
        
        if(isVerified){
            
//                    String[] properties = message.split(" ");
            if (cmdSplit.length== 3){
                String filename = cmdSplit[1];
                String addition = cmdSplit[2];
                try{
                    boolean success = TextUtils.addToDocIfUnique(filename, addition);
                    if (success)
                        event.getBot().sendIRC().message(data.respondToIgnoreMessage(),Colors.GREEN+"Success: "+Colors.NORMAL+addition+" was added to "+ filename);
                    else
                        event.getBot().sendIRC().message(data.respondToIgnoreMessage(),Colors.RED+"Failure: "+Colors.NORMAL+addition+" is already in "+ filename);
                    
                }catch(Exception e){
                    e.printStackTrace();
                    event.getBot().sendIRC().notice(caller,Colors.RED+"FAILURE: "+Colors.NORMAL+addition+" was NOT added to "+ filename);
                }
            }
            else{
                event.getBot().sendIRC().notice(caller, "Update reqires 2 inputs: the filename and the single word addition");
            }
        }
        else{
            event.getBot().sendIRC().notice(caller, "You do not have access to this command");
        }
    }
}
