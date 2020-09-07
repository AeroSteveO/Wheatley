/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * Used to update .txt files
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Utilities
 *    TextUtils
 * - Linked Classes
 *    Global
 *
 * Activate commands with:
 *      !update [filename] [singleWordItem]
 *          !update badwords defenetely
 */
public class UpdateFiles extends ListenerAdapter{
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}!{1,}[ ]{0,}){1,}")){
//            if (!message.matches("([ ]{0,}!{1,}[ ]{0,}){1,}")){
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            if (cmdSplit.length>0){
                if (cmdSplit[0].equalsIgnoreCase("save")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                    Global.settings.save();
                    Global.throttle.save();
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "Settings file saved");
                }
                else if (cmdSplit[0].equalsIgnoreCase("set")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                    if (cmdSplit.length==3){
                        
                        boolean success = Global.settings.set(cmdSplit[1], cmdSplit[2], event.getChannel().getName());
                        if (!success)
                            event.getBot().sendIRC().notice(event.getUser().getNick(), "Setting failed to update: Setting does not exist");
                        else
                            event.respond("SUCCESS IN MODIFYING SETTING");
                    }
                    else if(cmdSplit.length==4){
                        boolean success = Global.settings.set(cmdSplit[1], cmdSplit[2], cmdSplit[3]);
                        if (!success){
                            event.getBot().sendIRC().notice(event.getUser().getNick(), "Setting failed to update: Setting does not exist");
                        }
                        
                    }
                }
                else if (cmdSplit[0].equalsIgnoreCase("create")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                    
                    ArrayList<String> tree = new ArrayList<>(Arrays.asList(cmdSplit));
                    tree.remove(0);
                    Global.settings.create(tree);
                    event.respond(String.valueOf("Maybe its been modified? "+Global.settings.contains(tree.subList(0, tree.size()-1))));
                    
                }
                
                else if (cmdSplit[0].equalsIgnoreCase("contains")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                    
                    ArrayList<String> tree = new ArrayList<>(Arrays.asList(cmdSplit));
                    tree.remove(0);
                    event.respond(String.valueOf(Global.settings.contains(tree)));
                }
                
                
                else if (cmdSplit[0].equalsIgnoreCase("get")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                    
                    if (cmdSplit.length==2){
                        event.respond(Global.settings.get(cmdSplit[1]));
                        
                    }
                    else if (cmdSplit.length==3){
                        event.respond(Global.settings.get(cmdSplit[1],cmdSplit[2]));
                    }
                }
            }
        }
    }
}