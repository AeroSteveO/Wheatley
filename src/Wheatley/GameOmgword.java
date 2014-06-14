/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
 * Activate command with:
 *      !omgword
 *
 */
public class GameOmgword extends ListenerAdapter {
    // Initialize needed variables
    boolean isActive = false;
    String blockedChan = "#dtella";
    int time = 30;
    
    static ArrayList<Game> activeGame = new ArrayList<Game>();
    public void actionPerformed(ActionEvent e) {
    //If still loading, can't animate.

}

    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException{
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String currentChan = event.getChannel().getName();
        int currentIndex=0;
        // keep the spammy spammy out of main, could move to XML/Global.java at some point
        if (message.equalsIgnoreCase("!omgword")&&!currentChan.equals(blockedChan)) {
            if (activeGame.isEmpty()){
                activeGame.add(new Game(currentChan,"omgword","shuffle",time));
                currentIndex = 0;
            }
            else{
                for (int i=0;i<activeGame.size();i++){
                    if(activeGame.get(i).isGameRunning(currentChan,"omgword")){
                        isActive = true;
                    }
                }
                if (!isActive){
                    activeGame.add(new Game(currentChan,"omgword","shuffle",time));
                    currentIndex = activeGame.size()-1;
                }
            }
            
            if (!isActive){
                //get and shuffle the word
                currentIndex = getGameIdx(currentChan);
                String chosenword = activeGame.get(currentIndex).chosenWord;
                String scrambled = activeGame.get(currentIndex).solution;
                event.getBot().sendIRC().message(event.getChannel().getName(), "You have "+time+" seconds to solve this: " + Colors.BOLD+Colors.RED +scrambled.toUpperCase() + Colors.NORMAL);
                //setup amount of given time
                DateTime dt = new DateTime();
                DateTime end = dt.plusSeconds(time);
                WaitForQueue queue = new WaitForQueue(event.getBot());
                while (true){  //magical BS timer built into a waitforqueue, only updates upon message event
                    try {
                        MessageEvent CurrentEvent = queue.waitFor(MessageEvent.class);
                        dt = new DateTime();
                        currentIndex = getGameIdx(currentChan);
                        if (dt.isAfter(end)){
                            event.getBot().sendIRC().message(CurrentEvent.getChannel().getName(),"You did not guess the solution in time, the correct answer would have been "+chosenword.toUpperCase());
                            activeGame.remove(currentIndex);
                            queue.close();
                        }
                        else if (CurrentEvent.getMessage().equalsIgnoreCase(chosenword)&&CurrentEvent.getChannel().getName().equalsIgnoreCase(event.getChannel().getName())){
                            event.getBot().sendIRC().message(event.getChannel().getName(), CurrentEvent.getUser().getNick() + ": You have entered the solution! Correct answer was " + chosenword.toUpperCase());
                            activeGame.remove(currentIndex);
                            queue.close();
                        }
                        else if ((CurrentEvent.getMessage().equalsIgnoreCase("!fuckthis")||(CurrentEvent.getMessage().equalsIgnoreCase("I give up")))&&CurrentEvent.getChannel().getName().equals(event.getChannel().getName())){
                            event.getBot().sendIRC().message(event.getChannel().getName(), CurrentEvent.getUser().getNick() + ": You have given up! Correct answer was " + chosenword.toUpperCase());
                            activeGame.remove(currentIndex);
                            queue.close();
                        }
                    } catch (InterruptedException ex) {
                        //      activechan.remove(CurrentEvent.getChannel().getName());
                        ex.printStackTrace();
                    }
                }
            }
            else
                isActive=false;
        }
    }
    public int getGameIdx(String toCheck){
        int idx = -1;
        for(int i = 0; i < this.activeGame.size(); i++) {
            if (this.activeGame.get(i).channelName.equalsIgnoreCase(toCheck)) {
                idx = i;
                break;
            }
        }
        return (idx);
    }
}