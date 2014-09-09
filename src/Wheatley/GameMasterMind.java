/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.Game;
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
 *      !Mastermind [length] [chars] [lives]
 *
 *          Options include:
 *              Length: The number of characters in the code [int]
 *              Chars: The number of unique characters to use in the code [int]
 *              Lives: Number of guesses you can make before losing [int]
 *
 *
 *
 */
public class GameMasterMind extends ListenerAdapter {
    
    String blockedChan = "#dtella";
//    static Game.GameArray activeGame = new Game.GameArray();
    
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String gameChan = event.getChannel().getName();
        int currentIndex=0;
        if (message.split(" ")[0].equalsIgnoreCase("!mastermind")&&!Global.Channels.areGamesBlocked(gameChan)) {
            
            if (!Global.activeGame.isGameActive(gameChan, "mastermind")){
                String[] options = message.split(" ");
                int length = 5;
                int charSize = 2;
                int lives = length * charSize;
                
                if (options.length==2){
                    length = Integer.parseInt(options[1]);
                    if (length>10)
                        length=10;
                    lives = length * charSize;
                }
                else if (options.length == 3){
                    length = Integer.parseInt(options[1]);
                    if (length>10){
                        length=10;
                    }
                    charSize = Integer.parseInt(options[2]);
                    if (charSize>10){
                        charSize=10;
                    }
                    lives = length * charSize;
                }
                else if (options.length == 4){
                    length = Integer.parseInt(options[1]);
                    if (length>10)
                        length=10;
                    charSize = Integer.parseInt(options[2]);
                    if (charSize>10)
                        charSize=10;
                    lives = Integer.parseInt(options[3]);
                }
                
                
                int time = 30+(charSize+length)*10;
                int scorePositionValue = 0;
                int scoreValue = 0;
                Global.activeGame.add(new Game( gameChan, "mastermind", "int array", length, charSize, time));
                currentIndex = Global.activeGame.getGameIdx(gameChan,"mastermind");
                
                ArrayList<Integer> solutionArray = Global.activeGame.get(currentIndex).getIntArray();
                String solution = Global.activeGame.get(currentIndex).convertIntToString();
                
                boolean running=true;
                int key=(int) (Math.random()*100000+1);
                Game.TimedWaitForQueue timedQueue = Global.activeGame.getGame(gameChan, "mastermind").new TimedWaitForQueue(event,time,key);
                event.respond("Try to correctly guess a "+length+" digit code (0-"+Integer.toString(charSize-1)+")");
                //event.respond(""+Integer.toString(solutionArray.size()) + "  "+ solution);
                
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
                    else if (Pattern.matches("[0-9]{"+length+"}",guess)&&currentChan.equalsIgnoreCase(gameChan)){
                        String[] temp = guess.split("(?!^)");
                        ArrayList<Integer> guessArr = new ArrayList<Integer>();
                        for (int i=0;i<temp.length;i++){
                            guessArr.add(Integer.parseInt(temp[i]));
                        }
                        for (int i = 0;i<guessArr.size()&&i<solutionArray.size();i++){
                            if (guessArr.get(i)==solutionArray.get(i))
                                scorePositionValue++;
                        }
                        for (int i = 0;i<=charSize;i++){
                            if (solutionArray.contains(i)&&guessArr.contains(i)){
                                int solCount = characterCounter(solutionArray,i);
                                int gueCount = characterCounter(guessArr,i);
                                if(solCount>gueCount)
                                    scoreValue = scoreValue + gueCount;
                                else
                                    scoreValue = scoreValue + solCount;
                            }
                        }
                        lives--;
                        if (lives <= 0){
                            CurrentEvent.respond("You've run out of lives, the solution was "+solution);
                            running = false;
                            timedQueue.end();
                        }
                        else if (scorePositionValue == length){
                            event.getBot().sendIRC().message(gameChan,"Congratulations " + CurrentEvent.getUser().getNick() +  ", you've found the code: " + Colors.BOLD + solution + Colors.NORMAL);
                            running = false;
                            timedQueue.end();
                        }
                        else{
                            CurrentEvent.respond("Code has "+scorePositionValue+" digits correct in position and value | "+scoreValue+" digits correct in value | Lives left: "+lives);
                        }
                        
                        scoreValue = 0;
                        scorePositionValue = 0;
                    }
                }
                Global.activeGame.remove(Global.activeGame.getGameIdx(gameChan,"mastermind")); //updated current index of the game
            }
        }
    }
    public int characterCounter(ArrayList<Integer> s, int charToFind){
        int counter = 0;
        for( int i=0; i<s.size(); i++ ) {
            if( s.get(i)==(charToFind) ) {
                counter++;
            }
        }
        return counter;
    }
}
