/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Annot.CMD;
import Annot.GenCMD;
import Objects.Command;
import Objects.CommandMetaData;
import Wheatley.Global;
import java.util.ArrayList;
import org.pircbotx.Channel;
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
 * - Linked Classes
 *    Global
 * 
 * ADMIN COMMANDS
 * Activate Commands with:
 *      !act [action]
 *          Repeats the given action in the channel the command was run from
 *      !act [#channel] [action]
 *          Repeats the given action in the input channel
 *      !say [message]
 *          Repeats the given message in the channel the command was run from
 *      !say [#channel] [message]
 *          Repeats the given message in the input channel
 * 
 */
@GenCMD
@CMD
public class ManualBotControl implements Command{
    
    @Override
    public String toString(){
        return("Manually control the bots output");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("say");
        a.add("act");
//        a.add("kick");
//        a.add("ban");
        return a;
    }
    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData commandData = new CommandMetaData(event, true);
        
        String message = commandData.getMessage();
        String caller = commandData.getCaller();
        String channel = commandData.getEventChannel();
        boolean isVerified = commandData.isVerifiedBotOwner();
        String[] cmdSplit = commandData.getCommandSplit();
        
        
        if(isVerified){
            if (cmdSplit[0].equalsIgnoreCase("say")){
                if (cmdSplit.length>2&&cmdSplit[1].startsWith("#")){
                    String chan = cmdSplit[1];
                    if (!chan.startsWith("#")&&channel!=null)
                        chan = channel;
                    if (chan.startsWith("#")){
//                        System.out.println(chan);
                        Channel c = event.getBot().getUserChannelDao().getChannel(chan);
                        if(event.getBot().getUserBot().getChannels().contains(c)){
                            
                            String msg = message.split(" ",3)[2];
                            event.getBot().sendIRC().message(chan, msg);
                        }
                        else{
                            event.getBot().sendIRC().notice(caller, "Bot not in this channel");
                        }
                    }
                    else{
                        event.getBot().sendIRC().notice(caller, "Improperly formed channel string");
                    }
                }
                else{
                    String msg = message.split(" ",2)[1];
                    if (channel!=null)
                        event.getBot().sendIRC().message(channel, msg);
                    else
                        event.getBot().sendIRC().message(caller, msg);
                }
            }
//        }
            else if (cmdSplit[0].equalsIgnoreCase("act")){
                if (cmdSplit.length>2&&cmdSplit[1].startsWith("#")){
                    String chan = cmdSplit[1];
                    if (!chan.startsWith("#")&&channel!=null)
                        chan = channel;
                    if (chan.contains("#")){
//                        System.out.println(chan);
                        Channel c = event.getBot().getUserChannelDao().getChannel(chan);
                        if(event.getBot().getUserBot().getChannels().contains(c)){
                            
                            String msg = message.split(" ",3)[2];
                            event.getBot().sendIRC().action(chan, msg);
                        }
                        else{
                            event.getBot().sendIRC().notice(caller, "Bot not in this channel");
                        }
                    }
                    else{
                        event.getBot().sendIRC().notice(caller, "Improperly formed channel string");
                    }
                }
                else{
                    String msg = message.split(" ",2)[1];
                    if (channel!=null)
                        event.getBot().sendIRC().action(channel, msg);
                    else
                        event.getBot().sendIRC().action(caller, msg);
                }
            }
        }
        else{
            event.getBot().sendIRC().notice(caller, "You do not have access to this command");
        }
    }
}
