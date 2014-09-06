/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

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
    //static Game.GameArray activeGame = new Game.GameArray();
    
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String gameChan = event.getChannel().getName();
        int currentIndex=0;
        
        if ((message.split(" ")[0].equalsIgnoreCase("!GuessTheNumber")||message.split(" ")[0].equalsIgnoreCase("!guessnumber"))&&!Global.Channels.areGamesBlocked(gameChan)) {
            
            if (!Global.activeGame.isGameActive(gameChan, "guessthenumber")){
                String[] options = message.split(" ");
                int length = 100;
                
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
                Game.TimedWaitForQueue timedQueue = Global.activeGame.getGame(gameChan, "guessthenumber").new TimedWaitForQueue(event,time,key);
                event.respond("Try to correctly guess the number (1-"+Integer.toString(length)+")");

                while (running){
                    MessageEvent CurrentEvent = timedQueue.waitFor(MessageEvent.class);
                    String guess = CurrentEvent.getMessage();
                    String currentChan = CurrentEvent.getChannel().getName();
                    if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                        event.getBot().sendIRC().message(gameChan,"Game over! You've run out of time. "+Colors.BOLD + solution + Colors.NORMAL + " would have been the solution.");
                        running = false;
                        timedQueue.end();
                    }
                    else if ((CurrentEvent.getMessage().equals("!fuckthis")||(CurrentEvent.getMessage().equalsIgnoreCase("I give up")))&&currentChan.equals(gameChan)){
                        CurrentEvent.respond("You have given up! Correct answer was " + solution);
                        running = false;
                        timedQueue.end();
                    }
                    else if (Pattern.matches("[0-9]{"+1+","+length+"}",guess)&&currentChan.equalsIgnoreCase(gameChan)){
                        if (lives<=0){
                            CurrentEvent.respond("You've run out of lives, the solution was "+solution);
                            running = false;
                            timedQueue.end();
                        }
                        else if (guess.equalsIgnoreCase(solution)){
                            event.getBot().sendIRC().message(gameChan,"Congratulations " + CurrentEvent.getUser().getNick() +  ", you've found the number: " + Colors.BOLD + solution + Colors.NORMAL);
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
                Global.activeGame.remove(Global.activeGame.getGameIdx(gameChan,"guessthenumber")); //updated current index of the game
            }
        }
    }
}