/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.Game;
import Objects.TimedWaitForQueue;
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
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    TimedWaitForQueue
 *    Game
 * - Linked Classes
 *    Global
 *    GameControl
 *
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
    int basePrize = 40; // $
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String gameChan = event.getChannel().getName();
        
        if (message.split(" ")[0].equalsIgnoreCase("!mastermind")&&!Global.channels.areGamesBlocked(gameChan)) {
            
            if (!GameControl.activeGame.contains(new String[] {gameChan, "mastermind", "long"})){
                
                String[] options = message.split(" ");
                int length = 5;
                int charSize = 2;
                int lives = length * charSize;
                
                {
                    int i=1;
                    while (i<options.length){
                        if (!options[i].matches("[0-9]+")){
                            event.getBot().sendIRC().notice(event.getUser().getNick(),"You must input an integer");
                            return;
                        }
                        else if (options[i].matches("[0]+")){
                            event.getBot().sendIRC().notice(event.getUser().getNick(),"You must input a non-zero integer");
                            return;
                        }
                        i++;
                        
                    }
                    if (options.length>4){
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"This command takes 3 integer inputs maximum");
                        return;
                    }
                }
                
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
                
                GameControl.activeGame.add(gameChan, "mastermind", "long");//Lets add the game to the array well after the input checks
                int time = 30+(charSize+length)*10;
                int scorePositionValue = 0;
                int scoreValue = 0;
                Game currentGame = new Game("int array", length, charSize);
//                currentIndex = Global.activeGame.getGameIdx(gameChan,"mastermind");
                
                ArrayList<Integer> solutionArray = currentGame.getIntArray();
                String solution = currentGame.convertIntToString();
                
                boolean running=true;
                int key=(int) (Math.random()*100000+1);
                TimedWaitForQueue timedQueue = new TimedWaitForQueue(event,time,key);
                event.respond("Try to correctly guess a "+length+" digit code (0-"+Integer.toString(charSize-1)+")");
                
                while (running){
                    MessageEvent CurrentEvent = timedQueue.waitFor(MessageEvent.class);
                    String guess = CurrentEvent.getMessage();
                    if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                        event.getBot().sendIRC().message(gameChan,"Game over! You've run out of time. "+Colors.BOLD+Colors.RED + solution + Colors.NORMAL + " would have been the solution.");
                        running = false;
                        timedQueue.end();
                    }
                    else if (CurrentEvent.getChannel().getName().equalsIgnoreCase(gameChan)&&!CurrentEvent.getUser().getNick().equalsIgnoreCase(event.getBot().getNick())){
                        if ((CurrentEvent.getMessage().equals("!fuckthis")||(CurrentEvent.getMessage().equalsIgnoreCase("I give up")))){
                            CurrentEvent.respond("You have given up! Correct answer was "+Colors.BOLD+Colors.RED + solution);
                            running = false;
                            timedQueue.end();
                        }
                        else if (Pattern.matches("[0-9]{"+length+"}",guess)){
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
                            
                            if (scorePositionValue == length){
                                
                                int timeSpent = currentGame.getTimeSpent();
                                int prize = GameControl.scores.addScore(CurrentEvent.getUser().getNick(), basePrize+length+charSize+lives,length, timeSpent, time);
                                event.getBot().sendIRC().message(gameChan, CurrentEvent.getUser().getNick() + " entered the code in "+timeSpent+" seconds and wins $"+prize+". Code: " + Colors.BOLD+Colors.RED+solution.toUpperCase());
                                
//                                event.getBot().sendIRC().message(gameChan,"Congratulations " + CurrentEvent.getUser().getNick() +  ", you've found the code: " + Colors.BOLD +Colors.RED+ solution + Colors.NORMAL);
                                running = false;
                                timedQueue.end();
                            }
                            
                            else{
                                lives--;
                                CurrentEvent.getBot().sendIRC().message(gameChan,"Code has "+scorePositionValue+" digits correct in position and value | "+scoreValue+" digits correct in value | Lives left: "+lives);
                            }
                            
                            if (lives <= 0){
                                CurrentEvent.getBot().sendIRC().message(gameChan,"You've run out of lives, the solution was "+solution);
                                running = false;
                                timedQueue.end();
                            }
                            
                            scoreValue = 0;
                            scorePositionValue = 0;
                        }
                    }
                }
                GameControl.activeGame.remove(gameChan,"mastermind"); //updated current index of the game
            }
            else
                event.getBot().sendIRC().notice(event.getUser().getNick(),"Game Currently running in this channel");
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
