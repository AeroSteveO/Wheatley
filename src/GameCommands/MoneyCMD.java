/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package GameCommands;

import Objects.Command;
import static Wheatley.GameControl.scores;
import Wheatley.Global;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

/**
 *
 * @author Stephen
 */
public class MoneyCMD implements Command{
    
    @Override
    public void processCommand(Event event){
        String message = new String();
        String caller = new String();
        if (event instanceof MessageEvent){
            MessageEvent mEvent = (MessageEvent) event;
            message = Colors.removeFormattingAndColors(mEvent.getMessage());
            caller = mEvent.getUser().getNick();
        }
        else if (event instanceof PrivateMessageEvent){
            PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
            message = Colors.removeFormattingAndColors(pmEvent.getMessage());
            caller = pmEvent.getUser().getNick();
        }
        else{
            return;
        }
        String command = message.split(Global.commandPrefix)[1];
        String[] cmdSplit = command.split(" ");
        
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
            boolean isVerified = false;
            String responseLocation = "";
            
            if (event instanceof PrivateMessageEvent){
                PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
                isVerified = pmEvent.getUser().isVerified();
                responseLocation = pmEvent.getUser().getNick();
            }
            
            else if (event instanceof MessageEvent){
                MessageEvent mEvent = (MessageEvent) event;
                isVerified = mEvent.getUser().isVerified();
                responseLocation = mEvent.getChannel().getName();
            }
            
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
