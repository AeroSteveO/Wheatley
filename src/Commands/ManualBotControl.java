/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Annot.CMD;
import Annot.GenCMD;
import Objects.Command;
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
        a.add("kick");
        a.add("ban");
        return a;
    }
    
    @Override
    public void processCommand(Event event){
        
        String message = new String();
        String caller = new String();
        String channel = null;
        String responseChan = null;
        boolean isVerified = false;
        
        // START EVENT SPECIFIC PARSING
        if (event instanceof MessageEvent){ // MESSAGE EVENT SPECIFIC PARSING
            MessageEvent mEvent = (MessageEvent) event;
            message = Colors.removeFormattingAndColors(mEvent.getMessage());
            caller = mEvent.getUser().getNick();
            channel = mEvent.getChannel().getName();
            responseChan = channel;
            
//            if (message.split(" ").length==5){
                isVerified=(caller.equalsIgnoreCase(Global.botOwner))&&mEvent.getUser().isVerified();
//            }
//            else if (message.split(" ").length==4){
//                isVerified=((caller.equalsIgnoreCase(Global.botOwner))&&mEvent.getUser().isVerified());
//            }
        }// END MESSAGE EVENT SPECIFIC PARSING
        else if (event instanceof PrivateMessageEvent){ // PRIVATE MESSAGE EVENT SPECIFIC PARSING
            PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
            message = Colors.removeFormattingAndColors(pmEvent.getMessage());
            caller = pmEvent.getUser().getNick();
            
//            if (message.split(" ").length==5)
                isVerified=(caller.equalsIgnoreCase(Global.botOwner))&&pmEvent.getUser().isVerified();
        }// END PRIVATE MESSAGE EVENT SPECIFIC PARSING
        else{
            return;
        }
        // END EVENT SPECIFIC PARSING
        
        // COMMAND SPLITTING
        String command = message.split(Global.commandPrefix)[1];
        String[] cmdSplit = command.split(" ");
        if(isVerified){
            if (cmdSplit[0].equalsIgnoreCase("say")){
                
                if (cmdSplit.length>2&&cmdSplit[1].contains("#")){
                    String chan = cmdSplit[1];
                    if (!chan.startsWith("#")&&channel!=null)
                        chan = channel;
                    if (chan.contains("#")){
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
            if (cmdSplit[0].equalsIgnoreCase("act")){
                if (cmdSplit.length>2&&cmdSplit[1].contains("#")){
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
