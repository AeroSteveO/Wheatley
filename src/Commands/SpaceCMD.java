/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import Objects.Command;
import Objects.CommandMetaData;
import Wheatley.Global;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 *
 * Activate Command with: !SPACE Responds with a space core quote from portal 2
 *
 */
public class SpaceCMD implements Command {

  @Override
  public String toString() {
    return ("SPACE");
  }

  @Override
  public boolean isCommand(String toCheck) {
    return false;
  }

  @Override
  public ArrayList<String> help(String command) {
    ArrayList<String> a = new ArrayList<>();
    a.add(Colors.BOLD + Global.commandPrefix + "Space" + Colors.NORMAL + ": SPAAAAAAAAAAAAAAAAAAAACE");
    return a;
  }

  @Override
  public ArrayList<String> commandTerms() {
    ArrayList<String> a = new ArrayList<>();
    a.add("space");
    return a;
  }

  @Override
  public void processCommand(Event event) {
    CommandMetaData data = new CommandMetaData(event, false);
    String respondTo = data.respondToIgnoreMessage();
    switch ((int) (Math.random() * 3 + 1)) {
      case 1:
        event.getBot().sendIRC().message(respondTo, "What's your favorite thing about space? Mine is space.");
        break;
      case 2:
        event.getBot().sendIRC().message(respondTo, "Ohmygodohmygodohmygod! I'm in space!");
        break;
      case 3:
        String chan = data.getEventChannel();
        event.getBot().sendIRC().message(respondTo, "Oh oh oh. This is space! I'm in space!");
        event.getBot().sendIRC().message(respondTo, "So much space. Need to see it all.");
        event.getBot().sendRaw().rawLine("KICK " + chan + " " + data.getCaller() + " Space Court. For people in space. Judge space sun presiding. Bam. Guilty. Of being in space.");
        break;
    }
  }
}
