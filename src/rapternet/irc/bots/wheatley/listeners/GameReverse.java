/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.listeners;

import rapternet.irc.bots.wheatley.objects.Game;
import rapternet.irc.bots.wheatley.objects.GameMod;
import rapternet.irc.bots.wheatley.objects.TimedWaitForQueue;
import rapternet.irc.bots.common.utils.GameUtils;
import java.io.FileNotFoundException;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import rapternet.irc.bots.wheatley.objects.games.ReverseWord;

/**
 *
 * @author Steve-O
 * Based on the C# IRC bot, CasinoBot
 * which is really unstable and breaks all the time
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
 *      !reverse
 *
 */
public class GameReverse extends ListenerAdapter {
    int time = 20;  // Seconds
    String blockedChan = "#dtella";
    int basePrize = 10; // $
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException{
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String gameChan = event.getChannel().getName();
        // keep the spammy spammy out of main, could move to XML/Global.java at some point
        if (message.equalsIgnoreCase("!reverse")&&!GameUtils.areGamesBlocked(gameChan)) {
            // get the list of words only if theres nothing in the list alread
            
            if (!GameListener.activeGame.isGameActive(gameChan, "reverse")){
                
                Game currentGame = new Game(new ReverseWord());
                
                // Get the game object index and grab the necessary variables from it
//                int currentIndex = Global.activeGame.getGameIdx(gameChan,"reverse");
                String chosenword = currentGame.getChosenWord();
                String reversed = currentGame.getSolution();
                
                event.getBot().sendIRC().message(gameChan, "You have "+time+" seconds to reverse this: " + Colors.BOLD+Colors.RED +reversed.toUpperCase() + Colors.NORMAL);
                
                // Setup the Wait For Queue
                int key=(int) (Math.random()*100000+1);
                TimedWaitForQueue timedQueue = new TimedWaitForQueue(event,time,key);
                boolean running = true;
                while (running){
                    try {
                        MessageEvent CurrentEvent = timedQueue.waitFor(MessageEvent.class);
                        String currentChan = CurrentEvent.getChannel().getName();
                        if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                            event.getBot().sendIRC().message(currentChan,"You did not guess the solution in time, the correct answer would have been "+Colors.BOLD+Colors.RED+chosenword.toUpperCase());
                            running = false;
                            timedQueue.end();
                        }
                        else if (CurrentEvent.getChannel().getName().equalsIgnoreCase(gameChan)&&!CurrentEvent.getUser().getNick().equalsIgnoreCase(event.getBot().getNick())){
                            if (CurrentEvent.getMessage().equalsIgnoreCase(chosenword)){
                                
                                int timeSpent = currentGame.getTimeSpent();
                                int prize = GameListener.scores.addScore(CurrentEvent.getUser().getNick(), basePrize+reversed.length(), reversed.length(), timeSpent, time);
                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + " entered the solution in "+timeSpent+" seconds and wins $"+prize+". Solution: " + Colors.BOLD+Colors.RED+chosenword.toUpperCase());
                                
//                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + ": You have entered the solution! Correct answer was " + Colors.BOLD+Colors.RED+chosenword.toUpperCase());
                                timedQueue.end();
                                running = false;
                            }
                            else if (CurrentEvent.getMessage().equalsIgnoreCase("!fuckthis")||(CurrentEvent.getMessage().equalsIgnoreCase("I give up"))){
                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + ": You have given up! Correct answer was " +Colors.BOLD+Colors.RED+ chosenword.toUpperCase());
                                timedQueue.end();
                                running = false;
                            }
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                GameListener.activeGame.remove(gameChan,"reverse");
            }
            else
                event.getBot().sendIRC().notice(event.getUser().getNick(),"Game Currently running in this channel");
        }
    }
}