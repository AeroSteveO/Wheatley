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
    String channel = commandData.getEventChannel();
    ArrayList<ArrayList<String>> logCopy = CommandListener.logger.getArray(channel);
    if (CommandListener.logger.isEmpty(channel)) {
      return;
    }
    String line = logCopy.get(logCopy.size() - 2).get(1);
    String[] lineSplit = line.split(" ");
    String hashtag = "#";
    for (String item : lineSplit) {
      hashtag += capFirst(item.trim());
    }
    event.getBot().sendIRC().message(respondTo, hashtag);
  }

  String capFirst(String name) {
    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    return name;
  }
}
