/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.commands;

import rapternet.irc.bots.wheatley.objects.CommandMetaData;
import rapternet.irc.bots.wheatley.objects.Command;
import rapternet.irc.bots.wheatley.listeners.Global;
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
 *    CommandMetaData
 * - Utilities
 *    N/A
 * - Linked Classes
 *    N/A
 *
 * Activate Command with:
 *      !Channels
 *          Responds with a list of channels the bot is currently in
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
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        a.add(Colors.BOLD + Global.commandPrefix + "channels" + Colors.NORMAL + ": Responds with a list of the channels the bot is currently in");
        return a;
    }
    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData data = new CommandMetaData(event,true);
        String caller = data.getCaller();
        String channel = data.getEventChannel();
        String respondTo;
        
        if (channel==null)
            respondTo = caller;
        else
            respondTo = channel;
        
        boolean isVerified = data.isVerifiedBotOwner();
        
        if(!isVerified){
            event.getBot().sendIRC().notice(caller, Colors.BOLD+"Channels: "+Colors.NORMAL+"You don't have access to this command");
        }
        else{
            
            ArrayList<String> channelList = new ArrayList<>();
            ImmutableSortedSet users = event.getBot().getUserBot().getChannels();
            Iterator<Channel> iterator = users.iterator();
            String response = new String();
            
            while(iterator.hasNext()) {
                Channel element = iterator.next();
                channelList.add(element.getName());
            }
            
            for (int i=0;i<channelList.size()-1;i++){
                response+=channelList.get(i)+", ";
            }
            if (!channelList.isEmpty()) {
                response+=channelList.get(channelList.size()-1);
                event.getBot().sendIRC().message(respondTo, Colors.BOLD+"Channels: "+Colors.NORMAL+response);
            }
            else {
                event.getBot().sendIRC().message(respondTo, Colors.BOLD+"Channels: "+Colors.NORMAL+"NO CURRENTLY CONNECTED CHANNELS");
            }
        }
    }
}