/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import Objects.Command;
import Objects.CommandMetaData;
import Objects.SimpleSettings;
import Utils.TextUtils;
import Wheatley.Global;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 */
public class ShortCMD implements Command {
    private static SimpleSettings shortCommands = new SimpleSettings("shortCommands.json");
    String historyFilename = "cmdHist.txt";
    
    @Override
    public String toString() {
        return("Some text about the command that will never be used");
    }
    
    @Override
    public boolean isCommand(String toCheck) {
//        return toCheck.equalsIgnoreCase(Global.mainNick+", phrase command check"); // Phrase that when spoken will activate the command
        return false;
    }
    
    @Override
    public ArrayList<String> commandTerms() {
        ArrayList<String> a = shortCommands.getKeyList();
        a.add("mkcmd");
        a.add("rmcmd");
        a.add("upcmd");
//        a.add("command"); // Terms that when prefixed by the command prefix, will activate the command
// NOTE: these should be all lowercase
        return a;
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        ArrayList<String> commands = shortCommands.getKeyList();
        for (int i = 0; i < commands.size(); i++) {
            a.add(Colors.BOLD + Global.commandPrefix + commands.get(i) + Colors.NORMAL + ": " + shortCommands.getString(commands.get(i)));
        }
//        a.add(Colors.BOLD + Global.commandPrefix + "bankick [user] [time] [reason]" + Colors.NORMAL + ": Bans a user’s nickname only (not hostmask) for a period of time and then kicks them from the channel. ");
        return a;
    }
    
    
    @Override
    public void processCommand(Event event) {
        CommandMetaData data = new CommandMetaData(event, true);
        String caller = data.getCaller(); // Nick of the user who called the command
//        boolean isVerified = data.isVerifiedBotOwner(); // True if the user who called the command is the bot owner
//        String[] cmdSplit = data.getCommandSplit();
        String cmd = data.getCommand();
        String respondTo = data.respondToIgnoreMessage();
        
        if(cmd.equalsIgnoreCase("mkcmd") && data.isVerifiedBotOwner()) {

            ArrayList<String> keyList = shortCommands.getKeyList();
            if (keyList.contains(cmd.toLowerCase())) {
                event.getBot().sendIRC().message(respondTo, "Command Already Exists");
            }
            else {
                String fullText = data.getMessage();
                String newCMD = fullText.split(" ")[1];
                String text = fullText.split(" ", 3)[2];
                shortCommands.create(newCMD, text);
                shortCommands.save();
                event.getBot().sendIRC().notice(caller, "Command Successfully Added");
            }
        }
        else if (cmd.equalsIgnoreCase("rmcmd")  && data.isVerifiedBotOwner()) {

            ArrayList<String> keyList = shortCommands.getKeyList();
            String fullText = data.getMessage();
            String delCMD = fullText.split(" ")[1];
            
            if (keyList.contains(delCMD.toLowerCase())) {
                String text;
                text = shortCommands.getString(delCMD);
                TextUtils.addToDoc(historyFilename, delCMD + ": " + text);
                shortCommands.removeKey(delCMD);
                shortCommands.save();
                event.getBot().sendIRC().notice(caller, "Command Successfully Removed");
            }
            else {
                event.getBot().sendIRC().message(respondTo, "Command Does Not Exists");
            }

        }
        else if (cmd.equalsIgnoreCase("upcmd") && data.isVerifiedBotOwner()) {
            ArrayList<String> keyList = shortCommands.getKeyList();
            String fullText = data.getMessage();
            String delCMD = fullText.split(" ")[1];
            if (keyList.contains(delCMD.toLowerCase())) {
                String text = fullText.split(" ", 3)[2];
                TextUtils.addToDoc(historyFilename, delCMD + ": " + shortCommands.getString(delCMD));
                shortCommands.setString(delCMD, text);
                shortCommands.save();
                event.getBot().sendIRC().notice(caller, "Command Successfully Updated");
            }
            else {
                event.getBot().sendIRC().message(respondTo, "Command Does Not Exists");
            }

        }
        else if (shortCommands.contains(cmd)) {
            event.getBot().sendIRC().message(respondTo, shortCommands.getString(cmd));
        }
    }
}
