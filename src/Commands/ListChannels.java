/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Objects.CommandMetaData;
import Objects.Command;
import com.google.common.collect.ImmutableSortedSet;
import java.util.ArrayList;
import java.util.Iterator;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

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
        
        CommandMetaData data = new CommandMetaData(event,true);
        String message = data.getMessage();
        String caller = data.getCaller();
        String channel = data.getEventChannel();
        String respondTo = new String();
        
        if (channel==null)
            respondTo = caller;
        else
            respondTo = channel;
        
        boolean isVerified = data.isVerifiedBotOwner();
        
        // START EVENT SPECIFIC PARSING
//        if (event instanceof MessageEvent){ // MESSAGE EVENT SPECIFIC PARSING
//            MessageEvent mEvent = (MessageEvent) event;
//            message = Colors.removeFormattingAndColors(mEvent.getMessage());
//            caller = mEvent.getUser().getNick();
//            channel = mEvent.getChannel().getName();
//            respondTo = channel;
        
//            isVerified=(caller.equalsIgnoreCase(Global.botOwner)&&mEvent.getUser().isVerified());
//        }// END MESSAGE EVENT SPECIFIC PARSING
//        else if (event instanceof PrivateMessageEvent){ // PRIVATE MESSAGE EVENT SPECIFIC PARSING
//            PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
//            message = Colors.removeFormattingAndColors(pmEvent.getMessage());
//            caller = pmEvent.getUser().getNick();
//            respondTo = caller;
//            isVerified=(caller.equalsIgnoreCase(Global.botOwner)&&pmEvent.getUser().isVerified());
//        }// END PRIVATE MESSAGE EVENT SPECIFIC PARSING
//        else{
//            return;
//        }
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
