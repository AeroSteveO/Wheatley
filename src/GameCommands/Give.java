/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package GameCommands;

import Objects.CommandGame;
import Objects.CommandMetaData;
import static Wheatley.GameListener.scores;
import Wheatley.Global;
import java.util.ArrayList;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class Give implements CommandGame {
    
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
        
        if (cmdSplit.length == 3) {
            
            String reciever = cmdSplit[1];
            String toGive = cmdSplit[2];
            
            if (toGive.matches("[0-9]+")){
                
                if (scores.getScore(reciever)==Integer.MIN_VALUE){
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "USER NOT FOUND");
                }
                
                else{
                    
                    int givings = Integer.parseInt(cmdSplit[2]);
                    
                    if (scores.getScore(sender)>givings){
                        scores.addScore(reciever,givings);
                        scores.subtractScore(sender,givings);
                        event.getBot().sendIRC().message(channel,reciever+" currently has $"+scores.getScore(reciever)+", "+sender+" currently has $"+scores.getScore(sender));
                    }
                    else{
                        event.getBot().sendIRC().message(channel, sender+": You do not have enough money to give that much");
                    }
                }
            }
            
            else if(toGive.matches("\\-[0-9]+")){
                event.getBot().sendIRC().notice(sender,"GIVE: You cannot take money through the give command");
            }
            
            else{
                event.getBot().sendIRC().notice(sender,"GIVE: Input number must be an integer");
            }
        }
        else {
            event.getBot().sendIRC().notice(sender,"GIVE: This command takes two inputs, the reciever and the amount \""+Global.commandPrefix+"Give [nickname] [amount]\"");
        }
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("give");
        return a;
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
}
