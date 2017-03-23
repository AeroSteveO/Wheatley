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
import static Wheatley.Swapper.logger;
import java.util.ArrayList;
import java.util.Arrays;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 *
 * Requirements: - APIs N/A - Custom Objects Command - Utilities N/A - Linked
 * Classes N/A
 *
 * Activate Command with: !PickAPort Responds with a valid port number for use
 * in a program
 *
 */
public class Hashtagify implements Command {

  @Override
  public String toString() {
    return ("hashtagify");
  }

  @Override
  public boolean isCommand(String toCheck) {
    return commandTerms().contains(toCheck.toLowerCase());
  }

  @Override
  public ArrayList<String> help(String command) {
    ArrayList<String> a = new ArrayList<>();
    a.add(Colors.BOLD + Global.commandPrefix + "Hashtagify" + Colors.NORMAL + ": Turns the previous line into an amazing hashtag");
    return a;
  }

  @Override
  public ArrayList<String> commandTerms() {
    ArrayList<String> a = new ArrayList<>();
    a.add("hashtagify");
    a.add("#");
    return a;
  }

  @Override
  public void processCommand(Event event) {
    CommandMetaData commandData = new CommandMetaData(event, false);
    String respondTo = commandData.respondToIgnoreMessage();
    String sender = commandData.getCaller();
    String channel = commandData.getEventChannel();
    String[] cmdSplit = commandData.getCommandSplit();

    ArrayList<ArrayList<String>> logCopy = CommandListener.logger.getArray(channel);
    if (CommandListener.logger.isEmpty(channel)) {
      return;
    }

    if (cmdSplit.length == 2) {
      String nick = cmdSplit[1];

      int i = logCopy.size() - 2;
      boolean found = false;
      String line = new String();
      while (!found && i >= 0) {
        if (logCopy.get(i).get(0).replaceAll("(<|>)", "").equalsIgnoreCase(nick)) {
          found = true;
          nick = logCopy.get(i).get(0);
          line = logCopy.get(i).get(1);
        }
        i--;
      }
      if (!found) {
        event.getBot().sendIRC().notice(sender, "!hashtagify nick not found in log");
      } else {
        event.getBot().sendIRC().message(channel, nick + " " + hashtagify(line));
      }
    } else {
      String line = logCopy.get(logCopy.size() - 2).get(1);
      event.getBot().sendIRC().message(respondTo, hashtagify(line));
    }
  }

  String capFirst(String name) {
    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    return name;
  }

  String hashtagify(String line) {
    String[] lineSplit = line.split(" ");

    String hashtag = "#";
    for (String item : lineSplit) {
      hashtag += capFirst(item.trim());
    }
    return hashtag;
  }
}
