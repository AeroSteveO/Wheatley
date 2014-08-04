/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import static Wheatley.GameHangman.activeGame;
import java.io.FileNotFoundException;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * 
 * 
 * Activate Command with:
 *      !Mastermind [length] [chars]
 * 
 *          Options include:
 *              Length: the number of characters in the code [int]
 *              Chars: the number of unique characters to use in the code [int]
 *              
 * 
 * 
 */
public class GameMasterMind extends ListenerAdapter {
    
    int baselives = 10;
    int changed = 0;
    int lives = baselives;
    int time = 120;
    
    String blockedChan = "#dtella";
    static Game.GameArray activeGame = new Game.GameArray();
    
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String gameChan = event.getChannel().getName();
        int currentIndex=0;
        
        if (message.split(" ")[0].equalsIgnoreCase("!mastermind")&&!gameChan.equals(blockedChan)) {
            if (!activeGame.isGameActive(gameChan, "mastermind", "blank", time)){
                String[] options = message.split(" ");
                int length = 5;
                int charSize = 3;
                if (options.length==1)
                    length = Integer.parseInt(options[1]);
                else if (options.length == 3){
                    length = Integer.parseInt(options[1]);
                    charSize = Integer.parseInt(options[2]);
                }
                
                // Choose a random word from the list
                currentIndex = activeGame.getGameIdx(gameChan,"hangman");
                boolean running=true;
                int key=(int) (Math.random()*100000+1);
                Game.TimedWaitForQueue timedQueue = activeGame.getGame(gameChan, "hangman").new TimedWaitForQueue(Global.bot,time,event.getChannel(),event.getUser(),key);
                
                while (running){
                    MessageEvent CurrentEvent = timedQueue.waitFor(MessageEvent.class);
                    String currentChan = CurrentEvent.getChannel().getName();
                    changed = 0;
                    if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                        event.getBot().sendIRC().message(gameChan,"Game over! You've run out of time. "+Colors.BOLD + "solution" + Colors.NORMAL + " would have been the solution.");
                        running = false;
                        timedQueue.end();
                    }
                }
            }
        }
    }
}
