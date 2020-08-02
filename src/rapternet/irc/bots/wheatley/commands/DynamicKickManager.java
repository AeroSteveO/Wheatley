/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.commands;

import rapternet.irc.bots.common.objects.Command;
import rapternet.irc.bots.common.objects.CommandMetaData;
import rapternet.irc.bots.common.objects.CommandMetaData.EventType;
import rapternet.irc.bots.wheatley.objects.KickStorage;
import rapternet.irc.bots.wheatley.objects.kicks.CustomKick;
import rapternet.irc.bots.wheatley.objects.kicks.KickInterface;
import rapternet.irc.bots.common.utils.BotUtils;
import rapternet.irc.bots.common.utils.IRCUtils;
import rapternet.irc.bots.wheatley.listeners.Global;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import rapternet.irc.bots.wheatley.objects.Env;

/**
 *
 * @author Stephen
 *
 * The most BA package of kicks you'll ever see
 */
public class DynamicKickManager implements Command {
  private static KickStorage kickData = new KickStorage(Env.CONFIG_LOCATION + "kicks.json");
  private static KickStorage hist = new KickStorage(Env.CONFIG_LOCATION + "hist-kicks.json");
  
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
    a.addAll(kickData.getKickCommands());
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
      a.add(kickHelpString(command.toLowerCase()));
    }
    else if (command.equalsIgnoreCase(BotUtils.getClassName(this))) {
      ArrayList<String> kicks = kickData.getKeyList();
      for (int i = 0; i < kicks.size(); i++) {
        a.add(kickHelpString(kicks.get(i)));
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
          String blacklistChan = null;
          String whiteUsers = null;
          for (int i = 0; i < kickArgs.length; i++) {
            System.out.println(kickArgs[i].trim().toLowerCase() + " parsed");
            
            if (message.split("-c")[1].trim().toLowerCase().startsWith(kickArgs[i].trim().toLowerCase()) && kickCommand == null) {
              if (kickArgs[i].trim().split(" ").length != 1) {
                event.getBot().sendIRC().notice(caller, "Kick: Command can only be one word");
                return;
              }
              kickCommand = kickArgs[i].trim().toLowerCase();
              System.out.println(kickCommand + " parsed command");
            }
            else if (message.split("-f")[1].trim().toLowerCase().startsWith(kickArgs[i].trim().toLowerCase()) && kickFail == null) {
              kickFail = kickArgs[i].trim();
              System.out.println(kickFail + " parsed fail");
              
            }
            else if (message.split("-m")[1].trim().toLowerCase().startsWith(kickArgs[i].trim().toLowerCase()) && kickMessage == null) {
              kickMessage = kickArgs[i].trim();
              System.out.println(kickMessage + " parsed message");
              
            }
            else if ((message.contains("-b") && message.split("-b")[1].trim().toLowerCase().startsWith(kickArgs[i].trim().toLowerCase()) ||
                    message.contains("-w") && message.split("-w")[1].trim().toLowerCase().startsWith(kickArgs[i].trim().toLowerCase())) && blacklistChan == null) {
              blacklistChan = kickArgs[i].toLowerCase().trim();
            }
            else if (message.contains("-u") && message.split("-u")[1].trim().toLowerCase().startsWith(kickArgs[i].trim().toLowerCase()) && whiteUsers == null) {
              whiteUsers = kickArgs[i].toLowerCase().trim();
            }
          }
          if (kickData.contains(kickCommand)) {
            event.getBot().sendIRC().notice(caller, "Kick: A kick already exists for that command string");
            return;
          }
          
          kickData.addKick(kickCommand, kickMessage, kickFail, whiteUsers, blacklistChan, String.valueOf(message.contains("-w")));
          event.getBot().sendIRC().message(respondTo, "Kick: Success, " + kickCommand + " has been successfully added to the kick commands");
        }
        else if (message.contains("-c") || message.contains("-u") || message.contains("-f") || message.contains("-b") || message.contains("-m") || message.contains("-w")) {
          event.getBot().sendIRC().notice(caller, "Kick: Full set of inputs not found in input string, minimum inputs include -c, -f, -m");
        }
        else if (cmdSplit.length >= 3) {
          if (kickData.contains(cmdSplit[1])) {
            event.getBot().sendIRC().notice(caller, "Kick: A kick already exists for that command string");
            return;
          }
          
          String kickMessage = data.getMessage().split(" ", 3)[2];
          kickData.addKick(cmdSplit[1].toLowerCase(), kickMessage);
          event.getBot().sendIRC().message(respondTo, "Kick: Success, " + cmdSplit[1] + " has been successfully added to the kick commands");
        }
      }
      return;
    }
    else if (cmdSplit[0].equals("save")) {
      kickData.save();
      hist.save();
      event.getBot().sendIRC().notice(caller, "Kick: History and Kicks files saved");
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
      ArrayList<String> kicks = kickData.getKeyList();
      String kickList = Colors.BOLD + Colors.GREEN + "Enabled Kicks: " + Colors.NORMAL;
      for (int i = 0; i < kicks.size(); i++) {
        if (kickData.isKickEnabled(kicks.get(i)))
          kickList += kicks.get(i) + ", ";
      }
      kickList = kickList.substring(0, kickList.length()-2) + Colors.BOLD + Colors.RED + " Disabled Kicks: " + Colors.NORMAL;
      for (int i = 0; i < kicks.size(); i++) {
        if (!kickData.isKickEnabled(kicks.get(i)))
          kickList += kicks.get(i) + ", ";
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
          if (kickData.contains(cmdSplit[0])) {
            if (!kickData.isKickEnabled(cmdSplit[0])) {
              event.getBot().sendIRC().notice(caller, "Kick: "+cmdSplit[0]+" Command Currently Disabled");
            }
            else {
              event.getBot().sendRaw().rawLine("KICK " + channel + " " + cmdSplit[1] + " " + kickData.getMessage(cmdSplit[0]));
            }
          }
        }
        else {
          event.getBot().sendIRC().notice(caller, "Kick commands take one single input, the nick of the user to kick");
        }
      }
      else {
        if (kickData.contains(cmdSplit[0]) && !kickData.getFailureMessage(cmdSplit[0]).equals("null")) {
          if (!kickData.isKickEnabled(cmdSplit[0])) {
            event.getBot().sendIRC().notice(caller, "Kick: "+cmdSplit[0]+" Command Currently Disabled");
          }
          else {
            event.getBot().sendRaw().rawLine("KICK " + channel + " " + caller +  " " + kickData.getFailureMessage(cmdSplit[0]));
          }
        }
      }
    }
    else {
      event.getBot().sendIRC().message(respondTo, "You can't kick someone from a private message");
    }
  }
  
  public String kickHelpString(String kick) {
    String helpString;
    helpString = Colors.BOLD + "Command: " + Colors.NORMAL + (kick);
    helpString += Colors.BOLD + " Whitelisted Users: " + Colors.NORMAL + Global.botOwner + (kickData.getAllowedUsers(kick) == null ? "" : ", " + IRCUtils.arrayListToString(kickData.getAllowedUsers(kick)));
    if (kickData.getChannelList(kick) != null) {
      if (kickData.isChannelListWhitelist(kick)) {
        helpString += Colors.BOLD + " Channel Whitelist: " + Colors.NORMAL;
      }
      else {
        helpString += Colors.BOLD + " Channel Blacklist: " + Colors.NORMAL;
      }
      helpString += IRCUtils.arrayListToString(kickData.getChannelList(kick));
    }
    return helpString;
  }
  
  private boolean delKick(String command) {
    int i = 2;
    String cmd2 = command;
    while (hist.contains(cmd2)) {
      cmd2 = command + i;
      i++;
    }
    boolean succeed = hist.copyInFromStorage(kickData, command, cmd2);
    hist.save();
    if (succeed) {
      kickData.removeKey(command);
      kickData.save();
    }
    return succeed;
  }
  
  private boolean unloadKick(String command) {
    if (!kickData.contains(command)) {
      return false;
    }
    kickData.disableKick(command);
    return true;
  }
  
  private boolean loadKick(String command) {
    if (!kickData.contains(command)) {
      return false;
    }
    kickData.enableKick(command);
    return true;
  }
  
  private KickInterface getKick(String command) {
    KickInterface custo = new CustomKick(command, kickData.getMessage(command), kickData.getFailureMessage(command),
            kickData.getAllowedUsers(command), kickData.getChannelList(command), kickData.isChannelListWhitelist(command));
    return custo;  
  }
}