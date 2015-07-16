/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.Game;
import Objects.GameMod;
import Objects.TimedWaitForQueue;
import Utils.GameUtils;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    Game
 *    TimedWaitForQueue
 * - Linked Classes
 *    Global
 *    GameListener
 * 
 * Activate Command with:
 *      !GuessTheNumber [upperBound]
 *      !GuessNumber [upperBound]
 *
 *          Options include:
 *              Upper Bound: The upper bound of the search area, 1 - upper bound
 *                           If no upper bound is input, its assumed to be 100
 *
 *
 */
public class GameGuessTheNumber extends ListenerAdapter {
    String blockedChan = "#dtella";
    int basePrize = 15; // $
    //static Game.GameArray activeGame = new Game.GameArray();
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String gameChan = event.getChannel().getName();
        int currentIndex=0;
        
        if ((message.split(" ")[0].equalsIgnoreCase("!GuessTheNumber")||message.split(" ")[0].equalsIgnoreCase("!guessnumber"))&&!GameUtils.areGamesBlocked(gameChan)) {
            
            int length = 100;
            
            if (message.split(" ").length>=2){
                
                String[] options = message.split(" ");
                
                if (!options[1].matches("[0-9]+")){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"You must input an integer");
                    return;
                }
                
                else if (options.length>2){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"This command takes 1 integer input maximum");
                    return;
                }
                
                else if (options.length==2){
                    
                    length = Integer.parseInt(options[1]);
                    
                    if (length>1000)
                        length=1000;
                    
                    else if (length<10)
                        length = 10;
                    
                }
            }
            
            
            if (!GameListener.activeGame.isGameActive(gameChan, "guessthenumber", "long")){
                
                
                int lives =(int) 10+length/100;
                int time = 30+(length)/5;
                
                Game currentGame = new Game(GameMod.INT, length, 1);
//                currentIndex = Global.activeGame.getGameIdx(gameChan,"guessthenumber");
                String solution = Integer.toString(currentGame.getInt());
                
                boolean running=true;
                int key=(int) (Math.random()*100000+1);
                TimedWaitForQueue timedQueue = new TimedWaitForQueue(event,time,key);
                event.respond("Try to correctly guess the number (1-"+Integer.toString(length)+")");
                
                while (running){
                    MessageEvent CurrentEvent = timedQueue.waitFor(MessageEvent.class);
                    String guess = CurrentEvent.getMessage();
                    if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                        event.getBot().sendIRC().message(gameChan,"Game over! You've run out of time. "+Colors.BOLD+Colors.RED + solution + Colors.NORMAL + " would have been the solution.");
                        running = false;
                        timedQueue.end();
                    }
                    else if (CurrentEvent.getChannel().getName().equalsIgnoreCase(gameChan)&&!CurrentEvent.getUser().getNick().equalsIgnoreCase(event.getBot().getNick())){
                        if ((CurrentEvent.getMessage().equals("!fuckthis")||(CurrentEvent.getMessage().equalsIgnoreCase("I give up")))){
                            CurrentEvent.respond("You have given up! Correct answer was " +Colors.BOLD+Colors.RED+ solution);
                            running = false;
                            timedQueue.end();
                        }
                        else if (Pattern.matches("[0-9]{"+1+","+length+"}",guess)){
                            if (lives<=0){
                                CurrentEvent.respond("You've run out of lives, the solution was "+solution);
                                running = false;
                                timedQueue.end();
                            }
                            else if (guess.equalsIgnoreCase(solution)){
                                
                                int timeSpent = currentGame.getTimeSpent();
                                int prize = GameListener.scores.addScore(CurrentEvent.getUser().getNick(), basePrize+Integer.toString(length).length()+lives,Integer.toString(length).length(), timeSpent, time);
                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + " entered the number in "+timeSpent+" seconds and wins $"+prize+". Number: " + Colors.BOLD+Colors.RED+solution);
                                
//                                event.getBot().sendIRC().message(gameChan,"Congratulations " + CurrentEvent.getUser().getNick() +  ", you've found the number: " + Colors.BOLD +Colors.RED+ solution + Colors.NORMAL);
                                running = false;
                                timedQueue.end();
                            }
                            else if (Integer.parseInt(guess)<Integer.parseInt(solution)){
                                lives--;
                                CurrentEvent.respond("Your guess is too low, lives left: "+lives);
                            }
                            else if (Integer.parseInt(guess)>Integer.parseInt(solution)){
                                lives--;
                                CurrentEvent.respond("Your guess is too high, lives left: "+lives);
                            }
                        }
                    }
                }
                GameListener.activeGame.remove(gameChan,"guessthenumber"); //updated current index of the game
            }
            else
                event.getBot().sendIRC().notice(event.getUser().getNick(),"Game Currently running in this channel");
        }
    }
}