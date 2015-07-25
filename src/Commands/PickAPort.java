/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Commands;

import Objects.Command;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

/**
 *
 * @author Stephen
 * 
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    Command
 * - Utilities
 *    N/A
 * - Linked Classes
 *    N/A
 * 
 * Activate Command with:
 *      !PickAPort
 *          Responds with a valid port number for use in a program
 * 
 */

public class PickAPort implements Command{
    
    @Override
    public String toString(){
        return("PICKAPORT");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("pickaport");
        return a;
    }
    
    @Override
    public void processCommand(Event event){
        if (event instanceof MessageEvent){
            MessageEvent mEvent = (MessageEvent) event;
            mEvent.getBot().sendIRC().message(mEvent.getChannel().getName(),Colors.BOLD+"Port Number: "+Colors.NORMAL+(int) (1025+(Math.random()*(65534-1025+1))));
        }
        else   if (event instanceof PrivateMessageEvent){
            PrivateMessageEvent mEvent = (PrivateMessageEvent) event;
            mEvent.getBot().sendIRC().message(mEvent.getUser().getNick(),Colors.BOLD+"Port Number: "+Colors.NORMAL+(int) (1025+(Math.random()*(65534-1025+1))));
        }
    }
}
