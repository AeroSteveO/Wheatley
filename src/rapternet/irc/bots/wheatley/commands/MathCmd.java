/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapternet.irc.bots.wheatley.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import rapternet.irc.bots.common.objects.Command;
import rapternet.irc.bots.common.objects.CommandMetaData;
import rapternet.irc.bots.wheatley.listeners.Global;

/**
 *
 * @author Stephen
 */
public class MathCmd implements Command {

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
    a.add("math"); // Terms that when prefixed by the command prefix, will activate the command
    a.add("matlab"); // Terms that when prefixed by the command prefix, will activate the command
// NOTE: these should be all lowercase
    return a;
  }

  @Override
  public ArrayList<String> help(String command) {
    ArrayList<String> a = new ArrayList<>();
    a.add(Colors.BOLD + Global.commandPrefix + "math [maths]" + Colors.NORMAL + ": Converts the input words into morse code. ");
    return a;
  }

  @Override
  public void processCommand(Event event) {

    CommandMetaData data = new CommandMetaData(event, true);
    String[] cmdSplit = data.getCommandSplit();

    String built = "";
    for (int i = 1; i < cmdSplit.length; i++) {
      built += cmdSplit[i].toLowerCase();

    }
    try {
      ScriptEngineManager mgr = new ScriptEngineManager();
      ScriptEngine engine = mgr.getEngineByName("JavaScript");
      String out = built;

      event.respond(engine.eval(built).toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
