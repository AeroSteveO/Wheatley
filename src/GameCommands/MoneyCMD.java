/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package GameCommands;

import Objects.CommandGame;
import Objects.CommandMetaData;
import static Wheatley.GameControl.scores;
import Wheatley.Global;
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
    public void processCommand(MessageEvent event){
        
        CommandMetaData commandData = new CommandMetaData(event, true);
        String caller = commandData.getCaller();
        String command = commandData.getCommand();
        String[] cmdSplit = commandData.getCommandSplit();
        
        if (cmdSplit.length==1){ // Get your current score
            int userScore = scores.getScore(caller);
            if (userScore == Integer.MIN_VALUE){
                event.getBot().sendIRC().notice(caller, "USER NOT FOUND");
            }
            else
                event.respond("You currently have $"+userScore);
        }
        
        else if (command.split(" ").length==2){ // Get someone elses current score
            String user = command.split(" ")[1];
            int userScore = scores.getScore(user);
            if (userScore ==Integer.MIN_VALUE){
                event.getBot().sendIRC().notice(caller, "USER NOT FOUND");
            }
            else
                event.respond(user+" currently has $"+userScore);
        }
        
        else if (cmdSplit.length==3&&caller.equalsIgnoreCase(Global.botOwner)){
            boolean isVerified = commandData.isVerifiedBotOwner();
            String responseLocation = commandData.respondToCallerOrMessageChan();
            
            if(isVerified) {
                
                String user = command.split(" ")[1];
                String score = command.split(" ")[2];
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

        System.out.println("Cool Beans");
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
