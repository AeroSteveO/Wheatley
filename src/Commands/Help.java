/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Commands;

import Objects.Command;
import Objects.CommandMetaData;
import Wheatley.CommandListener;
import Wheatley.Global;
import java.util.ArrayList;
import java.util.List;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 */
public class Help implements Command {
    
    @Override
    public String toString() {
        return("Help: Brings information about all the available commands");
    }
    
    @Override
    public boolean isCommand(String toCheck) {
        return toCheck.equalsIgnoreCase(Global.mainNick+", help"); // Phrase that when spoken will activate the command
    }
    
    @Override
    public ArrayList<String> commandTerms() {
        ArrayList<String> a = new ArrayList<>();
        a.add("help"); // Terms that when prefixed by the command prefix, will activate the command
// NOTE: these should be all lowercase
        return a;
    }
    
    @Override
    public void processCommand(Event event) {
        
        CommandMetaData data = new CommandMetaData(event, false);
        
        String caller = data.getCaller(); // Nick of the user who called the command
        String[] cmdSplit = data.getCommandSplit();
        
        List<Command> commands = CommandListener.getCommandsAvailable();
        
        if (cmdSplit.length == 1) {
            String commandsAvailable = Colors.RED + "Commands supporting help (also support use by PM): " + Colors.NORMAL;
            for (int i = 0; i < commands.size(); i++) {
                ArrayList<String> internalCommands = commands.get(i).commandTerms();
                if (internalCommands == null || internalCommands.isEmpty()) {
                    commandsAvailable += commands.get(i).getClass().getName().split("\\.")[1] + ", ";
                }
                else {
                    for (int j = 0; j < internalCommands.size(); j++) {
                        if (internalCommands.get(j) == null) {
                            commandsAvailable += commands.get(i).getClass().getName().split("\\.")[1] + ", ";
                        }
                        else {
                            commandsAvailable += internalCommands.get(j) + ", ";
                        }
                    }
                }
            }
            event.getBot().sendIRC().message(caller, commandsAvailable);
        }
        else if (cmdSplit.length == 2) {
            
            if (cmdSplit[1].equalsIgnoreCase("modules")) {
                String modulesAvailable = Colors.RED + "Currently Enabled Modules: " + Colors.NORMAL;
                for (int i = 0; i < commands.size(); i++) {
                    modulesAvailable += commands.get(i).getClass().getName().split("\\.")[1] + ", ";
                }
                event.getBot().sendIRC().message(caller, modulesAvailable);
            }
        }
        
        
    }
    public ArrayList<String> getCommandTerms(List<Command> commands) {
        
        ArrayList<String> commandTerms = new ArrayList<>();
        
        for (int i = 0; i < commands.size(); i++) {
                ArrayList<String> internalCommands = commands.get(i).commandTerms();
                if (internalCommands == null || internalCommands.isEmpty()) {
                     commandTerms.add(commands.get(i).getClass().getName().split("\\.")[1].toLowerCase());
                }
                else {
                    for (int j = 0; j < internalCommands.size(); j++) {
                        if (internalCommands.get(j) == null) {
                            commandTerms.add(commands.get(i).getClass().getName().split("\\.")[1].toLowerCase());
                        }
                        else {
                            commandTerms.add(internalCommands.get(j).toLowerCase());
                        }
                    }
                }
            }
        return commandTerms;
    }
}
