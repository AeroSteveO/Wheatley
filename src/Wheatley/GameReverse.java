/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.WaitForQueue;
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
    static ArrayList<String> wordls = null;
    static ArrayList<String> activechan = new ArrayList<String>();
    boolean isactive = false;
    int time = 20;
    String blockedChan = "#dtella";
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException{
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String currentChan = event.getChannel().getName();
        // keep the spammy spammy out of main, could move to XML/Global.java at some point
        if (message.equalsIgnoreCase("!reverse")&&!event.getChannel().getName().equals(blockedChan)) {
            // get the list of words only if theres nothing in the list alread
            if (wordls == null) {
                wordls = getWordList();
            }
            // check if the current active channel list is empty
            if (activechan.isEmpty()){
                activechan.add(currentChan);
            }
            else{ //if its not empty, check if the channel calling the function is already active
                for (int i=0;i<activechan.size();i++){
                    if (activechan.get(i).equals(currentChan)){
                        isactive = true;
                    }
                }
                if (!isactive) { //if its not active, add it to the active channel list, and start the game
                    activechan.add(currentChan);
                }
            }
            if (!isactive){
                //get and shuffle the word
                String chosenword = wordls.get((int) (Math.random()*wordls.size()-1));
                String reversed = reverse(chosenword);
                event.getBot().sendIRC().message(currentChan, "You have 30 seconds to reverse this: " + Colors.BOLD+Colors.RED +reversed.toUpperCase() + Colors.NORMAL);
                //setup amount of given time
                DateTime dt = new DateTime();
                DateTime end = dt.plusSeconds(time);
                WaitForQueue queue = new WaitForQueue(event.getBot());
                boolean running = true;
                while (running){  //magical BS timer built into a waitforqueue, only updates upon message event
                    try {
                        MessageEvent CurrentEvent = queue.waitFor(MessageEvent.class);
                        currentChan = CurrentEvent.getChannel().getName();
                        dt = new DateTime();
                        if (dt.isAfter(end)){
                            event.getBot().sendIRC().message(currentChan,"You did not guess the solution in time, the correct answer would have been "+chosenword.toUpperCase());
                            queue.close();
                            running = false;
                        }
                        else if (CurrentEvent.getMessage().equalsIgnoreCase(chosenword)&&currentChan.equalsIgnoreCase(event.getChannel().getName())){
                            event.getBot().sendIRC().message(event.getChannel().getName(), CurrentEvent.getUser().getNick() + ": You have entered the solution! Correct answer was " + chosenword.toUpperCase());
                            queue.close();
                            running = false;
                        }
                        else if ((CurrentEvent.getMessage().equalsIgnoreCase("!fuckthis")||(CurrentEvent.getMessage().equalsIgnoreCase("I give up")))&&currentChan.equals(event.getChannel().getName())){
                            event.getBot().sendIRC().message(event.getChannel().getName(), CurrentEvent.getUser().getNick() + ": You have given up! Correct answer was " + chosenword.toUpperCase());
                            queue.close();
                            running = false;
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GameOmgword.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                activechan.remove(event.getChannel().getName());
            }
            else
                isactive=false;
        }
    }
    public ArrayList<String> getWordList() throws FileNotFoundException{
        try{
            Scanner wordfile = new Scanner(new File("wordlist.txt"));
            ArrayList<String> wordls = new ArrayList<String>();
            while (wordfile.hasNext()){
                wordls.add(wordfile.next());
            }
            wordfile.close();
            return (wordls);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameOmgword.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
       public static String reverse(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        for(int i=characters.size();i>0;i--){
            output.append(characters.get(i-1));
        }
        return(output.toString());
    }
}