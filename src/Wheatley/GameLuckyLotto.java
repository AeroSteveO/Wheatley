/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.TimedWaitForQueue;
import Utils.GameUtils;
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
            
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            if(cmdSplit[0].equalsIgnoreCase("luckylotto")&&!GameUtils.areGamesBlocked(event.getChannel().getName())){
                
                if(cmdSplit.length==2){
                    
                    if (!cmdSplit[1].matches("[0-9]")){
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"Your guess must be a single digit integer");
                    }
                    else{
                        int solution = (int) (Math.random()*10);
                        System.out.println(solution);
                        int bet = baseBet;
                        
                        if (GameListener.scores.getScore(event.getUser().getNick())<bet){
                            event.getBot().sendIRC().notice(event.getUser().getNick(),"You don't have enough money to play this game");
                        }
                        else{
                            
                            
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
                                    
                                    if (currentMessage.startsWith(Global.commandPrefix)&&currentChan.equalsIgnoreCase(gameChan)){
                                        String currentCommand = currentMessage.split(Global.commandPrefix)[1].toLowerCase();
                                        String[] currentCmdSplit = currentCommand.split(" ");
                                        
                                        if(currentCommand.equalsIgnoreCase("luckylotto")&&cmdSplit.length==2){
                                            
                                            if (!currentCmdSplit[1].matches("[0-9]")){
                                                event.getBot().sendIRC().notice(event.getUser().getNick(),"Your guess must be a single digit integer");
                                            }
                                            else{
                                                if (GameListener.scores.getScore(CurrentEvent.getUser().getNick())<bet){
                                                    event.getBot().sendIRC().notice(event.getUser().getNick(),"You don't have enough money to play this game");
                                                }
                                                else{
                                                    player.add(CurrentEvent.getUser().getNick());
                                                    guess.add(currentCmdSplit[1]);
                                                }
                                            }
                                        }
                                    }
                                }
                                catch (Exception ex){
                                    ex.printStackTrace();
                                }
                            }// END TIMED WAIT FOR QUEUE
                            if(player.size()>1){
                                String winners = "";
                                int earned = bet*player.size()+25;
                                for (int i=0;i<player.size();i++){
                                    if(Integer.parseInt(guess.get(i))==solution){
                                        winners +=player.get(i)+", ";
                                        GameListener.scores.addScore(player.get(i),earned);
                                    }
                                    else{
                                        GameListener.scores.subtractScore(player.get(i),bet);
                                    }
                                }
                                if (!winners.equalsIgnoreCase("")){
                                    event.getBot().sendIRC().message(event.getChannel().getName(),"The following players were lucky and win $"+ earned + ": "+removeSecondLastChar(winners)+". Everybody else lost $"+bet+". Correct was: "+Colors.BOLD+solution);
                                }
                                else
                                    event.getBot().sendIRC().message(event.getChannel().getName(),"Every player lost $"+bet+" - the correct number would have been "+Colors.BOLD+solution);
                            }
                            else{
                                event.getBot().sendIRC().message(event.getChannel().getName(),"Nobody entered the game in time - it would have been "+Colors.BOLD+solution);
                            }
                        }
                    }
                }// END COMMAND SPLIT LENGTH CHECK
                else{
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"You need to pick a single integer for a lotto number");
                }
            }
        }
    }
    private static String removeSecondLastChar(String str) {
        return str.substring(0,str.length()-2);
    }
}
