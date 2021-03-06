/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.commands;

import rapternet.irc.bots.common.objects.Command;
import rapternet.irc.bots.common.objects.CommandMetaData;
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
public class ExampleCMD implements Command {
    
    @Override
    public String toString() {
        return("Some text about the command that will never be used");
    }
    
    @Override
    public boolean isCommand(String toCheck) {
        return toCheck.equalsIgnoreCase(Global.mainNick+", phrase command check"); // Phrase that when spoken will activate the command
    }
    
    @Override
    public ArrayList<String> commandTerms() {
        ArrayList<String> a = new ArrayList<>();
        a.add("command"); // Terms that when prefixed by the command prefix, will activate the command
// NOTE: these should be all lowercase
        return a;
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        a.add(Colors.BOLD + Global.commandPrefix + "bankick [user] [time] [reason]" + Colors.NORMAL + ": Bans a user’s nickname only (not hostmask) for a period of time and then kicks them from the channel. ");
        return a;
    }
    
    
    @Override
    public void processCommand(Event event) {
        
        CommandMetaData data = new CommandMetaData(event, true);
        
        String caller = data.getCaller(); // Nick of the user who called the command
        boolean isVerified = data.isVerifiedBotOwner(); // True if the user who called the command is the bot owner
        String[] cmdSplit = data.getCommandSplit();
        
        
        
        
    }
}
