/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package GameCommands;

import Objects.CommandGame;
import Objects.CommandMetaData;
import static Wheatley.GameListener.scores;
import java.util.ArrayList;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class Merge implements CommandGame {
    
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
        String[] cmdSplit = data.getCommandSplit();
        String channel = data.getCommandChannel();
        boolean isVerified = data.isVerifiedBotOwner();
        
        if (cmdSplit.length==3){
            if (isVerified) {
                
                String mergeThis = cmdSplit[1];
                String mergeIntoThis = cmdSplit[2];
                
                if (scores.getScore(mergeThis)==Integer.MIN_VALUE){
                    event.getBot().sendIRC().notice(sender, mergeThis+": USER NOT FOUND");
                }
                else if (scores.getScore(mergeIntoThis)==Integer.MIN_VALUE){
                    event.getBot().sendIRC().notice(sender, mergeIntoThis+": USER NOT FOUND");
                }
                else{
                    scores.merge(mergeThis,mergeIntoThis);
                    event.getBot().sendIRC().message(channel, mergeIntoThis+" currently has $"+scores.getScore(mergeIntoThis));
                }
            }
        }
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("merge");
        return a;
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
}
