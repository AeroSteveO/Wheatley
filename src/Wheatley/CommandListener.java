/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Wheatley;

import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class CommandListener extends ListenerAdapter{
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        System.out.println("Command list size " + Global.commandList.size());
        for (int i=0;i<Global.commandList.size();i++){
            if (Global.commandList.get(i).commandTerms().contains(message)){
                Global.commandList.get(i).processCommand(event);
            }
        }
    }
}
