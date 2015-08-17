/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Objects.Kicks.SmashKick;
import Objects.Kicks.HackKick;
import Objects.Command;
import Objects.CommandMetaData;
import Objects.CommandMetaData.EventType;
import Objects.Kicks.CustomKick;
import Objects.Kicks.KickInterface;
import Utils.BotUtils;
import Utils.IRCUtils;
import Wheatley.Global;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 *
 * The most BA package of kicks you'll ever see
 */
public class KickCMD implements Command {
    static ArrayList<KickInterface> kicks = loadKicks();
    static ArrayList<KickInterface> unloadedKicks = new ArrayList<>();
    
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
        a.add("addkick");
        a.add("delkick");
        a.add("enablekick");
        a.add("disablekick");
        a.add("listkicks");
        a.addAll(getKickCommands());
        return a;
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        if (command.equalsIgnoreCase("addkick") || command.equalsIgnoreCase(BotUtils.getClassName(this))) {
            a.add(Colors.BOLD + Global.commandPrefix + "addkick [kick command] [kick message]" + Colors.NORMAL + ": Adds a new kick command using the input message as the kick message" );
            a.add(Colors.BOLD + Global.commandPrefix + "addkick -c [kick command] -m [kick message] -u [space delimited list of allowed users] (-b | -w) [space delimited list of blacklisted or whitelisted channels] -f [failure message]" + Colors.NORMAL + ": Adds a new kick command using the input input settings for the kick, and -c, -m, & -f are the only required inputs" );
        }
        if (command.equalsIgnoreCase("delkick") || command.equalsIgnoreCase(BotUtils.getClassName(this)))
            a.add(Colors.BOLD + Global.commandPrefix + "delkick [kick]" + Colors.NORMAL + ": Completely deletes the input kick" );
        
        if (command.equalsIgnoreCase("enablekick") || command.equalsIgnoreCase(BotUtils.getClassName(this)))
            a.add(Colors.BOLD + Global.commandPrefix + "enablekick [kick]" + Colors.NORMAL + ": Enables the input kick so that it can be used" );
        
        if (command.equalsIgnoreCase("disablekick") || command.equalsIgnoreCase(BotUtils.getClassName(this)))
            a.add(Colors.BOLD + Global.commandPrefix + "disablekick [kick]" + Colors.NORMAL + ": Disables the input kick so that it cannot be used until its enabled" );
        
        if (command.equalsIgnoreCase("listkicks") || command.equalsIgnoreCase(BotUtils.getClassName(this)))
            a.add(Colors.BOLD + Global.commandPrefix + "listkicks" + Colors.NORMAL + ": Responds with a list of all the currently enabled or disabled kicks" );
        
        if (commandTerms().contains(command.toLowerCase())) {
            a.add(kickHelpString(getKick(command.toLowerCase())));
        }
        else if (command.equalsIgnoreCase(BotUtils.getClassName(this))) {
            for (int i = 0; i < kicks.size(); i++) {
                a.add(kickHelpString(kicks.get(i)));
            }
            for (int i = 0; i < unloadedKicks.size(); i++) {
                a.add(kickHelpString(unloadedKicks.get(i)));
            }
        }
        
        
        return a;
    }
    
    
    @Override
    public void processCommand(Event event) {
        
        CommandMetaData data = new CommandMetaData(event, true);
        
        String caller = data.getCaller(); // Nick of the user who called the command
        boolean isVerified = data.isVerifiedBotOwner(); // True if the user who called the command is the bot owner
        String[] cmdSplit = data.getCommandSplit();
        String respondTo = data.respondToIgnoreMessage();
        String channel = data.getEventChannel();
        String message = data.getMessage();
        
        if (cmdSplit[0].equals("addkick")) {
            if (isVerified) {
                if (message.contains("-c") && message.contains("-f") && message.contains("-m")) { // && message.contains("-b") && message.contains("-u")
                    String[] kickArgs = message.split("(\\-c)|(\\-u)|(\\-b)|(\\-f)|(\\-m)|(\\-w)");
                    String kickCommand = null;
                    String kickFail = null;
                    String kickMessage = null;
                    ArrayList<String> whiteListedUsers = null;
                    ArrayList<String> blacklistedChannels = null;
                    for (int i = 0; i < kickArgs.length; i++) {
                        
                        if (message.split("-c")[1].trim().toLowerCase().startsWith(kickArgs[i].trim().toLowerCase()) && kickCommand == null) {
                            if (kickArgs[i].trim().split(" ").length != 1) {
                                event.getBot().sendIRC().notice(caller, "Kick: Command can only be one word");
                                return;
                            }
                            kickCommand = kickArgs[i].trim().toLowerCase();
                        }
                        else if (message.split("-f")[1].trim().toLowerCase().startsWith(kickArgs[i].trim().toLowerCase()) && kickFail == null) {
                            kickFail = kickArgs[i].trim();
                        }
                        else if (message.split("-m")[1].trim().toLowerCase().startsWith(kickArgs[i].trim().toLowerCase()) && kickMessage == null) {
                            kickMessage = kickArgs[i].trim();
                        }
                        else if ((message.contains("-b") && message.split("-b")[1].trim().toLowerCase().startsWith(kickArgs[i].trim().toLowerCase()) ||
                                message.contains("-w") && message.split("-w")[1].trim().toLowerCase().startsWith(kickArgs[i].trim().toLowerCase())) && blacklistedChannels == null) {
                            blacklistedChannels = new ArrayList(Arrays.asList(kickArgs[i].toLowerCase().trim().split(" ")));
                        }
                        else if (message.contains("-u") && message.split("-u")[1].trim().toLowerCase().startsWith(kickArgs[i].trim().toLowerCase()) && whiteListedUsers == null) {
                            whiteListedUsers = new ArrayList(Arrays.asList(kickArgs[i].toLowerCase().trim().split(" ")));
                        }
                    }
                    
                    for (int i = 0; i < kicks.size(); i++) {
                        if (kickCommand.equalsIgnoreCase(kicks.get(i).getCommand())) {
                            event.getBot().sendIRC().notice(caller, "Kick: A kick already exists for that command string");
                            return;
                        }
                    }
                    for (int i = 0; i < unloadedKicks.size(); i++) {
                        if (kickCommand.equalsIgnoreCase(unloadedKicks.get(i).getCommand())) {
                            event.getBot().sendIRC().notice(caller, "Kick: A kick already exists for that command string");
                            return;
                        }
                    }
                    
                    addKick(kickCommand, kickMessage, kickFail, whiteListedUsers, blacklistedChannels, (message.contains("-w")));
                    event.getBot().sendIRC().message(respondTo, "Kick: Success, " + kickCommand + " has been successfully added to the kick commands");
                }
                else if (message.contains("-c") || message.contains("-u") || message.contains("-f") || message.contains("-b") || message.contains("-m") || message.contains("-w")) {
                    event.getBot().sendIRC().notice(caller, "Kick: Full set of inputs not found in input string, minimum inputs include -c, -f, -m");
                }
                else if (cmdSplit.length >= 3) {
                    for (int i = 0; i < kicks.size(); i++) {
                        if (cmdSplit[1].equalsIgnoreCase(kicks.get(i).getCommand())) {
                            event.getBot().sendIRC().notice(caller, "Kick: A kick already exists for that command string");
                            return;
                        }
                    }
                    for (int i = 0; i < unloadedKicks.size(); i++) {
                        if (cmdSplit[1].equalsIgnoreCase(unloadedKicks.get(i).getCommand())) {
                            event.getBot().sendIRC().notice(caller, "Kick: A kick already exists for that command string");
                            return;
                        }
                    }
                    
                    
                    String kickMessage = data.getMessage().split(" ", 3)[2];
                    addKick(cmdSplit[1].toLowerCase(), kickMessage);
                    event.getBot().sendIRC().message(respondTo, "Kick: Success, " + cmdSplit[1] + " has been successfully added to the kick commands");
                }
            }
            return;
        }
        else if (cmdSplit[0].equals("delkick")) {
            if (isVerified) {
                if (cmdSplit.length == 2) {
                    if (delKick(cmdSplit[1])) {
                        event.getBot().sendIRC().message(respondTo, "Kick: " + cmdSplit[1] + " has been deleted");
                    }
                    else {
                        event.getBot().sendIRC().message(respondTo, "Kick: 404 not found");
                    }
                }
            }
            return;
        }
        else if (cmdSplit[0].equals("enablekick")) {
            if (isVerified) {
                if (cmdSplit.length == 2) {
                    if(loadKick(cmdSplit[1])) {
                        event.getBot().sendIRC().message(respondTo, "Kick: Successfully enabled");
                    }
                    else {
                        event.getBot().sendIRC().message(respondTo, "Kick: No disabled kick by that ID found");
                    }
                }
            }
            return;
        }
        else if (cmdSplit[0].equals("disablekick")) {
            if (isVerified) {
                if(unloadKick(cmdSplit[1])) {
                    event.getBot().sendIRC().message(respondTo, "Kick: Successfully disabled");
                }
                else {
                    event.getBot().sendIRC().message(respondTo, "Kick: No enabled kick by that ID found");
                }
            }
            return;
        }
        else if (cmdSplit[0].equals("listkicks")) {
            String kickList = Colors.BOLD + Colors.GREEN + "Enabled Kicks: " + Colors.NORMAL;
            for (int i = 0; i < kicks.size(); i++) {
                kickList += kicks.get(i).getCommand() + ", ";
            }
            kickList = kickList.substring(0, kickList.length()-2) + Colors.BOLD + Colors.RED + " Disabled Kicks: " + Colors.NORMAL;
            for (int i = 0; i < unloadedKicks.size(); i++) {
                kickList += unloadedKicks.get(i).getCommand() + ", ";
            }
            
            kickList = kickList.substring(0, kickList.length()-2);
            event.getBot().sendIRC().message(caller, kickList);
            return;
        }
        
        if (data.getEventType() == EventType.MessageEvent) {
            
            KickInterface kick = getKick(cmdSplit[0]);
            
            
            
            if (data.isVerifiedChanBotOwner()
                    || (kick.getAllowedUsers() != null && kick.getAllowedUsers().contains(caller.toLowerCase()) && data.isVerified())) {
                
                if (!data.isVerifiedChanBotOwner() && kick.getChannelList() != null) {
                    if (kick.isChannelListWhitelist() && !kick.getChannelList().contains(channel.toLowerCase())) {
                        event.getBot().sendIRC().notice(caller, "Kick: You are not allowed to use that function in this channel");
                        return;
                    }
                    else if (!kick.isChannelListWhitelist() && kick.getChannelList().contains(channel.toLowerCase())){
                        event.getBot().sendIRC().notice(caller, "Kick: You are not allowed to use that function in this channel");
                        return;
                    }
                }
                
                if (cmdSplit.length == 2) {
                    for (int i = 0; i < kicks.size(); i++) {
                        if (kicks.get(i).getCommand().equalsIgnoreCase(cmdSplit[0])) {
                            event.getBot().sendRaw().rawLine("KICK " + channel + " " + cmdSplit[1] + " " + kicks.get(i).getMessage());
                            return;
                        }
                    }
                }
                else {
                    event.getBot().sendIRC().notice(caller, "Kick commands take one single input, the nick of the user to kick");
                }
            }
            else {
                for (int i = 0; i < kicks.size(); i++) {
                    if (kicks.get(i).getCommand().equalsIgnoreCase(cmdSplit[0]) && kicks.get(i).getFailureMessage() != null) {
                        event.getBot().sendRaw().rawLine("KICK " + channel + " " + caller +  " " + kicks.get(i).getFailureMessage());
                        return;
                    }
                }
            }
        }
        else {
            event.getBot().sendIRC().message(respondTo, "You can't kick someone from a private message");
        }
    }
    
    public String kickHelpString(KickInterface kick) {
        String helpString;
        helpString = Colors.BOLD + "Command: " + Colors.NORMAL + kick.getCommand();
        helpString += Colors.BOLD + " Whitelisted Users: " + Colors.NORMAL + Global.botOwner + (kick.getAllowedUsers() == null ? "" : ", " + IRCUtils.arrayListToString(kick.getAllowedUsers()));
        if (kick.getChannelList() != null) {
            if (kick.isChannelListWhitelist()) {
                helpString += Colors.BOLD + " Channel Whitelist: " + Colors.NORMAL;
            }
            else {
                helpString += Colors.BOLD + " Channel Blacklist: " + Colors.NORMAL;
            }
            helpString += IRCUtils.arrayListToString(kick.getChannelList());
        }
        return helpString;
    }
    
    private Collection<? extends String> getKickCommands() {
        ArrayList<String> kickCommands = new ArrayList<>();
        
        for (int i = 0; i < kicks.size(); i++) {
            kickCommands.add(kicks.get(i).getCommand());
        }
        return kickCommands;
    }
    
    private void addKick(String command, String message) {
        addKick(command, message, null, null, null, false);
    }
    
    private void addKick(String command, String message, String failureMessage) {
        addKick(command, message, failureMessage, null, null, false);
    }
    private void addKick(String command, String message, String failureMessage, ArrayList<String> allowedUsers, ArrayList<String> blockedChans, boolean isWhitelist) {
        kicks.add(new CustomKick(command, message, failureMessage, allowedUsers, blockedChans, isWhitelist));
    }
    
    private boolean delKick(String command) {
        for (int i = 0; i < kicks.size(); i++) {
            if (kicks.get(i).getCommand().equalsIgnoreCase(command)) {
                kicks.remove(i);
                return true;
            }
        }
        for (int i = 0; i < unloadedKicks.size(); i++) {
            if (unloadedKicks.get(i).getCommand().equalsIgnoreCase(command)) {
                unloadedKicks.remove(i);
                return true;
            }
        }
        return false;
    }
    
    private boolean unloadKick(String command) {
        for (int i = 0; i < kicks.size(); i++) {
            if (kicks.get(i).getCommand().equalsIgnoreCase(command)) {
                unloadedKicks.add(kicks.get(i));
                kicks.remove(i);
                return true;
            }
        }
        return false;
    }
    
    private boolean loadKick(String command) {
        for (int i = 0; i < unloadedKicks.size(); i++) {
            if (unloadedKicks.get(i).getCommand().equalsIgnoreCase(command)) {
                kicks.add(unloadedKicks.get(i));
                unloadedKicks.remove(i);
                return true;
            }
        }
        return false;
    }
    private KickInterface getKick(String command) {
        for (int i = 0; i < kicks.size(); i++) {
            if (kicks.get(i).getCommand().equalsIgnoreCase(command)) {
                return kicks.get(i);
            }
        }
        return null;
    }
    private static ArrayList<KickInterface> loadKicks() {
        ArrayList<KickInterface> availableKicks = new ArrayList<>();
        availableKicks.add(new HackKick());
        availableKicks.add(new SmashKick());
        
        return availableKicks;
    }
}