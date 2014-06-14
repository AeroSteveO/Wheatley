/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 * Based on the C# IRC bot, CasinoBot
 * which is generally unstable and requires windows to run
 *
 * Activate Command with:
 *      !hangman
 */
public class GameHangman extends ListenerAdapter {
    // Woohooo basic variables for junk
    int baselives = 10;
    int changed = 0;
    int lives = baselives;
    int time = 60;
    ArrayList<String> wordls = null;
    static ArrayList<String> activechan = new ArrayList<String>();
    boolean isactive = false;
    String blockedChan = "#dtella";
    int correct = 0;
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        {
            String message = Colors.removeFormattingAndColors(event.getMessage());
            String gameChan = event.getChannel().getName();
            if (message.equalsIgnoreCase("!hangman")&&!gameChan.equals(blockedChan)) {
                if (wordls == null) {
                    wordls = getWordList();
                }
                if (activechan.isEmpty()){
                    activechan.add(gameChan);
                }
                else{ //if its not empty, check if the channel calling the function is already active
                    for (int i=0;i<activechan.size();i++){
                        if (activechan.get(i).equals(gameChan)){
                            isactive = true;
                        }
                    }
                    if (!isactive) { //if its not active, add it to the active channel list, and start the game
                        activechan.add(gameChan);
                    }
                }
                if (!isactive){
                    // Choose a random word from the list
                    String chosenword = wordls.get((int) (Math.random()*wordls.size()-1));
                    char[] characters = chosenword.toCharArray();
                    // Make a variable of all blanks to use
                    String guess = MakeBlank(chosenword);
                    event.getBot().sendIRC().message(gameChan, "You have "+time+" seconds to find the following word: " + Colors.BOLD + guess + Colors.NORMAL);
                    boolean running=true;
                    int key=(int) (Math.random()*100000+1);
                    TimedWaitForQueue timedQueue = new TimedWaitForQueue(Global.bot,time,event.getChannel(),event.getUser(),key);
                    while (running){
                        MessageEvent CurrentEvent = timedQueue.waitFor(MessageEvent.class);
                        String currentChan = CurrentEvent.getChannel().getName();
                        changed = 0;
                        if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                            event.getBot().sendIRC().message(gameChan,"Game over! "+Colors.BOLD + chosenword.toUpperCase() + Colors.NORMAL + " would have been the solution.");
                            running = false;
                            timedQueue.close();
                        }
                        else if (Pattern.matches("[a-zA-Z]{1}", CurrentEvent.getMessage())&&currentChan.equalsIgnoreCase(gameChan)){
                            for (int i = 0; i<chosenword.length(); i++){
                                if (Character.toString(characters[i]).equalsIgnoreCase(CurrentEvent.getMessage())){
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
                                if(lives == 0){
                                    event.getBot().sendIRC().message(gameChan, "You've run out of lives! The word we looked for was " + Colors.BOLD + Colors.RED + chosenword.toUpperCase() + Colors.NORMAL);
                                    running = false;
                                    timedQueue.close();
                                }
                            }
                            else if (correct == chosenword.length()){
                                event.getBot().sendIRC().message(gameChan,"Congratulations " + CurrentEvent.getUser().getNick() +  ", you've found the word: " + Colors.BOLD + chosenword.toUpperCase() + Colors.NORMAL);
                                running = false;
                                timedQueue.close();
                            }
                        }
                        else if ((CurrentEvent.getMessage().equals("!fuckthis")||(CurrentEvent.getMessage().equalsIgnoreCase("I give up")))&&currentChan.equals(gameChan)){
                            CurrentEvent.respond("You have given up! Correct answer was " + chosenword.toUpperCase());
                            running = false;
                            timedQueue.close();
                        }
                    }
                    correct = 0;
                    changed = 0;
                    lives = baselives;
                    activechan.remove(gameChan);
                }
                else
                    isactive=false;
            }
        }
    }
    public static String MakeBlank(String input){
        String blanks = new String();
        for (int i = 0; i<input.length(); i++){
            blanks = blanks + "_";
        }
        return(blanks);
    }
    public ArrayList<String> getWordList() throws FileNotFoundException{
        try{
            Scanner wordfile = new Scanner(new File("wordlist.txt"));
            ArrayList<String> wordList = new ArrayList<String>();
            while (wordfile.hasNext()){
                wordList.add(wordfile.next());
            }
            wordfile.close();
            return (wordList);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public class TimedWaitForQueue extends WaitForQueue{
        int time;
        public TimedWaitForQueue(PircBotX bot,int time, Channel chan,User user, int key) throws InterruptedException {
            super(bot);
            this.time=time;
            Thread.sleep(this.time*1000);
            bot.getConfiguration().getListenerManager().dispatchEvent(new MessageEvent(Global.bot,chan,user,Integer.toString(key)));
        }
        
    }
}