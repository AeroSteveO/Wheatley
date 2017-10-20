/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.listeners;

import rapternet.irc.bots.wheatley.objects.Game;
import rapternet.irc.bots.wheatley.objects.TimedWaitForQueue;
import rapternet.irc.bots.common.utils.GameUtils;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import rapternet.irc.bots.wheatley.objects.games.BlankWord;

/**
 *
 * @author Stephen
 * Based on the C# IRC bot, CasinoBot
 * which is generally unstable and requires windows to run
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    Game
 * - Linked Classes
 *    Global
 *    GameListener
 *
 * Activate Command with:
 *      !hangman
 */
public class GameHangman extends ListenerAdapter {
    // Woohooo basic variables for junk
    int baselives = 10;
    int time = 60;  // Seconds
    String blockedChan = "#dtella";
    int basePrize = 30; // $
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String gameChan = event.getChannel().getName();
        
        if (message.equalsIgnoreCase("!hangman")&&!GameUtils.areGamesBlocked(gameChan)) {
            
            if (!GameListener.activeGame.isGameActive(gameChan, "hangman", "long")){
                
                Game currentGame = new Game(new BlankWord());
                
                // Setup variables that will be needed through hangman
                int changed = 0;
                int correct = 0;
                int lives = baselives;
                
                String chosenword = currentGame.getChosenWord();
                String guess = currentGame.getSolution();
                char[] characters = chosenword.toCharArray();
                event.getBot().sendIRC().message(gameChan, "You have "+time+" seconds to find the following word: " + Colors.BOLD + guess + Colors.NORMAL);
                boolean running=true;
                int key=(int) (Math.random()*100000+1);
                TimedWaitForQueue timedQueue = new TimedWaitForQueue(event,time,key);
                
                while (running){
                    MessageEvent CurrentEvent = timedQueue.waitFor(MessageEvent.class);
                    changed = 0;
                    if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                        event.getBot().sendIRC().message(gameChan,"Game over! You've run out of time. "+Colors.BOLD+Colors.RED + chosenword.toUpperCase() + Colors.NORMAL + " would have been the solution.");
                        running = false;
                        timedQueue.end();
                    }
                    else if (CurrentEvent.getChannel().getName().equalsIgnoreCase(gameChan)&&!CurrentEvent.getUser().getNick().equalsIgnoreCase(event.getBot().getNick())){
                        if (Pattern.matches("[a-zA-Z]{2,}",CurrentEvent.getMessage())){
                            if (CurrentEvent.getMessage().equalsIgnoreCase(chosenword)){
                                int timeSpent = currentGame.getTimeSpent();
                                int prize = GameListener.scores.addScore(CurrentEvent.getUser().getNick(), basePrize+chosenword.length()+lives+(chosenword.length()-changed),chosenword.length(), timeSpent, time);
                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + " entered the solution in "+timeSpent+" seconds and wins $"+prize+". Solution: " + Colors.BOLD+Colors.RED+chosenword.toUpperCase());
                                
//                                event.getBot().sendIRC().message(gameChan,"Congratulations " + CurrentEvent.getUser().getNick() +  ", you've found the word: " + Colors.BOLD+Colors.RED + chosenword.toUpperCase() + Colors.NORMAL);
                                running=false;
                                timedQueue.end();
                            }
                            else{
                                lives--;
                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getMessage() + " is incorrect. Lives left: " + lives );
                                
                                if(lives <= 0){
                                    event.getBot().sendIRC().message(gameChan, "You've run out of lives! The word we looked for was " + Colors.BOLD + Colors.RED + chosenword.toUpperCase() + Colors.NORMAL);
                                    running = false;
                                    timedQueue.end();
                                }
                            }
                        }
                        else if (Pattern.matches("[a-zA-Z]{1}", CurrentEvent.getMessage())){
                            for (int i = 0; i<chosenword.length(); i++){
                                if (Character.toString(characters[i]).equalsIgnoreCase(CurrentEvent.getMessage())&&!Character.toString(guess.charAt(i)).equalsIgnoreCase(CurrentEvent.getMessage())){//Character.toString(blanks[i]).equalsIgnoreCase(CurrentEvent.getMessage())
                                    String temp = guess.substring(0,i)+CurrentEvent.getMessage()+guess.substring(i+1);
                                    guess = temp;
                                    event.getBot().sendIRC().message(gameChan, CurrentEvent.getMessage() + " is correct! " + Colors.BOLD + guess.toUpperCase() + Colors.NORMAL + " Lives left: " +  lives );
                                    correct++;
                                    changed = 1;
                                }
                            }
                            if (changed ==0){
                                lives--;
                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getMessage() + " is wrong or was already guessed. Lives left: " + lives );
                                
                                if(lives <= 0){
                                    event.getBot().sendIRC().message(gameChan, "You've run out of lives! The word we looked for was " + Colors.BOLD + Colors.RED + chosenword.toUpperCase() + Colors.NORMAL);
                                    running = false;
                                    timedQueue.end();
                                }
                            }
                            else if (correct == chosenword.length()){
                                int timeSpent = currentGame.getTimeSpent();
                                int prize = GameListener.scores.addScore(CurrentEvent.getUser().getNick(), basePrize+chosenword.length()+lives, chosenword.length(), timeSpent, time);
                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + " entered the solution in "+timeSpent+" seconds and wins $"+prize+". Solution: " + Colors.BOLD+Colors.RED+chosenword.toUpperCase());
//                                event.getBot().sendIRC().message(gameChan,"Congratulations " + CurrentEvent.getUser().getNick() +  ", you've found the word: " + Colors.BOLD +Colors.RED+ chosenword.toUpperCase() + Colors.NORMAL);
                                running = false;
                                timedQueue.end();
                            }
                        }
                        else if (CurrentEvent.getMessage().equals("!fuckthis")||CurrentEvent.getMessage().equalsIgnoreCase("I give up")){
                            CurrentEvent.respond("You have given up! Correct answer was " +Colors.BOLD+Colors.RED+ chosenword.toUpperCase());
                            running = false;
                            timedQueue.end();
                        }
                    }
                }
                GameListener.activeGame.remove(gameChan,"hangman"); //updated current index of the game
            }
            else
                event.getBot().sendIRC().notice(event.getUser().getNick(),"Game Currently running in this channel");
        }
    }
}