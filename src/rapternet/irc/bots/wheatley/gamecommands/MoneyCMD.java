/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.gamecommands;

import rapternet.irc.bots.wheatley.objects.CommandGame;
import rapternet.irc.bots.common.objects.CommandMetaData;
import static rapternet.irc.bots.wheatley.listeners.GameListener.scores;
import rapternet.irc.bots.wheatley.listeners.Global;
import java.util.ArrayList;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class MoneyCMD implements CommandGame {
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
        
        CommandMetaData commandData = new CommandMetaData(event, true);
        String caller = commandData.getCaller();
        String command = commandData.getCommand();
        String[] cmdSplit = commandData.getCommandSplit();
        for(int i = 0; i < cmdSplit.length; i++)
            System.out.println(cmdSplit[i]);
        
        if (cmdSplit.length==1){ // Get your current score
            int userScore = scores.getScore(caller);
            if (userScore == Integer.MIN_VALUE){
                event.getBot().sendIRC().notice(caller, "USER NOT FOUND");
            }
            else
                event.respond("You currently have $"+userScore);
        }
        
        else if (cmdSplit.length == 2){ // Get someone elses current score
            System.out.println("Get User Money");
            String user = cmdSplit[1];
            int userScore = scores.getScore(user);
            if (userScore ==Integer.MIN_VALUE){
                event.getBot().sendIRC().notice(caller, "USER NOT FOUND");
            }
            else
                event.respond(user+" currently has $"+userScore);
        }
        
        else if (cmdSplit.length == 3&&caller.equalsIgnoreCase(Global.botOwner)){
            boolean isVerified = commandData.isVerifiedBotOwner();
            String responseLocation = commandData.respondToCallerOrMessageChan();
            
            if(isVerified) {
                String user = cmdSplit[1];
                String score = cmdSplit[2];
                int userCurrentScore = scores.getScore(user);
                
                if (userCurrentScore < Integer.MIN_VALUE){
                    event.getBot().sendIRC().notice(caller, "USER NOT FOUND");
                }
                
//                else if (!score.matches("[0-9]+")){
//                    event.getBot().sendIRC().notice(caller,"Input number must be a positive integer");
//                }
                
//                    else if(score.matches("\\-[0-9]+")){
//                        event.getBot().sendIRC().notice(event.getUser().getNick(),"You cannot give a user a negative score");
//                    }
                
                else{
                    scores.setScore(user, Integer.parseInt(score));
                    event.getBot().sendIRC().message(responseLocation,user+" currently has $"+scores.getScore(user));
                }
            }
        }
    }
        
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("money");
        return a;
        
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
}
