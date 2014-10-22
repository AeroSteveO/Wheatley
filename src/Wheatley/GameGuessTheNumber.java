/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.Game;
import Objects.TimedWaitForQueue;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
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
        
        if ((message.split(" ")[0].equalsIgnoreCase("!GuessTheNumber")||message.split(" ")[0].equalsIgnoreCase("!guessnumber"))&&!Global.channels.areGamesBlocked(gameChan)) {
            
            if (!Global.activeGame.isGameActive(gameChan, "guessthenumber")){
                String[] options = message.split(" ");
                int length = 100;
                
                if (!options[1].matches("[0-9]+")){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"You must input an integer");
                    return;
                }
                if (options.length>2){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"This command takes 1 integer inputs maximum");
                    return;
                }
                
                if (options.length==2){
                    length = Integer.parseInt(options[1]);
                    if (length>1000)
                        length=1000;
                }
                
                int lives = length / 10;
                int time = 30+(length)/5;
                
                Global.activeGame.add(new Game( gameChan, "guessthenumber", "int", length, 1, time));
                currentIndex = Global.activeGame.getGameIdx(gameChan,"guessthenumber");
                String solution = Integer.toString(Global.activeGame.get(currentIndex).getInt());
                
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
                                
                                int timeSpent = Global.activeGame.get(currentIndex).getTimeSpent();
                                int prize = GameControl.scores.addScore(CurrentEvent.getUser().getNick(), basePrize+Integer.toString(length).length()+lives,Integer.toString(length).length(), timeSpent, time);
                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + " entered the number in "+timeSpent+" seconds and wins $"+prize+". Number: " + Colors.BOLD+Colors.RED+solution);
                                
//                                event.getBot().sendIRC().message(gameChan,"Congratulations " + CurrentEvent.getUser().getNick() +  ", you've found the number: " + Colors.BOLD +Colors.RED+ solution + Colors.NORMAL);
                                running = false;
                                timedQueue.end();
                            }
                            else if (Integer.parseInt(guess)<Integer.parseInt(solution)){
                                CurrentEvent.respond("Your guess is too low, lives left: "+lives);
                                lives--;
                            }
                            else if (Integer.parseInt(guess)>Integer.parseInt(solution)){
                                CurrentEvent.respond("Your guess is too high, lives left: "+lives);
                                lives--;
                            }
                        }
                    }
                }
                Global.activeGame.remove(Global.activeGame.getGameIdx(gameChan,"guessthenumber")); //updated current index of the game
            }
        }
    }
}