/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import static Wheatley.GameHangman.activeGame;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 *
 * Requested by: PiTheMathGod
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
    
    String blockedChan = "#dtella";
    static Game.GameArray activeGame = new Game.GameArray();
    
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String gameChan = event.getChannel().getName();
        int currentIndex=0;
        if (message.split(" ")[0].equalsIgnoreCase("!mastermind")&&!gameChan.equals(blockedChan)) {
            
            if (!activeGame.isGameActive(gameChan, "mastermind")){
                String[] options = message.split(" ");
                int length = 5;
                int charSize = 3;
                int lives = length * charSize;
                
                if (options.length==2){
                    length = Integer.parseInt(options[1]);
                    lives = length * charSize;
                }
                else if (options.length == 3){
                    length = Integer.parseInt(options[1]);
                    charSize = Integer.parseInt(options[2]);
                    lives = length * charSize;
                }
                else if (options.length == 4){
                    length = Integer.parseInt(options[1]);
                    charSize = Integer.parseInt(options[2]);
                    lives = Integer.parseInt(options[3]);
                }
                if (length>10)
                    length=10;
                if (charSize>10)
                    charSize=10;
                int time = 30+(charSize+length)*10;
                int scorePositionValue = 0;
                int scoreValue = 0;
                activeGame.add(new Game( gameChan, "mastermind", "int array", length, charSize, time));
                // Choose a random word from the list
                currentIndex = activeGame.getGameIdx(gameChan,"mastermind");
                
                ArrayList<Integer> solutionArray = activeGame.get(currentIndex).getIntArray();
                int solution = activeGame.get(currentIndex).getInt();
                
                boolean running=true;
                int key=(int) (Math.random()*100000+1);
                Game.TimedWaitForQueue timedQueue = activeGame.getGame(gameChan, "mastermind").new TimedWaitForQueue(Global.bot,time,event.getChannel(),event.getUser(),key);
                event.respond("Try to correctly guess a "+length+" digit code (0-"+Integer.toString(charSize-1)+")");
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
                        CurrentEvent.respond("You have given up! Correct answer was " + Integer.toString(solution));
                        running = false;
                        timedQueue.end();
                    }
                    else if (Pattern.matches("[0-9]{1,}",guess)&&currentChan.equalsIgnoreCase(gameChan)){
                        String[] temp = guess.split("(?!^)");
                        ArrayList<Integer> guessArr = new ArrayList<Integer>();
                        for (int i=0;i<temp.length;i++){
                            guessArr.add(Integer.parseInt(temp[i]));
                        }
                        for (int i = 0;i<guessArr.size()&&i<solutionArray.size();i++){
                            if (guessArr.get(i)==solutionArray.get(i))
                                scorePositionValue++;
                            
                        }
                        for (int i = 0;i<charSize;i++){
                            if (solutionArray.contains(i)&&guessArr.contains(i))
                                scoreValue++;
                        }
                        lives--;
                        if (lives <= 0){
                            CurrentEvent.respond("You've run out of lives, the solution was "+Integer.toString(solution));
                            running = false;
                            timedQueue.end();
                        }
                        else{
                            CurrentEvent.respond("Code has "+scorePositionValue+" digits in the correct place, and uses "+scoreValue+" digits | Lives left: "+lives);
                        }
                        
                        scoreValue = 0;
                        scorePositionValue = 0;
                    }
                }
                activeGame.remove(activeGame.getGameIdx(gameChan,"mastermind")); //updated current index of the game
            }
        }
    }
}
