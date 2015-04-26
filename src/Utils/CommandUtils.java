/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Utils;

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
 *    Global
 * 
 * Methods:
 *      isVerifiedBotOwner     - Checks that the user from the input event is the bot owner and is verified
 *      isVerifiedChanBotOwner - Checks that the user from the input event is either the bot owner or chan owner and is verified
 *      isVerifiedChanOwner    - Checks that the user from the input event is the chan owner and is verified
 * 
 * Note: Only commands marked with a * are available for use outside the object 
 */
public class CommandUtils {
    
    public static boolean isVerifiedBotOwner(Event event){
        boolean isVerified = false;
        String caller = new String();
        
        if (event instanceof MessageEvent){ // MESSAGE EVENT SPECIFIC PARSING
            
            MessageEvent mEvent = (MessageEvent) event;
            caller = mEvent.getUser().getNick();
            isVerified=(caller.equalsIgnoreCase(Global.botOwner))&&mEvent.getUser().isVerified();
        }// END MESSAGE EVENT SPECIFIC PARSING
        
        else if (event instanceof PrivateMessageEvent){ // PRIVATE MESSAGE EVENT SPECIFIC PARSING
            
            PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
            caller = pmEvent.getUser().getNick();
            isVerified=(caller.equalsIgnoreCase(Global.botOwner))&&pmEvent.getUser().isVerified();
        }// END PRIVATE MESSAGE EVENT SPECIFIC PARSING
        // END EVENT SPECIFIC PARSING
        return isVerified;
    }
    public static boolean isVerifiedChanBotOwner(Event event){
        boolean isVerified = false;
        String caller = new String();
        
        if (event instanceof MessageEvent){ // MESSAGE EVENT SPECIFIC PARSING
            
            MessageEvent mEvent = (MessageEvent) event;
            caller = mEvent.getUser().getNick();
            String message = Colors.removeFormattingAndColors(mEvent.getMessage());
            
            if (message.contains("#")){
                String chan ="#" + message.split("#")[0].split(" ")[0];
                isVerified=(caller.equalsIgnoreCase(Global.botOwner)||mEvent.getUser().getChannelsOwnerIn().contains(mEvent.getBot().getUserChannelDao().getChannel(chan)))&&mEvent.getUser().isVerified();
            }
            else{
                isVerified=((mEvent.getChannel().isOwner(mEvent.getUser())||caller.equalsIgnoreCase(Global.botOwner))&&mEvent.getUser().isVerified());
            }
        }// END MESSAGE EVENT SPECIFIC PARSING
        
        else if (event instanceof PrivateMessageEvent){ // PRIVATE MESSAGE EVENT SPECIFIC PARSING
            
            PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
            String message = Colors.removeFormattingAndColors(pmEvent.getMessage());
            caller = pmEvent.getUser().getNick();
            
            if (message.contains("#")){
                String chan ="#" + message.split("#")[0].split(" ")[0];
                isVerified=(caller.equalsIgnoreCase(Global.botOwner)||pmEvent.getUser().getChannelsOwnerIn().contains(pmEvent.getBot().getUserChannelDao().getChannel(chan)))&&pmEvent.getUser().isVerified();
            }
            else{
                isVerified=(caller.equalsIgnoreCase(Global.botOwner))&&pmEvent.getUser().isVerified();
            }
        }// END PRIVATE MESSAGE EVENT SPECIFIC PARSING
        // END EVENT SPECIFIC PARSING
        return isVerified;
    }
    
    public static boolean isVerifiedChanOwner(Event event){
        boolean isVerified = false;
        String caller = new String();
        
        if (event instanceof MessageEvent){ // MESSAGE EVENT SPECIFIC PARSING
            
            MessageEvent mEvent = (MessageEvent) event;
            caller = mEvent.getUser().getNick();
            String message = Colors.removeFormattingAndColors(mEvent.getMessage());
            
            if (message.contains("#")){
                String chan ="#" + message.split("#")[0].split(" ")[0];
                isVerified=(mEvent.getUser().getChannelsOwnerIn().contains(mEvent.getBot().getUserChannelDao().getChannel(chan)))&&mEvent.getUser().isVerified();
            }
            else{
                isVerified=((mEvent.getChannel().isOwner(mEvent.getUser()))&&mEvent.getUser().isVerified());
            }
        }// END MESSAGE EVENT SPECIFIC PARSING
        
        else if (event instanceof PrivateMessageEvent){ // PRIVATE MESSAGE EVENT SPECIFIC PARSING
            
            PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
            String message = Colors.removeFormattingAndColors(pmEvent.getMessage());
            caller = pmEvent.getUser().getNick();
            
            if (message.contains("#")){
                String chan ="#" + message.split("#")[0].split(" ")[0];
                isVerified=(pmEvent.getUser().getChannelsOwnerIn().contains(pmEvent.getBot().getUserChannelDao().getChannel(chan)))&&pmEvent.getUser().isVerified();
            }
            else{
                isVerified=false;
            }
        }// END PRIVATE MESSAGE EVENT SPECIFIC PARSING
        // END EVENT SPECIFIC PARSING
        return isVerified;
    }
}