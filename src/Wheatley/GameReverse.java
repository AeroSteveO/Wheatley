/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Wheatley.Game.GameArray;
import Wheatley.Game.TimedWaitForQueue;
import java.io.FileNotFoundException;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * Based on the C# IRC bot, CasinoBot
 * which is really unstable and breaks all the time
 *
 * Activate Command with:
 *      !reverse
 *
 */
public class GameReverse extends ListenerAdapter {
//    static GameArray activeGame = new GameArray();
    int time = 20;
    String blockedChan = "#dtella";
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException{
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String gameChan = event.getChannel().getName();
        // keep the spammy spammy out of main, could move to XML/Global.java at some point
        if (message.equalsIgnoreCase("!reverse")&&!Global.Channels.areGamesBlocked(gameChan)) {
            // get the list of words only if theres nothing in the list alread
            
            if (!Global.activeGame.isGameActive(gameChan, "reverse", "reverse", time)){
                //get and shuffle the word
                int currentIndex = Global.activeGame.getGameIdx(gameChan,"reverse");
                String chosenword = Global.activeGame.get(currentIndex).getChosenWord();
                String reversed = Global.activeGame.get(currentIndex).getSolution();
                event.getBot().sendIRC().message(gameChan, "You have "+time+" seconds to reverse this: " + Colors.BOLD+Colors.RED +reversed.toUpperCase() + Colors.NORMAL);
                //setup amount of given time
                int key=(int) (Math.random()*100000+1);
                TimedWaitForQueue timedQueue = Global.activeGame.getGame(gameChan, "reverse").new TimedWaitForQueue(Global.bot,time,event.getChannel(),event.getUser(),key);
                boolean running = true;
                while (running){
                    try {
                        MessageEvent CurrentEvent = timedQueue.waitFor(MessageEvent.class);
                        String currentChan = CurrentEvent.getChannel().getName();
                        
                        if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                            event.getBot().sendIRC().message(currentChan,"You did not guess the solution in time, the correct answer would have been "+chosenword.toUpperCase());
                            running = false;
                            timedQueue.end();
                        }
                        else if (CurrentEvent.getMessage().equalsIgnoreCase(chosenword)&&currentChan.equalsIgnoreCase(gameChan)){
                            event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + ": You have entered the solution! Correct answer was " + chosenword.toUpperCase());
                            timedQueue.end();
                            running = false;
                        }
                        else if ((CurrentEvent.getMessage().equalsIgnoreCase("!fuckthis")||(CurrentEvent.getMessage().equalsIgnoreCase("I give up")))&&currentChan.equals(gameChan)){
                            event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + ": You have given up! Correct answer was " + chosenword.toUpperCase());
                            timedQueue.end();
                            running = false;
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                Global.activeGame.remove(Global.activeGame.getGameIdx(gameChan,"reverse"));
            }
        }
    }
}