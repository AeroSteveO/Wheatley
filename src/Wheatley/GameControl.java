package Wheatley;

import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author Stephen
 */
public class GameControl extends ListenerAdapter {
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.startsWith(Global.commandPrefix)){
            String command = message.split(Global.commandPrefix)[1];
            if (command.equalsIgnoreCase("!flush")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                
            }
            else if(command.equalsIgnoreCase("list games")){
                ArrayList<String> descriptions = Global.activeGame.getCurrentGameDescriptions();
                for (int i=0;i<descriptions.size();i++){
                    event.getBot().sendIRC().message(event.getChannel().getName(),descriptions.get(i));
                }
            }
        }
    }
}
