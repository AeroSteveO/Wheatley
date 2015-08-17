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
 *      !help
 *          Responds with a list of every command utilized in the command listener
 *          package
 *      !help modules
 *          Responds with a list of loaded command modules
 * 
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
        a.add("help"); 
        return a;
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        a.add(Colors.BOLD + Global.commandPrefix + "help" + Colors.NORMAL + ":  Provides a list of commands and packages that support the help function"); 
        a.add(Colors.BOLD + Global.commandPrefix + "help modules" + Colors.NORMAL + ":  Provides a list of currently loaded packages that support the help function");
        a.add(Colors.BOLD + Global.commandPrefix + "help [command]" + Colors.NORMAL + ":  Provides help regarding the input command");
        a.add(Colors.BOLD + Global.commandPrefix + "help [module]" + Colors.NORMAL + ":  Provides help regarding the input module");
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
            else {
                String command = cmdSplit[1];
                if (command.startsWith(Global.commandPrefix)) {
                    command = command.substring(1);
                }
                
                for (int i = 0; i < commands.size(); i++) {
                    if (commands.get(i).commandTerms().contains(command.toLowerCase()) || commands.get(i).getClass().getName().split("\\.")[1].equalsIgnoreCase(command)) {
                        ArrayList<String> helpText = commands.get(i).help(command);
                        for (int j = 0; j < helpText.size(); j++) {
                            event.getBot().sendIRC().message(caller, helpText.get(j));
                        }
                        return;
                    }
                }
                
                System.out.println(command);
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
