/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import Wheatley.Global;
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
 *    N/A
 * - Linked Classes
 *    N/A
 *
 * Object:
 *      CommandMetaData
 * - This object parses out a MessageEvent or PrivateMessageEvent into all the meta data
 *   including message, channel, channels from the message, user, and whether that user
 *   is verified as the bot owner, channel owner (from the event source) or channel
 *   owner from the channel in the message
 *
 * Methods:
 *      getCommands       - Sets the command and cmdSplit variables from the 
 *                          message string
 *     *getChannel        - Returns the channel the event was sent from, if the 
 *                          event was in a channel, else returns null
 *     *getCaller         - Returns the nickname of the user who sent the event
 *     *getCommand        - Gets the command string minus prefix
 *     *getCommandChannel - Returns the channel that was in the command, or the 
 *                          channel the command was sent from
 *     *getCommandSplit   - Returns a String[] of the command data without the 
 *                          prefix, and split by spaces
 *     *getEventChannel   - Returns the channel the event was sent from, or null
 *     *getEventType      - Returns either "privatemessage" or "channelmessage"
 *     *getMessage        - Returns the event message
 *     *isVerifiedChanOwner    - Returns true if the event user is verified and 
 *                               is the owner of the channel in the command or 
 *                               that the command was sent from
 *     *isVerifiedBotOwner     - Returns true if the event user is verified and 
 *                               is the bot owner
 *     *isVerifiedChanBotOwner - Returns true if the event user is verified and 
 *                               is either the bot owner, or the channel owner 
 *                               of the channel the command was sent from, or 
 *                               channel contained in the event
 *
 * Note: Only commands marked with a * are available for use outside the object
 *
 * Useful Resources
 * - N/A
 *
 * Version: 0.5.0
 */
public class CommandMetaData {
    private final Event event;
    private String caller;
    private String eventType;
    private String channel = null; // This channel is only changed if the event is a message event
    private String refChan = null; // This channel is only changed if one is in the input string
    private boolean isVerified = false;
    private boolean isBotOwner = false;
    private boolean isChanOwner = false;
    private String message;
    private String command;
    private String[] cmdSplit;
    
    public CommandMetaData(Event event, boolean verify){
        this.event = event;
        if (verify)
            processFullEvent();
        else
            processEventWithoutVerification();
    }
    
    private void processEventWithoutVerification(){
        if (event instanceof MessageEvent){ // MESSAGE EVENT SPECIFIC PARSING
            eventType = "ChannelMessage";
            MessageEvent mEvent = (MessageEvent) event;
            caller = mEvent.getUser().getNick();
            message = Colors.removeFormattingAndColors(mEvent.getMessage());
            
            if (message.contains("#")){
                refChan ="#" + message.split("#")[0].split(" ")[0];
            }
            else{
                channel = mEvent.getChannel().getName();
            }
            
            getCommands(); // REPLACES THE FOLLOWING IF STATEMENT
        }// END MESSAGE EVENT SPECIFIC PARSING
        
        else if (event instanceof PrivateMessageEvent){ // PRIVATE MESSAGE EVENT SPECIFIC PARSING
            eventType = "PrivateMessage";
            PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
            message = Colors.removeFormattingAndColors(pmEvent.getMessage());
            caller = pmEvent.getUser().getNick();
            
            if (message.contains("#")){
                refChan = "#" + message.split("#")[0].split(" ")[0];
            }
            getCommands();
        }// END PRIVATE MESSAGE EVENT SPECIFIC PARSING
    }
    
    private void processFullEvent(){
        if (event instanceof MessageEvent){ // MESSAGE EVENT SPECIFIC PARSING
            eventType = "ChannelMessage";
            MessageEvent mEvent = (MessageEvent) event;
            caller = mEvent.getUser().getNick();
            message = Colors.removeFormattingAndColors(mEvent.getMessage());
            
            if (message.contains("#")){
                refChan ="#" + message.split("#")[0].split(" ")[0];
                isVerified  = mEvent.getUser().isVerified();
                isBotOwner  = caller.equalsIgnoreCase(Global.botOwner);
                isChanOwner = mEvent.getUser().getChannelsOwnerIn().contains(mEvent.getBot().getUserChannelDao().getChannel(refChan));
            }
            else{
                channel = mEvent.getChannel().getName();
                isVerified  = mEvent.getUser().isVerified();
                isBotOwner  = caller.equalsIgnoreCase(Global.botOwner);
                isChanOwner = mEvent.getChannel().isOwner(mEvent.getUser());
            }
            
            getCommands(); // REPLACES THE FOLLOWING IF STATEMENT
//            if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}!{1,}[ ]{0,}){1,}")){
//
//                command = message.split(Global.commandPrefix)[1];
//                cmdSplit = command.split(" ");
//            }
        }// END MESSAGE EVENT SPECIFIC PARSING
        
        else if (event instanceof PrivateMessageEvent){ // PRIVATE MESSAGE EVENT SPECIFIC PARSING
            eventType = "PrivateMessage";
            PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
            message = Colors.removeFormattingAndColors(pmEvent.getMessage());
            caller = pmEvent.getUser().getNick();
            
            if (message.contains("#")){
                refChan = "#" + message.split("#")[0].split(" ")[0];
                isVerified  = pmEvent.getUser().isVerified();
                isBotOwner  = caller.equalsIgnoreCase(Global.botOwner);
                isChanOwner = pmEvent.getUser().getChannelsOwnerIn().contains(pmEvent.getBot().getUserChannelDao().getChannel(refChan));
            }
            else{
                isVerified = pmEvent.getUser().isVerified();
                isBotOwner = caller.equalsIgnoreCase(Global.botOwner);
            }
            getCommands();
        }// END PRIVATE MESSAGE EVENT SPECIFIC PARSING
    }
    
    private void getCommands(){
        
        // IF THE COMMAND STARTS WITH THE PREDETERMINED PREFIX AND CONTAINS MORE THAN JUST SPACES AND COMMAND CHARACTERS
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            
            command = message.split(Global.commandPrefix)[1];
            cmdSplit = command.split(" ");
        }
    }
    
    public boolean isVerifiedBotOwner(){
        return (isVerified&&isBotOwner);
    }
    public boolean isVerifiedChanOwner(){
        return (isVerified&&isBotOwner);
    }
    public boolean isVerifiedChanBotOwner(){
        return (isVerified&&(isBotOwner||isChanOwner));
    }
    public String getCaller(){
        return (caller);
    }
    public String getMessage(){
        return (message);
    }
    public String[] getCommandSplit(){
        return (cmdSplit);
    }
    public String getCommand(){
        return (command);
    }
    public String getEventType(){
        return (eventType);
    }
    public String getEventChannel(){
        return (channel);
    }
    public String getCommandChannel(){
        
//        if (channel==null && refChan == null)
//            return (null);
        if (channel == null && refChan != null)
            return (refChan);
        
        else if (channel != null && refChan == null)
            return(channel);
        
        else if (channel != null && refChan != null){
            
            if(!refChan.equalsIgnoreCase(channel))
                return (refChan);
            
            else
                return (channel);
        }
        else
            return (null);
    }
    //ORIGINAL CONSTRUCTOR THAT WAS NEVER USED
//    public CommandMetaData(Event event){
//        this.event = event;
//        
//        if (event instanceof MessageEvent){ // MESSAGE EVENT SPECIFIC PARSING
//            eventType = "ChannelMessage";
//            MessageEvent mEvent = (MessageEvent) event;
//            caller = mEvent.getUser().getNick();
//            message = Colors.removeFormattingAndColors(mEvent.getMessage());
//            
//            if (message.contains("#")){
//                refChan ="#" + message.split("#")[0].split(" ")[0];
//                isVerified  = mEvent.getUser().isVerified();
//                isBotOwner  = caller.equalsIgnoreCase(Global.botOwner);
//                isChanOwner = mEvent.getUser().getChannelsOwnerIn().contains(mEvent.getBot().getUserChannelDao().getChannel(refChan));
//            }
//            else{
//                channel = mEvent.getChannel().getName();
//                isVerified  = mEvent.getUser().isVerified();
//                isBotOwner  = caller.equalsIgnoreCase(Global.botOwner);
//                isChanOwner = mEvent.getChannel().isOwner(mEvent.getUser());
//            }
//            
//            getCommands(); // REPLACES THE FOLLOWING IF STATEMENT
////            if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}!{1,}[ ]{0,}){1,}")){
////
////                command = message.split(Global.commandPrefix)[1];
////                cmdSplit = command.split(" ");
////            }
//        }// END MESSAGE EVENT SPECIFIC PARSING
//        
//        else if (event instanceof PrivateMessageEvent){ // PRIVATE MESSAGE EVENT SPECIFIC PARSING
//            eventType = "PrivateMessage";
//            PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
//            message = Colors.removeFormattingAndColors(pmEvent.getMessage());
//            caller = pmEvent.getUser().getNick();
//            
//            if (message.contains("#")){
//                refChan = "#" + message.split("#")[0].split(" ")[0];
//                isVerified  = pmEvent.getUser().isVerified();
//                isBotOwner  = caller.equalsIgnoreCase(Global.botOwner);
//                isChanOwner = pmEvent.getUser().getChannelsOwnerIn().contains(pmEvent.getBot().getUserChannelDao().getChannel(refChan));
//            }
//            else{
//                isVerified = pmEvent.getUser().isVerified();
//                isBotOwner = caller.equalsIgnoreCase(Global.botOwner);
//            }
//            getCommands();
//        }// END PRIVATE MESSAGE EVENT SPECIFIC PARSING
//    }
}