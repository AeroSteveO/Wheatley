/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.gamecommands;

import rapternet.irc.bots.wheatley.objects.CommandGame;
import rapternet.irc.bots.wheatley.objects.CommandMetaData;
import static rapternet.irc.bots.wheatley.listeners.GameListener.activeGame;
import static rapternet.irc.bots.wheatley.listeners.GameListener.scores;
import java.util.ArrayList;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class Flush implements CommandGame {
    
    @Override
    public boolean isGame() {
        return false;
    }
    
    @Override
    public boolean isShortGame() {
        return true;
    }
    
    @Override
    public void processCommand(MessageEvent event){
        
        CommandMetaData data = new CommandMetaData(event, true);
        
        String sender = data.getCaller();
//        String[] cmdSplit = data.getCommandSplit();
        boolean isVerified = data.isVerifiedBotOwner();
        
        if (isVerified) {
            activeGame.clear();
            scores.clean();
            event.getBot().sendIRC().notice(sender, "Cleaned out game files");
        }
        else{
            event.getBot().sendIRC().notice(sender, "You do not have access to this function");
        }
        
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("flush");
        return a;
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
}
