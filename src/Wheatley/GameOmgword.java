/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;
import Objects.Game;
import Objects.TimedWaitForQueue;
import java.io.FileNotFoundException;
import org.pircbotx.Colors;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;

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
 *    ScoreArray
 *    GameList
 * - Linked Classes
 *    Global
 *    GameControl
 * 
 * Activate command with:
 *      !omgword
 *
 */
public class GameOmgword extends ListenerAdapter {
    // Initialize needed variables
//    String blockedChan = "#dtella";
    int time = 30;  // Seconds
    int basePrize = 20; // $
//    static GameArray activeGame = new GameArray();
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException{
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String gameChan = event.getChannel().getName();
//        int currentIndex=0;
        // keep the spammy spammy out of main, could move to XML/Global.java at some point
        if (message.equalsIgnoreCase("!omgword")&&!Global.channels.areGamesBlocked(gameChan)) {
            
            if (!GameControl.activeGame.isGameActive(gameChan, "omgword")){
                
                Game currentGame = new Game("shuffle");
                //get and shuffle the word
                boolean running = true;
//                currentIndex = Global.activeGame.getGameIdx(gameChan,"omgword");
                String chosenword = currentGame.getChosenWord();
                String scrambled = currentGame.getSolution();
                
                event.getBot().sendIRC().message(event.getChannel().getName(), "You have "+time+" seconds to solve this: " + Colors.BOLD+Colors.RED +scrambled.toUpperCase() + Colors.NORMAL);
                
                int key=(int) (Math.random()*100000+1);
                TimedWaitForQueue timedQueue = new TimedWaitForQueue(event,time,key);
                while (running){
                    try {
                        MessageEvent CurrentEvent = timedQueue.waitFor(MessageEvent.class);
                        if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                            event.getBot().sendIRC().message(gameChan,"You did not guess the solution in time, the correct answer would have been "+Colors.BOLD+Colors.RED+chosenword.toUpperCase());
                            running = false;
                            timedQueue.end();
                        }
                        else if (CurrentEvent.getChannel().getName().equalsIgnoreCase(gameChan)&&!CurrentEvent.getUser().getNick().equalsIgnoreCase(event.getBot().getNick())){
                            
                            if (CurrentEvent.getMessage().equalsIgnoreCase(chosenword)){
                                
                                int timeSpent = currentGame.getTimeSpent();
                                int prize = GameControl.scores.addScore(CurrentEvent.getUser().getNick(), basePrize+chosenword.length(), chosenword.length(), timeSpent, time);
                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + " entered the solution in "+timeSpent+" seconds and wins $"+prize+". Solution: " + Colors.BOLD+Colors.RED+chosenword.toUpperCase());
                                
//                                event.getBot().sendIRC().message(event.getChannel().getName(), CurrentEvent.getUser().getNick() + ": You have entered the solution! Correct answer was " +Colors.BOLD+Colors.RED+ chosenword.toUpperCase());
                                running = false;
                                timedQueue.end();
                            }
                            
                            else if ((CurrentEvent.getMessage().equalsIgnoreCase("!fuckthis")||(CurrentEvent.getMessage().equalsIgnoreCase("I give up")))&&CurrentEvent.getChannel().getName().equals(event.getChannel().getName())){
                                event.getBot().sendIRC().message(event.getChannel().getName(), CurrentEvent.getUser().getNick() + ": You have given up! Correct answer was " + Colors.BOLD+Colors.RED+chosenword.toUpperCase());
                                running = false;
                                timedQueue.end();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            GameControl.activeGame.remove(gameChan,"omgword");
        }
    }
}