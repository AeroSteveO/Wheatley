/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.TimedWaitForQueue;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 * 
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Classes
 *    TimedWaitForQueue
 * 
 */
public class GameLuckyLotto extends ListenerAdapter {
    int time = 15; // seconds
    int baseBet = 25;
    
    @Override
    public void onMessage(MessageEvent event) throws InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.startsWith(Global.commandPrefix)){
            String command = message.split(Global.commandPrefix)[1].toLowerCase();
            String[] cmdSplit = command.split(" ");
            
            if(command.equalsIgnoreCase("luckylotto")&&!Global.channels.areGamesBlocked(event.getChannel().getName())){
                if(cmdSplit.length==2){
                    
                    if (!cmdSplit[1].matches("[0-9]")){
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"Your guess must be a single digit integer");
                    }
                    else{
                        int solution = (int) Math.random()*10;
                        int bet = baseBet;
                        ArrayList<String> player = new ArrayList<>();
                        ArrayList<String> guess = new ArrayList<>();
                        
                        event.getBot().sendIRC().message(event.getChannel().getName(),"Everyone in the channel has "+time+" seconds to choose a number between 0 and 9."
                                + " Each player who guessed the correct number wins. The more players joined, the higher the prize. Current buy in is $"+bet);
                        
                        player.add(event.getUser().getNick());
                        guess.add(cmdSplit[1]);
                        String gameChan = event.getChannel().getName();
                        
                        int key=(int) (Math.random()*100000+1);
                        TimedWaitForQueue timedQueue = new TimedWaitForQueue(event,time,key);
                        boolean running = true;
                        while (running){
                            try {
                                MessageEvent CurrentEvent = timedQueue.waitFor(MessageEvent.class);
                                String currentChan = CurrentEvent.getChannel().getName();
                                String currentMessage = Colors.removeFormattingAndColors(CurrentEvent.getMessage());
                                
                                if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                                    event.getBot().sendIRC().message(currentChan,"TIMES UP");
                                    running = false;
                                    timedQueue.end();
                                }
                                
                                if (message.startsWith(Global.commandPrefix)&&currentChan.equalsIgnoreCase(gameChan)){
                                    String currentCommand = currentMessage.split(Global.commandPrefix)[1].toLowerCase();
                                    String[] currentCmdSplit = currentCommand.split(" ");
                                    
                                    if(currentCommand.equalsIgnoreCase("luckylotto")&&cmdSplit.length==2){
                                        
                                        if (!currentCmdSplit[1].matches("[0-9]")){
                                            event.getBot().sendIRC().notice(event.getUser().getNick(),"Your guess must be a single digit integer");
                                        }
                                        else{
                                            player.add(CurrentEvent.getUser().getNick());
                                            guess.add(currentCmdSplit[1]);
                                        }
                                    }
                                }
                            }
                            catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                }
                else{
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"You need to pick a single integer for a lotto number");
                }
            }
        }
    }
}
