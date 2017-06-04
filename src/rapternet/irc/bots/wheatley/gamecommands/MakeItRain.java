/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.gamecommands;

import rapternet.irc.bots.wheatley.objects.CommandGame;
import rapternet.irc.bots.wheatley.objects.CommandMetaData;
import static rapternet.irc.bots.wheatley.listeners.GameListener.scores;
import rapternet.irc.bots.wheatley.listeners.Global;
import java.util.ArrayList;
import java.util.Iterator;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class MakeItRain implements CommandGame {
    
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
        
        CommandMetaData data = new CommandMetaData(event, false);
        
        String sender = data.getCaller();
        String[] cmdSplit = data.getCommandSplit();
        String channel = data.getCommandChannel();
        
        if (cmdSplit.length > 2){
            event.getBot().sendIRC().notice(sender, "!MakeItRain only accepts only 1 input");
        }
        else if(cmdSplit.length == 1 || cmdSplit[1].matches("[0-9]+")){
            int rain = 100;
            if (cmdSplit.length==2){
                rain = Integer.parseInt(cmdSplit[1]);
            }
            ArrayList<String> people = new ArrayList<>();
            
            if(scores.getScoreObj(sender).getScore()<rain){
                event.getBot().sendIRC().notice(sender, "You don't have enough to make it rain that much");
            }
            else{
                Iterator<User> userList = event.getChannel().getUsers().iterator();
                int numUsers = event.getChannel().getUsers().size();
                int earnings = rain/(numUsers-2);
                scores.subtractScore(sender, rain);
                
                while (userList.hasNext()){
                    User person = userList.next();
                    if (!person.getNick().equalsIgnoreCase(Global.mainNick)&&!person.getNick().equalsIgnoreCase(sender)){
                        scores.addScore(person.getNick(), earnings);
                        event.getBot().sendIRC().message(channel, person.getNick()+" is now $"+earnings+" richer");
                    }
                }
            }
            
        }
        else{
            event.getBot().sendIRC().notice(sender, "!MakeItRain only accepts non-negative integer as an input");
        }
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("makeitrain");
        return a;
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
}
