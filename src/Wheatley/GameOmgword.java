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
import org.joda.time.*;
import org.pircbotx.Colors;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;

/**
 *
 * @author Steve-O
 * Based on the C# IRC bot, CasinoBot
 * which is really unstable and breaks all the time
 *
 */
public class GameOmgword extends ListenerAdapter {
    // Initialize needed variables
    static ArrayList<String> wordls = null;
    static ArrayList<String> activechan = new ArrayList<String>();
    boolean isactive = false;
    int time = 30;
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException{
        String message = Colors.removeFormattingAndColors(event.getMessage());
        // keep the spammy spammy out of main, could move to XML/Global.java at some point
        if (message.equalsIgnoreCase("!omgword")&&!event.getChannel().getName().equals("#dtella")) {
            // get the list of words only if theres nothing in the list alread
            if (wordls == null) {
                wordls = getWordList();
            }
            // check if the current active channel list is empty
            if (activechan.isEmpty()){
                activechan.add(event.getChannel().getName());
            }
            else{ //if its not empty, check if the channel calling the function is already active
                for (int i=0;i<activechan.size();i++){
                    if (activechan.get(i).equals(event.getChannel().getName())){
                        isactive = true;
                    }
                }
                if (!isactive) { //if its not active, add it to the active channel list, and start the game
                    activechan.add(event.getChannel().getName());
                }
            }
            if (!isactive){
                //get and shuffle the word
                String chosenword = wordls.get((int) (Math.random()*wordls.size()-1));
                String scrambled = shuffle(chosenword);
                event.getBot().sendIRC().message(event.getChannel().getName(), "You have 30 seconds to solve this: " + Colors.BOLD+Colors.RED +scrambled.toUpperCase() + Colors.NORMAL);
                //setup amount of given time
                DateTime dt = new DateTime();
                DateTime end = dt.plusSeconds(time);
                WaitForQueue queue = new WaitForQueue(event.getBot());
                while (true){  //magical BS timer built into a waitforqueue, only updates upon message event
                    try {
                        MessageEvent CurrentEvent = queue.waitFor(MessageEvent.class);
                        dt = new DateTime();
                        if (dt.isAfter(end)){
                            event.getBot().sendIRC().message(CurrentEvent.getChannel().getName(),"You did not guess the solution in time, the correct answer would have been "+chosenword.toUpperCase());
                            activechan.remove(CurrentEvent.getChannel().getName());
                            queue.close();
                        }
                        else if (CurrentEvent.getMessage().equalsIgnoreCase(chosenword)&&CurrentEvent.getChannel().getName().equals(event.getChannel().getName())){
                            event.getBot().sendIRC().message(event.getChannel().getName(), CurrentEvent.getUser().getNick() + ": You have entered the solution! Correct answer was " + chosenword.toUpperCase());
                            activechan.remove(CurrentEvent.getChannel().getName());
                            queue.close();
                        }
                        else if ((CurrentEvent.getMessage().equalsIgnoreCase("!fuckthis")||(CurrentEvent.getMessage().equalsIgnoreCase("I give up")))&&CurrentEvent.getChannel().getName().equals(event.getChannel().getName())){
                            event.getBot().sendIRC().message(event.getChannel().getName(), CurrentEvent.getUser().getNick() + ": You have given up! Correct answer was " + chosenword.toUpperCase());
                            activechan.remove(CurrentEvent.getChannel().getName());
                            queue.close();
                        }
                    } catch (InterruptedException ex) {
                        //      activechan.remove(CurrentEvent.getChannel().getName());
                        ex.printStackTrace();
                    }
                }
            }
            else
                isactive=false;
        }
    }
    // Stupid freaking warning is wrong, breaks code when implemented
    @SuppressWarnings("SizeReplaceableByIsEmpty")
    // Shuffle up the chosen word string
    public static String shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return(output.toString());
    }
    // Grabs the wordlist and loads into variable
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
            ex.printStackTrace();
            return null;
        }
    }
}