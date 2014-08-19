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
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;

/**
 *
 * @author Steve-O
 * Based on the C# IRC bot, CasinoBot
 * which is really unstable and breaks all the time
 *
 * Activate command with:
 *      !omgword
 *
 */
public class GameOmgword extends ListenerAdapter {
    // Initialize needed variables
    String blockedChan = "#dtella";
    int time = 30;
    static GameArray activeGame = new GameArray();

    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException{
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String gameChan = event.getChannel().getName();
        int currentIndex=0;
        // keep the spammy spammy out of main, could move to XML/Global.java at some point
        if (message.equalsIgnoreCase("!omgword")&&!Global.Channels.areGamesBlocked(gameChan)) {
            
            if (!activeGame.isGameActive(gameChan, "omgword", "shuffle", time)){
                //get and shuffle the word
                boolean running = true;
                currentIndex = activeGame.getGameIdx(gameChan,"omgword");
                String chosenword = activeGame.get(currentIndex).getChosenWord();
                String scrambled = activeGame.get(currentIndex).getSolution();
                event.getBot().sendIRC().message(event.getChannel().getName(), "You have "+time+" seconds to solve this: " + Colors.BOLD+Colors.RED +scrambled.toUpperCase() + Colors.NORMAL);
                int key=(int) (Math.random()*100000+1);
                TimedWaitForQueue timedQueue = activeGame.getGame(gameChan,"omgword").new TimedWaitForQueue(Global.bot,time,event.getChannel(),event.getUser(),key);
                while (running){ 
                    try {
                        MessageEvent CurrentEvent = timedQueue.waitFor(MessageEvent.class);
                        if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                            event.getBot().sendIRC().message(gameChan,"You did not guess the solution in time, the correct answer would have been "+chosenword.toUpperCase());
                            running = false;
                            timedQueue.end();
                        }
                        else if (CurrentEvent.getMessage().equalsIgnoreCase(chosenword)&&CurrentEvent.getChannel().getName().equalsIgnoreCase(event.getChannel().getName())){
                            event.getBot().sendIRC().message(event.getChannel().getName(), CurrentEvent.getUser().getNick() + ": You have entered the solution! Correct answer was " + chosenword.toUpperCase());
                            running = false;
                            timedQueue.end();
                        }
                        else if ((CurrentEvent.getMessage().equalsIgnoreCase("!fuckthis")||(CurrentEvent.getMessage().equalsIgnoreCase("I give up")))&&CurrentEvent.getChannel().getName().equals(event.getChannel().getName())){
                            event.getBot().sendIRC().message(event.getChannel().getName(), CurrentEvent.getUser().getNick() + ": You have given up! Correct answer was " + chosenword.toUpperCase());
                            running = false;
                            timedQueue.end();
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                activeGame.remove(activeGame.getGameIdx(gameChan,"omgword"));
            }
        }
    }
}