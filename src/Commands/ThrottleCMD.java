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
import Objects.CommandMetaData;
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
 *    Throttle
 * - Linked Classes
 *    Global
 *
 */
//!throttle [type] [time|call] [int]
//!throttle [type] [time|call] [int] [channel]
//Use !set for a more general command
//**Parse the int out of the call string, allow inputs in any order
//**channel begins with #
//If no strong begins with #, error
//If string doesn't begin with # and is neither "time" nor "call" then the string is the type

//**Get channel by checking for #, splitting by it if it exists, splitting by space, and grabbing the middle section

@CMD
@GenCMD

public class ThrottleCMD implements Command {
    
    @Override
    public String toString(){
        return("THROTTLE");
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
    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData data = new CommandMetaData(event,true);
        
//        String message = data.getMessage();
        String caller = data.getCaller();
        String channel = data.getEventChannel();
        String responseChan = data.getEventChannel();
        boolean isVerified = data.isVerifiedChanBotOwner();
        
        // START EVENT SPECIFIC PARSING
//        if (event instanceof MessageEvent){ // MESSAGE EVENT SPECIFIC PARSING
//            MessageEvent mEvent = (MessageEvent) event;
//            message = Colors.removeFormattingAndColors(mEvent.getMessage());
//            caller = mEvent.getUser().getNick();
//            channel = mEvent.getChannel().getName();
//            responseChan = channel;
//
//            if (message.split(" ").length==5){
//                isVerified=(caller.equalsIgnoreCase(Global.botOwner)||mEvent.getUser().getChannelsOwnerIn().contains(mEvent.getBot().getUserChannelDao().getChannel("#" + message.split("\\#")[1].split(" ")[0])))&&mEvent.getUser().isVerified();
//            }
//            else if (message.split(" ").length==4){
//                isVerified=((mEvent.getChannel().isOwner(mEvent.getUser())||caller.equalsIgnoreCase(Global.botOwner))&&mEvent.getUser().isVerified());
//            }
//        }// END MESSAGE EVENT SPECIFIC PARSING
//        else if (event instanceof PrivateMessageEvent){ // PRIVATE MESSAGE EVENT SPECIFIC PARSING
//            PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
//            message = Colors.removeFormattingAndColors(pmEvent.getMessage());
//            caller = pmEvent.getUser().getNick();
//
//            if (message.split(" ").length==5)
//                isVerified=(caller.equalsIgnoreCase(Global.botOwner)||pmEvent.getUser().getChannelsOwnerIn().contains(pmEvent.getBot().getUserChannelDao().getChannel("#" + message.split("\\#")[1].split(" ")[0])))&&pmEvent.getUser().isVerified();
//        }// END PRIVATE MESSAGE EVENT SPECIFIC PARSING
//        else{
//            return;
//        }
        // END EVENT SPECIFIC PARSING
        
        // COMMAND SPLITTING
//        String command = data.getCommand();
        String[] cmdSplit = data.getCommandSplit();
        
        // INITIAL VALUES
        String type = null;
        String modifier = null;
        int throttleModification = Integer.MIN_VALUE;
        
        
        if (cmdSplit.length == 2 && cmdSplit[1].equalsIgnoreCase("list")){
            
            ArrayList<String> types = Global.throttle.getAllTypes();
            String response = Colors.BOLD+"Throttle Types: "+Colors.NORMAL;
            
            for (int i=0;i<types.size()-1;i++){
                response+=types.get(i)+", ";
            }
            response+=types.get(types.size()-1);
            
            event.getBot().sendIRC().notice(caller, response );
        }
        else{
            
            for (int i=0;i<cmdSplit.length;i++){
                if (cmdSplit[i].startsWith("#"))
                    channel = cmdSplit[i];
                else if (cmdSplit[i].equalsIgnoreCase("log")||cmdSplit[i].equalsIgnoreCase("time"))
                    modifier = cmdSplit[i];
                else if (cmdSplit[i].matches("[0-9]+"))
                    throttleModification = Integer.parseInt(cmdSplit[i]);
                else if (!cmdSplit[i].equalsIgnoreCase("list")) // list isn't a type
                    type = cmdSplit[i];
            }
            
            
            if (cmdSplit.length>5){
                event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"Too many inputs");
            }
            else if (cmdSplit.length==1){
                event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"Not enough inputs, requires [type] [channel] minimum");
            }
            else if (channel == null){
                event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"Channel is a required input (!Throttle [channel] [type] [modifier] [int])");
            }
            else if(modifier == null){
                event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"Modifier is a required input, Modifier may be either 'log' or 'time'");
            }
            else if (type == null){
                event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"Type is a required input (!Throttle [channel] [type] [modifier] [int])");
            }
            else if (cmdSplit.length<=4){
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
            else{
                
                if (throttleModification!=Integer.MIN_VALUE){
                    if (isVerified){
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
                        event.getBot().sendIRC().notice(caller, Colors.BOLD+"Throttle: "+Colors.NORMAL+"You don't have access to this command");
                }
            }
        }
    }
}