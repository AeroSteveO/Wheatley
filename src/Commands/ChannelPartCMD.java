/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Objects.Command;
import Objects.CommandMetaData;
import Wheatley.Global;
import java.util.ArrayList;
import org.pircbotx.Channel;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 */
public class ChannelPartCMD implements Command {
    
    @Override
    public String toString() {
        return("Some text about the command that will never be used");
    }
    
    @Override
    public boolean isCommand(String toCheck) {
        return toCheck.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", leave"); // Phrase that when spoken will activate the command
        
    }
    
    @Override
    public ArrayList<String> commandTerms() {
        ArrayList<String> a = new ArrayList<>();
//        a.add("part");
        return a;
    }
    
    @Override
    public void processCommand(Event event) {
        
        CommandMetaData data = new CommandMetaData(event, true);
        
        String caller = data.getCaller(); // Nick of the user who called the command
        boolean isVerified = data.isVerifiedBotOwner(); // True if the user who called the command is the bot owner
        boolean isVerifiedChanOwner = data.isVerifiedChanBotOwner();
        String[] cmdSplit = data.getCommandSplit();
        String message = data.getMessage();
        String respondTo = data.respondToCallerOrMessageChan();
        String channel = data.getCommandChannel();
        
        if (isVerified) {
            if (message.contains("#")) {
                String[] chan = message.split("#");
                if (!event.getBot().getUserChannelDao().containsChannel("#" + chan[1])) {
                    event.respond("Part: Bot currently not in channel");
                    return;
                }
                
                
                Channel c = event.getBot().getUserChannelDao().getChannel("#"+chan[1]);
                if (!event.getBot().getUserBot().getChannels().contains(c)) {
                    event.respond("Not in that channel!");
                }
                else {
                    c.send().part();
                    event.respond("Left #" + chan[1] + ".");
                    Global.channels.remove(Global.channels.indexOf("#"+chan[1].toLowerCase()));
                }
            }
            else if (channel != null) {
                            Channel c = event.getBot().getUserChannelDao().getChannel(channel);
            c.send().part("GoodBye");
            event.getBot().sendRaw().rawLine("PART " + channel + " GoodBye");
//                event.getChannel().send().part("Goodbye");
            Global.channels.remove(Global.channels.indexOf(channel.toLowerCase()));

            }
        } // command the bot to part the current channel that the command was sent from
        else if (isVerifiedChanOwner){//||message.equalsIgnoreCase("!part"))){
//                event.getBot().sendRaw("PART " + channel + " GoodBye");
            Channel c = event.getBot().getUserChannelDao().getChannel(channel);
            c.send().part("GoodBye");
//            event.getBot().sendRaw().rawLine("PART " + channel + " GoodBye");
//                event.getChannel().send().part("Goodbye");
            Global.channels.remove(Global.channels.indexOf(channel.toLowerCase()));
        }
        
        
        
    }
}