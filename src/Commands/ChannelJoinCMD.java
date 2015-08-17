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
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 */
public class ChannelJoinCMD implements Command {
    
    @Override
    public String toString() {
        return("Join: Allows the bot to be commanded to join a channel");
    }
    
    @Override
    public boolean isCommand(String toCheck) {
        return toCheck.toLowerCase().startsWith(Global.mainNick.toLowerCase() + ", join #"); // Phrase that when spoken will activate the command
    }
    
    @Override
    public ArrayList<String> commandTerms() {
        ArrayList<String> a = new ArrayList<>();
//        a.add("join");
        return a;
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        a.add(Colors.BOLD + Global.mainNick + ", join [#channel]" + Colors.NORMAL + ": Commands the bot to join the input channel" );
        return a;
    }
    
    
    @Override
    public void processCommand(Event event) {
        
        CommandMetaData data = new CommandMetaData(event, true);
        
        boolean isVerified = data.isVerifiedBotOwner(); // True if the user who called the command is the bot owner
        String message = data.getMessage();
//        String respondTo = data.respondToCallerOrMessageChan();
        String respondTo = data.respondToIgnoreMessage();
        
        // command the bot to join channels
        if (isVerified) {
            String[] chan = message.split("#");
            if (message.toLowerCase().contains("#")){
                event.getBot().sendIRC().message(respondTo,"Joining #" + chan[1]);
                event.getBot().sendIRC().joinChannel("#" + chan[1]);
                Global.channels.add("#"+chan[1].toLowerCase());
            }
            else
                event.getBot().sendIRC().message(respondTo,"Join: No input channel found");
        }
    }
}
