/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import java.util.ArrayList;
import org.pircbotx.hooks.Event;
import Annot.CMD;
import Annot.GenCMD;
import Objects.Command;
import Wheatley.Global;
import org.pircbotx.Colors;
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
 */
@CMD
@GenCMD

public class ThrottleCMD implements Command {
    
    @Override
    public String toString(){
        return("THROTTLE");
    }
    
    @Override
    public void processCommand(Event event){
        
        String message = new String();
        String caller = new String();
        String channel = null;
        String responseChan = null;
        boolean isVerified = false;
        
        if (event instanceof MessageEvent){
            MessageEvent mEvent = (MessageEvent) event;
            message = Colors.removeFormattingAndColors(mEvent.getMessage());
            caller = mEvent.getUser().getNick();
            channel = mEvent.getChannel().getName();
            responseChan = channel;
            
            if (message.split(" ").length==5){
                isVerified=(caller.equalsIgnoreCase(Global.botOwner)||mEvent.getUser().getChannelsOwnerIn().contains(mEvent.getBot().getUserChannelDao().getChannel("#" + message.split("\\#")[1].split(" ")[0])))&&mEvent.getUser().isVerified();
            }
            else if (message.split(" ").length==4){
                isVerified=((mEvent.getChannel().isOp(mEvent.getUser())||caller.equalsIgnoreCase(Global.botOwner))&&mEvent.getUser().isVerified());
            }
        }
        else if (event instanceof PrivateMessageEvent){
            PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
            message = Colors.removeFormattingAndColors(pmEvent.getMessage());
            caller = pmEvent.getUser().getNick();
            
            if (message.split(" ").length==5)
                isVerified=(pmEvent.getUser().isVerified()&&(caller.equalsIgnoreCase(Global.botOwner))||pmEvent.getUser().getChannelsOwnerIn().contains(pmEvent.getBot().getUserChannelDao().getChannel("#" + message.split("\\#")[1].split(" ")[0])));
        }
        else{
            return;
        }
        
        String command = message.split(Global.commandPrefix)[1];
        String[] cmdSplit = command.split(" ");
        String type = null;
        String modifier = null;
        
        if (cmdSplit.length>5){
            event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"Too many inputs");
            return;
        }
        
        if (cmdSplit[1].equalsIgnoreCase("list")){
            
            ArrayList<String> types = Global.throttle.getAllTypes();
            String response = Colors.BOLD+"Throttle Types: "+Colors.NORMAL;
            
            for (int i=0;i<types.size()-1;i++){
                response+=types.get(i)+", ";
            }
            response+=types.get(types.size()-1);
            
            event.getBot().sendIRC().notice(caller, response );
            return;
        }
        
        int throttleModification = Integer.MIN_VALUE;
        
        for (int i=0;i<cmdSplit.length;i++){
            if (cmdSplit[i].startsWith("#"))
                channel = cmdSplit[i];
            else if (cmdSplit[i].equalsIgnoreCase("log")||cmdSplit[i].equalsIgnoreCase("time"))
                modifier = cmdSplit[i];
            else if (cmdSplit[i].matches("[0-9]+"))
                throttleModification = Integer.parseInt(cmdSplit[i]);
            else
                type = cmdSplit[i];
        }
        
        
        if (cmdSplit.length==1){
            event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"Not enough inputs, requires [type] [channel] minimum");
        }
        
        else if (throttleModification==Integer.MIN_VALUE&&!type.equalsIgnoreCase(null)){
            
            if (!channel.equalsIgnoreCase(null)){
                if (Global.throttle.contains(type+"log",channel)){
                    String calls = Global.throttle.get(type+"log",channel);
                    String time = Global.throttle.get(type+"time",channel);
                    int timeSeconds = Integer.parseInt(time)/1000;
                    if (event instanceof MessageEvent)
                        event.getBot().sendIRC().message(responseChan, Colors.BOLD+type+": "+Colors.NORMAL+calls+" calls may be made per every "+timeSeconds+"s in "+channel);
                    else
                        event.getBot().sendIRC().message(caller, Colors.BOLD+type+": "+Colors.NORMAL+calls+" calls may be made per every "+timeSeconds+"s in "+channel);
                }
                else
                    event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"Channel does not exist in throttle settings");
            }
            else
                event.getBot().sendIRC().message(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"Channel is a required input");
            
        }
//    else if (cmdSplit.length==3){
//
//    }
        else if (throttleModification!=Integer.MIN_VALUE&&!type.equalsIgnoreCase(null)){
            if (isVerified){
                if (!channel.equalsIgnoreCase(null)){
                    if(!modifier.equalsIgnoreCase(null)){
                        if (Global.throttle.contains(type+"log",channel)){
                            if (modifier.equalsIgnoreCase("log"))
                                Global.throttle.setMaxLog(type, throttleModification, channel);
                            else if (modifier.equalsIgnoreCase("time"))
                                Global.throttle.setMaxTime(type, throttleModification*1000, channel);
                            
                            
                            String calls = Global.throttle.get(type+"log",channel);
                            String time = Global.throttle.get(type+"time",channel);
                            int timeSeconds = Integer.parseInt(time)/1000;
                            
                            if (event instanceof MessageEvent)
                                event.getBot().sendIRC().message(responseChan, Colors.BOLD+type+": "+Colors.NORMAL+calls+" calls may be made per every "+timeSeconds+"s in "+channel);
                            else
                                event.getBot().sendIRC().message(caller, Colors.BOLD+type+": "+Colors.NORMAL+calls+" calls may be made per every "+timeSeconds+"s in "+channel);
                        }
                        else
                            event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"Channel does not exist in throttle settings");
                    }
                    else
                        event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"Modifier (log or time) is a required input");
                }
                else
                    event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"Channel is a required input");
            }
            else
                event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"You don't have access to this command");
        }
        
//!throttle [type] [time|call] [int]
//!throttle [type] [time|call] [int] [channel]
//Use !set for a more general command
//**Parse the int out of the call string, allow inputs in any order
//**channel begins with #
//If no strong begins with #, error
//If string doesn't begin with # and is neither "time" nor "call" then the string is the type
        
//**Get channel by checking for #, splitting by it if it exists, splitting by space, and grabbing the middle section
        
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("throttle");
        return a;
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
}