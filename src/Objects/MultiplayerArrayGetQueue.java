/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import Wheatley.Global;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    TimedWaitForQueue
 * - Linked Classes
 *    Global
 *
 * Object:
 *      MultiplayerArrayGetQueue
 * - Specific form of the TimedWaitForQueue that returns an arraylist of people who
 *   send the commandString prefixed by the Global.commandPrefix
 *   Originally intended for getting a list of people who want to be dealt into
 *   a hand of BlackJack
 *
 * Methods:
 *     *end         - Kills the TimedWaitForQueue that lies underneath the class
 *     *getPlayers  - Gets an arraylist of players by opening a TimedWaitForQueue
 *                    that waits for the commandString prefixed by Global.commandPrefix
 *                    coming from the channel that the source event came from
 *     *setMaxSize  - Sets the maximum number of players allowed in the list, if
 *                    this isn't set by the user, there is no limit
 *
 *
 * Note: Only commands marked with a * are available for use outside the object
 */
public class MultiplayerArrayGetQueue {
    
    private ArrayList<String> players = new ArrayList<>();
    private final MessageEvent event;
    private final int time; // Stock value of 30 seconds
    private final String commandString;
    private TimedWaitForQueue queue;
    private int maxSize = -1;
    
    
    public MultiplayerArrayGetQueue(MessageEvent event,String commandString){
        this.event=event;
        this.commandString = commandString;
        this.time = 30;
    }
    
    public MultiplayerArrayGetQueue(MessageEvent event,String commandString, int time){
        this.event=event;
        this.commandString = commandString;
        this.time = time;
    }
    
    public MultiplayerArrayGetQueue(MessageEvent event,String commandString, int time, int maxSize){
        this.event=event;
        this.commandString = commandString;
        this.time = time;
        if (maxSize>0){
            this.maxSize = maxSize;
        }
        else
            throw new NullPointerException("Max size must be an integer greater than zero");
    }
    
    public void setMaxSize(int maxSize){
        this.maxSize = maxSize;
    }
    
    public void end() throws InterruptedException{
        this.queue.end();
    }
    
    public ArrayList<String> getPlayers() throws InterruptedException{
        
        boolean running = true;
        int key=(int) (Math.random()*100000+1);
        this.queue = new TimedWaitForQueue(this.event,this.time,key);
        
        while (running){
            
            MessageEvent currentEvent = this.queue.waitFor(MessageEvent.class);
            String currentMessage = Colors.removeFormattingAndColors(currentEvent.getMessage());
            String currentChan = currentEvent.getChannel().getName();
            
            if (currentMessage.equalsIgnoreCase(Integer.toString(key))){
                this.queue.close();
                running = false;
            }
            else if (currentChan.equalsIgnoreCase(this.event.getChannel().getName())){
                if (currentMessage.equalsIgnoreCase(Global.commandPrefix+this.commandString)){
                    this.players.add(currentEvent.getUser().getNick());
                    currentEvent.respond("You have been added to the playerlist");
                }
                if (maxSize!=-1){
                    if(this.players.size()>this.maxSize){
                        this.event.getBot().sendIRC().message(this.event.getChannel().getName(),"Playerlist Full");
                        this.queue.end();
//                        this.queue.close();
                        running = false;
                    }
                }
            }
        }
        return this.players;
    }
}
