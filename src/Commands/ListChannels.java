/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Objects.Command;
import Wheatley.Global;
import com.google.common.collect.ImmutableSortedSet;
import java.util.ArrayList;
import java.util.Iterator;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

/**
 *
 * @author Stephen
 */
public class ListChannels implements Command {
    
    @Override
    public String toString(){
        return("CHANNELS");
    }
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("channels");
        return a;
    }
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
    @Override
    public void processCommand(Event event){
        
        String message = new String();
        String caller = new String();
        String channel = null;
        String respondTo = null;
        boolean isVerified = false;
        
        // START EVENT SPECIFIC PARSING
        if (event instanceof MessageEvent){ // MESSAGE EVENT SPECIFIC PARSING
            MessageEvent mEvent = (MessageEvent) event;
            message = Colors.removeFormattingAndColors(mEvent.getMessage());
            caller = mEvent.getUser().getNick();
            channel = mEvent.getChannel().getName();
            respondTo = channel;
            
            isVerified=(caller.equalsIgnoreCase(Global.botOwner)&&mEvent.getUser().isVerified());
        }// END MESSAGE EVENT SPECIFIC PARSING
        else if (event instanceof PrivateMessageEvent){ // PRIVATE MESSAGE EVENT SPECIFIC PARSING
            PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
            message = Colors.removeFormattingAndColors(pmEvent.getMessage());
            caller = pmEvent.getUser().getNick();
            respondTo = caller;
            isVerified=(caller.equalsIgnoreCase(Global.botOwner)&&pmEvent.getUser().isVerified());
        }// END PRIVATE MESSAGE EVENT SPECIFIC PARSING
        else{
            return;
        }
        // END EVENT SPECIFIC PARSING
        
        if(!isVerified){
            event.getBot().sendIRC().notice(caller, Colors.BOLD+"Channels: "+Colors.NORMAL+"You don't have access to this command");
        }
        else{
            
            ArrayList<String> channelList = new ArrayList<>();
            ImmutableSortedSet users = event.getBot().getUserBot().getChannels();
            Iterator<Channel> iterator = users.iterator();
            boolean modified = false;
            String response = new String();
            
            while(iterator.hasNext()) {
                Channel element = iterator.next();
                channelList.add(element.getName());
            }
            
            for (int i=0;i<channelList.size()-1;i++){
                response+=channelList.get(i)+", ";
            }
            
            response+=channelList.get(channelList.size()-1);
            event.getBot().sendIRC().message(respondTo, Colors.BOLD+"Channels: "+Colors.NORMAL+response);
        }
    }
}
