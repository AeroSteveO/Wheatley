/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.TimedWaitForQueue;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 *  Original Bot: WHEATLEY
 *      a variant of the reverse function from CasinoBot, idea from Steve-O
 *
 *  Activate Command with:
 *      !altreverse
 *      esrever!
 */
public class GameAltReverse extends ListenerAdapter {
    static ArrayList<String> activechan = new ArrayList<String>();
//    static GameArray activeGame = new GameArray();
    
    boolean isactive = false;
    String blockedChan = "#dtella";
    int time = 20;  // Seconds
    int basePrize = 10; // $
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException{
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String gameChan = event.getChannel().getName();
        // keep the spammy spammy out of main, could move to XML/Global.java at some point
        if ((message.equalsIgnoreCase("!altreverse")||message.equalsIgnoreCase("esrever!"))&&!Global.channels.areGamesBlocked(gameChan)) {
            
            if (!Global.activeGame.isGameActive(gameChan, "altreverse", "reverse", time)){
                
                //get and shuffle the word
                int currentIndex = Global.activeGame.getGameIdx(gameChan,"altreverse");
                String chosenword = Global.activeGame.get(currentIndex).getChosenWord();
                String reversed = Global.activeGame.get(currentIndex).getSolution();
                boolean running = true;
                
                event.getBot().sendIRC().message(gameChan, "You have "+time+" seconds to reverse this: " + Colors.BOLD+Colors.RED +chosenword.toUpperCase() + Colors.NORMAL);
                
                // Setup the wait for queue
                int key=(int) (Math.random()*100000+1);
                TimedWaitForQueue timedQueue = new TimedWaitForQueue(event,time,key);
                while (running){
                    try {
                        MessageEvent CurrentEvent = timedQueue.waitFor(MessageEvent.class);
                        String currentChan = CurrentEvent.getChannel().getName();
                        if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))&&currentChan.equalsIgnoreCase(gameChan)){
                            event.getBot().sendIRC().message(currentChan,"You did not guess the solution in time, the correct answer would have been "+Colors.BOLD+Colors.RED+chosenword.toUpperCase());
                            running = false;
                            timedQueue.end();
                        }
                        else if (CurrentEvent.getChannel().getName().equalsIgnoreCase(gameChan)&&!CurrentEvent.getUser().getNick().equalsIgnoreCase(event.getBot().getNick())){
                            if (CurrentEvent.getMessage().equalsIgnoreCase(reversed)&&currentChan.equalsIgnoreCase(gameChan)){
                                
                                int timeSpent = Global.activeGame.get(currentIndex).getTimeSpent();
                                int prize = GameControl.scores.addScore(CurrentEvent.getUser().getNick(), basePrize+reversed.length(),reversed.length(), timeSpent, time);
                                
                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + " entered the solution in "+timeSpent+" seconds and wins $"+prize+". Solution: " + Colors.BOLD+Colors.RED+reversed.toUpperCase());
                                running = false;
                                timedQueue.end();
                            }
                            else if ((CurrentEvent.getMessage().equalsIgnoreCase("!fuckthis")||(CurrentEvent.getMessage().equalsIgnoreCase("I give up")))&&currentChan.equals(event.getChannel().getName())){
                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + ": You have given up! Correct answer was " +Colors.BOLD+Colors.RED+ reversed.toUpperCase());
                                running = false;
                                timedQueue.end();
                            }
                        }
                    } catch (Exception ex) {
                        //      activechan.remove(CurrentEvent.getChannel().getName());
                        ex.printStackTrace();
                    }
                }
                Global.activeGame.remove(Global.activeGame.getGameIdx(gameChan,"altreverse"));
            }
            else
                isactive=false;
        }
    }
}