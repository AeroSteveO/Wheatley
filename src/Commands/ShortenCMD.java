/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Objects.Command;
import Objects.CommandMetaData;
import Objects.Shorten.Bitly;
import Objects.Shorten.DtellaShorten;
import Objects.Shorten.IsGd;
import Objects.Shorten.ShortenerInterface;
import Wheatley.Global;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 */
public class ShortenCMD implements Command {
    ArrayList<ShortenerInterface> shorteners = getShorteners();
    
    @Override
    public String toString(){
        return("Shortens URLs with is.gd");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false; // Phrase that when spoken will activate the command
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("shorten");
        return a;
    }
    
        @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        a.add(Colors.BOLD + Global.commandPrefix + "Shorten [URL]" + Colors.NORMAL + ": Responds with a shortened version of the input URL");
        a.add(Colors.BOLD + Global.commandPrefix + "Shorten [tag] [URL]" + Colors.NORMAL + ": Returns a shortened version of the URL using the specified link shortener");
        a.add(Colors.BOLD + "Supported Tags/Shorteners");
        for (int i = 0; i < shorteners.size(); i++) {
            a.add(Colors.BOLD + shorteners.get(i).getInfo());
        }
        return a;
    }

    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData data = new CommandMetaData(event,false);
        
        String caller = data.getCaller(); // Nick of the user who called the command
        String[] cmdSplit = data.getCommandSplit();
        
        if (cmdSplit.length == 2) {
            try {
                String shortenedURL = null;
                int c = 0;
                
                while (shortenedURL == null && shorteners.size() > c) {
                    shortenedURL = shorteners.get(c).shorten(cmdSplit[1]);
                    c++;
                }
                
                event.respond(shortenedURL);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                event.getBot().sendIRC().notice(caller, "Shorten: URL unable to be shortened. Either something broke or your url is a dud");
            }
        }
        else if (cmdSplit.length == 3) {
            boolean found = false;
            for (int i = 0; i < shorteners.size(); i++) {
                if (shorteners.get(i).isShortIdentifier(cmdSplit[1])) {
                    String shortenedURL = shorteners.get(i).shorten(cmdSplit[2]);
                    if (shortenedURL == null) {
                        event.respond("Shorten: " + shorteners.get(i).getName() + " failed to shorten the input URL");
                    }
                    event.respond(shortenedURL);
                    found = true;
                }
            }
            if(!found) {
                event.getBot().sendIRC().notice(caller, "Shorten: Link shortener ID not found | Command should be of form \"!shorten [id] [link]\"");
                for (int i = 0; i < shorteners.size(); i++) {
                    event.getBot().sendIRC().notice(caller, shorteners.get(i).getInfo());
                }
                
            }
        }
        else {
            event.getBot().sendIRC().notice(caller, "Shorten: Command should take form of \"!shorten [id] [link]\" where the ID is an optional identifier for which link shortening tool to use");
        }
    }
    
    private ArrayList<ShortenerInterface> getShorteners() {
        ArrayList<ShortenerInterface> shorteners = new ArrayList<>();
        shorteners.add(new DtellaShorten());
        shorteners.add(new Bitly());
        shorteners.add(new IsGd());
        return shorteners;
    }
}
