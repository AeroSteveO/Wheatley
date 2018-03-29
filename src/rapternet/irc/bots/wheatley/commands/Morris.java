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
import java.util.HashMap;
import java.util.Map;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 *
 * Requirements: - APIs N/A - Custom Objects Command CommandMetaData - Utilities
 * N/A - Linked Classes N/A
 *
 * Activate Command with: !Command Wheatley, command Command description here
 *
 */
public class Morris implements Command {

  @Override
  public String toString() {
    return ("Convert ASCII to morse code");
  }

  @Override
  public boolean isCommand(String toCheck) {
    return false; // Phrase that when spoken will activate the command
  }

  @Override
  public ArrayList<String> commandTerms() {
    ArrayList<String> a = new ArrayList<>();
    a.add("morris"); // Terms that when prefixed by the command prefix, will activate the command
    a.add("morse"); // Terms that when prefixed by the command prefix, will activate the command
// NOTE: these should be all lowercase
    return a;
  }

  @Override
  public ArrayList<String> help(String command) {
    ArrayList<String> a = new ArrayList<>();
    a.add(Colors.BOLD + Global.commandPrefix + "morris [words]" + Colors.NORMAL + ": Converts the input words into morse code. ");
    return a;
  }

  Map<String, String> morse = generateMap();

  @Override
  public void processCommand(Event event) {

    CommandMetaData data = new CommandMetaData(event, true);
    String[] cmdSplit = data.getCommandSplit();

    String out = "";
    for (int i = 1; i < cmdSplit.length; i++) {
      for (char c : cmdSplit[i].toLowerCase().toCharArray()) {
        out += morse.getOrDefault(String.valueOf(c), "") + " ";

      }
    }
    event.respond(out);
  }

  public Map<String, String> generateMap() {
    Map<String, String> morris = new HashMap<>();
    morris.put("a", ".-");
    morris.put("b", "-...");
    morris.put("c", "-.-.");
    morris.put("d", "-..");
    morris.put("e", ".");
    morris.put("f", "..-.");
    morris.put("g", "--.");
    morris.put("h", "....");
    morris.put("i", "..");
    morris.put("j", ".---");
    morris.put("k", "-.-");
    morris.put("l", ".-..");
    morris.put("m", "--");
    morris.put("n", "-.");
    morris.put("o", "---");
    morris.put("p", ".--.");
    morris.put("q", "--.-");
    morris.put("r", ".-.");
    morris.put("s", "...");
    morris.put("t", "-");
    morris.put("u", "..-");
    morris.put("v", "...-");
    morris.put("w", ".--");
    morris.put("x", "-..-");
    morris.put("y", "-.--");
    morris.put("z", "--..");

    morris.put("0", "-----");
    morris.put("1", ".----");
    morris.put("2", "..---");
    morris.put("3", "...--");
    morris.put("4", "....-");
    morris.put("5", ".....");
    morris.put("6", "-....");
    morris.put("7", "--...");
    morris.put("8", "---..");
    morris.put("9", "----.");

    morris.put(".", ".-.-.-");
    morris.put(",", "--..--");
    morris.put("?", "..--..");
    morris.put("!", "..--.");
    morris.put(":", "---...");
    morris.put("\"", ".-..-.");
    morris.put("'", ".----.");
    morris.put("=", "-..-");

    return morris;
  }
}
